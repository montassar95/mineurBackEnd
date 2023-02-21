package com.cgpr.mineur.controllersSecurity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgpr.mineur.models.ApiResponse;
import com.cgpr.mineur.models.Nationalite;
import com.cgpr.mineur.modelsSecurity.User;
import com.cgpr.mineur.repository.NationaliteRepository;
import com.cgpr.mineur.repositorySecurity.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/all")
	public ApiResponse<List<User>> listUser() {
		return new ApiResponse<>(HttpStatus.OK.value(), "  List Fetched Successfully.",
				userRepository.findAll());
	}

	 
}
