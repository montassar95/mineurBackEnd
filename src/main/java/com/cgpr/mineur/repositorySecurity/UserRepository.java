package com.cgpr.mineur.repositorySecurity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.models.Personelle;
import com.cgpr.mineur.modelsSecurity.User;
 
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
	@Query("SELECT u.personelle FROM User u ")
	List<Personelle> allUsers ( );
	
	
	// Méthode personnalisée pour vérifier si un nom d'utilisateur est déjà utilisé, en excluant l'utilisateur actuel
    Optional<User> findByUsernameAndIdNot(String username, Long id);
	 
//	Boolean existsByEmail(String email);
}
