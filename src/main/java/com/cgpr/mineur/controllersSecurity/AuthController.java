package com.cgpr.mineur.controllersSecurity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.converter.EtablissementConverter;
import com.cgpr.mineur.models.Etablissement;
import com.cgpr.mineur.modelsSecurity.ERole;
import com.cgpr.mineur.modelsSecurity.Role;
import com.cgpr.mineur.modelsSecurity.User;
import com.cgpr.mineur.payload.request.LoginRequest;
import com.cgpr.mineur.payload.request.SignupRequest;
import com.cgpr.mineur.payload.request.SignupRequestByAdmin;
import com.cgpr.mineur.payload.response.JwtResponse;
import com.cgpr.mineur.payload.response.MessageResponse;
import com.cgpr.mineur.repository.EtablissementRepository;
import com.cgpr.mineur.repositorySecurity.RoleRepository;
import com.cgpr.mineur.repositorySecurity.UserRepository;
import com.cgpr.mineur.security.jwt.JwtUtils;
import com.cgpr.mineur.security.services.UserDetailsImpl;
import com.cgpr.mineur.sms.EnvoiSmsService;
import com.cgpr.mineur.tools.PasswordUtils;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	EtablissementRepository etablissementRepository;
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
     EnvoiSmsService envoiSmsService;

	 
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
	    );

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);

	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

	    User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);

	    userRepository.updateLastLoginDate(user.getUsername(), LocalDateTime.now());

	    Map<String, Object> checkResult = checkUserWarnings(user);

	    JwtResponse response = new JwtResponse(jwt);
	    response.setWarnings((List<String>) checkResult.get("warnings"));
	    

	    return ResponseEntity.ok(response);
	}

	public Map<String, Object> checkUserWarnings(User user) {
	    Map<String, Object> result = new HashMap<>();
	    List<String> warnings = new ArrayList<>();
	    
	    if (user == null) {
	        warnings.add("⚠️ لا يوجد مستخدم، الرجاء تسجيل الدخول من جديد.");
	        result.put("status", "invalid");
	        result.put("warnings", warnings);
	        return result;
	    }
	    warnings.add("⚠️يرجى من جميع الراغبين في الاطلاع على بيانات الطفل الجناح داخل الوحدات السجنية تقديم مكتوب إلى إدارة الإشراف (مركز الإعلامية منوبة أو إدارة شؤون المودعين ).");
	    if (user.getTelephone() == null || user.getTelephone().isEmpty()) {
	        warnings.add("⚠️ رقم الهاتف غير موجود، الرجاء الاتصال بمركز الإعلامية لتحديث بياناتك، أو سيتم غلق الحساب في المرة القادمة.");
	    }

	    if (user.getLastPasswordModifiedDate() == null) {
	        warnings.add("⚠️ لم تقم بتغيير كلمة السر من قبل، الرجاء تغييرها، أو سيتم غلق الحساب في المرة القادمة.");
	    } else {
	        LocalDateTime now = LocalDateTime.now();
	        if (user.getLastPasswordModifiedDate().isBefore(now.minusDays(30))) {
	            warnings.add("⚠️ لقد مر أكثر من 30 يومًا على آخر تغيير لكلمة السر، الرجاء تغييرها، أو سيتم غلق الحساب في المرة القادمة.");
	        }
	    }

	    if (user.getLastLogin() == null) {
	        warnings.add("⚠️ لم يتم تسجيل الدخول من قبل.");
	    } else {
	        LocalDateTime now = LocalDateTime.now();
	        if (user.getLastLogin().isBefore(now.minusDays(20))) {
	            warnings.add("⚠️ لقد مر أكثر من 20 يومًا منذ آخر تسجيل دخول، الرجاء الدخول بصفة منتظمة، أو سيتم غلق الحساب في المرة القادمة.");
	        }
	    }


	    result.put("status", "ok");
	    result.put("warnings", warnings);
	    return result;
	}
 
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(
	        @Valid @RequestBody SignupRequestByAdmin signUpRequest,
	        BindingResult bindingResult) {
 System.out.println(signUpRequest.toString());
	    if (bindingResult.hasErrors()) {
	        Map<String, String> errors = new HashMap<>();
	        bindingResult.getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage())
	        );
	        return ResponseEntity.badRequest().body(errors);
	    }

	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	        Map<String, String> usernameError = new HashMap<>();
	        usernameError.put("username", "Ce nom d'utilisateur est déjà pris.");
	        return ResponseEntity.badRequest().body(usernameError);
	    }

	    User user = new User();
	    user.setUsername(signUpRequest.getUsername());
	    user.setNom(signUpRequest.getNom());
	    user.setPrenom(signUpRequest.getPrenom());
	    user.setNumAdministratif(signUpRequest.getNumAdministratif());
	    user.setTelephone(signUpRequest.getTelephone());
	   // user.setEtablissement(EtablissementConverter.dtoToEntity(signUpRequest.getEtablissement())); // ici l'etablicemeit copose de etablisement et gouvernorat donc il faut envoyer just id etablisement
	    Etablissement etab = etablissementRepository.findById(signUpRequest.getEtablissementId())
	    	    .orElseThrow(() -> new RuntimeException("Etablissement non trouvé"));
	    	user.setEtablissement(etab); // ✅ pas besoin de convertir quoi que ce soit

	    String randomPwd = PasswordUtils.generateRandomPassword(8, false);
	    user.setPassword(encoder.encode(randomPwd));

	    Set<String> strRoles = signUpRequest.getRole();
	    Set<Role> roles = new HashSet<>();
	    if (strRoles == null || strRoles.isEmpty()) {
	        roles.add(getRoleOrThrow(ERole.ROLE_USER));
	    } else {
	        for (String role : strRoles) {
	            roles.add(getRoleFromString(role));
	        }
	    }
	    user.setRoles(roles);

	    userRepository.save(user);
	    String from = "SOUJOUN";
	    // Envoi du mot de passe par SMS
	    if (user.getTelephone() != null && !user.getTelephone().isEmpty()) {
	    	String message = "مرحبًا " + user.getNom()+" " + user.getPrenom() + "، لقد تم إنشاء حسابك بنجاح.\n" 
	                + "اسم المستخدم الخاص بك هو: " + user.getUsername() + "\n"
	                + "كلمة المرور الخاصة بك هي: " + randomPwd;

	    	//CentreMineurs
	        
	        envoiSmsService.envoyerSms(from , "216"+user.getTelephone(), message)
	            .subscribe(success -> {
	                if (!success) {
	                    System.err.println("Échec de l'envoi du SMS à " + user.getTelephone());
	                }
	            });
	    }

	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Utilisateur enregistré avec succès !");
	    response.put("password", randomPwd); // <- tu peux enlever ça si tu ne veux plus afficher le mot de passe

	    return ResponseEntity.ok(response);
	}

	private Role getRoleFromString(String role) {
	    switch (role.toLowerCase()) {
	        case "admin": return getRoleOrThrow(ERole.ROLE_ADMIN);
	        case "mod": return getRoleOrThrow(ERole.ROLE_MODERATOR);
	        case "user": return getRoleOrThrow(ERole.ROLE_USER);
	        case "soc": return getRoleOrThrow(ERole.ROLE_SOCIAL_USER);
	        default:  return getRoleOrThrow(ERole.ROLE_DIRECTEUR); 
	    }
	}
