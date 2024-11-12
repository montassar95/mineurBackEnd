package com.cgpr.mineur.controllersSecurity;

import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.converter.EtablissementConverter;
import com.cgpr.mineur.modelsSecurity.ERole;
import com.cgpr.mineur.modelsSecurity.Role;
import com.cgpr.mineur.modelsSecurity.User;
import com.cgpr.mineur.payload.request.LoginRequest;
import com.cgpr.mineur.payload.request.SignupRequest;
import com.cgpr.mineur.payload.response.JwtResponse;
import com.cgpr.mineur.payload.response.MessageResponse;
import com.cgpr.mineur.repositorySecurity.RoleRepository;
import com.cgpr.mineur.repositorySecurity.UserRepository;
import com.cgpr.mineur.security.jwt.JwtUtils;
import com.cgpr.mineur.security.services.UserDetailsImpl;


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
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponse(    jwt, 
													 userDetails.getId(), 
													 userDetails.getUsername(), 
													 roles,
													 userDetails.getEtablissement() ,
													 userDetails.getNom(),
													 userDetails.getPrenom()
												));
	}
	
 

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println(signUpRequest.toString());
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		 

		// Create new user's account
		User user = new User( );
		
		user.setUsername(signUpRequest.getUsername());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));
 
		user.setNom(signUpRequest.getNom());
		user.setPrenom(signUpRequest.getPrenom());
		user.setNumAdministratif(signUpRequest.getNumAdministratif());
		user.setEtablissement(EtablissementConverter.dtoToEntity(signUpRequest.getEtablissement()));

 		Set<String> strRoles = signUpRequest.getRole();
 		 
 		
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
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

 		user.setRoles(roles);
		System.out.println(user.toString());
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
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

	    // Mise à jour du mot de passe si présent
	    if (signUpRequest.getPassword() != null) {
	        existingUser.setPassword(encoder.encode(signUpRequest.getPassword()));
	    }

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

	    if (signUpRequest.getEtablissement() != null) {
	        existingUser.setEtablissement(EtablissementConverter.dtoToEntity(signUpRequest.getEtablissement()));
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

}
