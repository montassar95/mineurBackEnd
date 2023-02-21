package com.cgpr.mineur.repositorySecurity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cgpr.mineur.modelsSecurity.ERole;
import com.cgpr.mineur.modelsSecurity.Role;

 

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