//	@PostMapping("/signup")
//	public ResponseEntity<?> registerUser(
//	        @Valid @RequestBody SignupRequest signUpRequest,
//	        BindingResult bindingResult) {
//
//	    // ✅ Vérification manuelle des erreurs de validation via BindingResult
//	    // BindingResult permet de récupérer les erreurs de validation JSR-380 (ex: @Size, @Pattern)
//	    // Cela permet de personnaliser la réponse sans que Spring lève une exception automatiquement
//	    if (bindingResult.hasErrors()) {
//	        Map<String, String> errors = new HashMap<>();
//	        bindingResult.getFieldErrors().forEach(error -> {
//	            errors.put(error.getField(), error.getDefaultMessage());
//	        });
//	        return ResponseEntity.badRequest().body(errors); // 🔁 renvoie des messages lisibles pour le front Angular
//	    }
//
//	    // 🔁 Vérifie si le nom d'utilisateur existe déjà en base
//	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//	        Map<String, String> usernameError = new HashMap<>();
//	        usernameError.put("username", "Ce nom d'utilisateur est déjà pris.");
//	        return ResponseEntity.badRequest().body(usernameError);
//	    }
//
//	    // 🔧 Création de l'entité User à partir du DTO
//	    User user = new User();
//	    user.setUsername(signUpRequest.getUsername());
//	    user.setPassword(encoder.encode(signUpRequest.getPassword())); // encodage du mot de passe
//	    user.setNom(signUpRequest.getNom());
//	    user.setPrenom(signUpRequest.getPrenom());
//	    user.setNumAdministratif(signUpRequest.getNumAdministratif());
//	    user.setEtablissement(EtablissementConverter.dtoToEntity(signUpRequest.getEtablissement()));
//
//	    // 🎯 Attribution des rôles
//	    Set<String> strRoles = signUpRequest.getRole();
//	    Set<Role> roles = new HashSet<>();
//
//	    if (strRoles == null || strRoles.isEmpty()) {
//	        roles.add(getRoleOrThrow(ERole.ROLE_USER));
//	    } else {
//	        for (String role : strRoles) {
//	            switch (role.toLowerCase()) {
//	                case "admin":
//	                    roles.add(getRoleOrThrow(ERole.ROLE_ADMIN));
//	                    break;
//	                case "mod":
//	                    roles.add(getRoleOrThrow(ERole.ROLE_MODERATOR));
//	                    break;
//	                case "dir":
//	                    roles.add(getRoleOrThrow(ERole.ROLE_DIRECTEUR));
//	                    break;
//	                default:
//	                    roles.add(getRoleOrThrow(ERole.ROLE_USER));
//	                    break;
//	            }
//	        }
//	    }
//
//	    user.setRoles(roles);
//	    userRepository.save(user); // 💾 enregistrement en base
//
//	    // 🔁 Réponse de succès formatée pour Angular
//	    Map<String, String> success = new HashMap<>();
//	    success.put("message", "Utilisateur enregistré avec succès !");
//	    return ResponseEntity.ok(success);
//	}



	// Méthode utilitaire privée
	private Role getRoleOrThrow(ERole roleName) {
	    return roleRepository.findByName(roleName)
	            .orElseThrow(() -> new RuntimeException("Erreur : Rôle " + roleName + " introuvable."));
	}


	
	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @Valid @RequestBody SignupRequest signUpRequest) {
	    // Vérifier si l'utilisateur existe
	    User existingUser = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Error: User not found with id " + id));

	    // Vérification si le nouveau username est déjà utilisé par un autre utilisateur
	    if (signUpRequest.getUsername() != null && !signUpRequest.getUsername().equals(existingUser.getUsername())) {
	        Optional<User> userWithSameUsername = userRepository.findByUsernameAndIdNot(signUpRequest.getUsername(), id);
	        if (userWithSameUsername.isPresent()) {
	            return ResponseEntity
	                    .badRequest()
	                    .body(new MessageResponse("Error: Username is already taken by another user!"));
	        }
	        // Mise à jour du username
	        existingUser.setUsername(signUpRequest.getUsername());
	    }

//	    // Mise à jour du mot de passe si présent
//	    if (signUpRequest.getPassword() != null) {
//	        existingUser.setPassword(encoder.encode(signUpRequest.getPassword()));
//	    }

	    // Mise à jour des autres champs (nom, prénom, etc.)
	    if (signUpRequest.getNom() != null) {
	        existingUser.setNom(signUpRequest.getNom());
	    }

	    if (signUpRequest.getPrenom() != null) {
	        existingUser.setPrenom(signUpRequest.getPrenom());
	    }

	    if (signUpRequest.getNumAdministratif() != null) {
	        existingUser.setNumAdministratif(signUpRequest.getNumAdministratif());
	    }
	    
	    if (signUpRequest.getTelephone() != null) {
	        existingUser.setTelephone(signUpRequest.getTelephone());
	    }
	    
	    if (signUpRequest.getEtablissementId() != null) {
	       
	        Etablissement etab = etablissementRepository.findById(signUpRequest.getEtablissementId())
		    	    .orElseThrow(() -> new RuntimeException("Etablissement non trouvé"));
	        existingUser.setEtablissement(etab); // ✅ pas besoin de convertir quoi que ce soit
	    }

	    // Mise à jour des rôles
	    Set<String> strRoles = signUpRequest.getRole();
	    Set<Role> roles = new HashSet<>();

	    if (strRoles == null || strRoles.isEmpty()) {
	        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	        roles.add(userRole);
	    } else {
	        strRoles.forEach(role -> {
	            switch (role) {
	                case "admin":
	                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(adminRole);
	                    break;
	                case "mod":
	                    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(modRole);
	                    break;
	                case "dir":
	                    Role dirRole = roleRepository.findByName(ERole.ROLE_DIRECTEUR)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(dirRole);
	                    break;
	                default:
	                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(userRole);
	            }
	        });
	    }

	    // Mise à jour des rôles de l'utilisateur
	    existingUser.setRoles(roles);

	    // Sauvegarder l'utilisateur mis à jour
	    userRepository.save(existingUser);

	    return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
	}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
	    // Check if the user exists
	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Error: User not found with id " + id));
	    
	    // Delete the user from the database
	    userRepository.delete(user);
	    
	    return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
	}
	

}
