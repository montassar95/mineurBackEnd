package com.cgpr.mineur.repositorySecurity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//import com.cgpr.mineur.models.Personelle;
import com.cgpr.mineur.modelsSecurity.User;
 
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
//	@Query("SELECT u.personelle FROM User u ")
//	List<Personelle> allUsers ( );
	
	
	// Méthode personnalisée pour vérifier si un nom d'utilisateur est déjà utilisé, en excluant l'utilisateur actuel
    Optional<User> findByUsernameAndIdNot(String username, Long id);
	 
//	Boolean existsByEmail(String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.username = :username")
    int updateLastLoginDate(@Param("username") String username, @Param("lastLogin") LocalDateTime lastLogin);

}
