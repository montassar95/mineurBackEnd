package com.cgpr.mineur.service.Impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cgpr.mineur.exception.InvalidPasswordException;
import com.cgpr.mineur.modelsSecurity.User;
import com.cgpr.mineur.repositorySecurity.UserRepository;
import com.cgpr.mineur.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean checkOldPassword(Long userId, String oldPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

   
    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        if (!checkOldPassword(userId, oldPassword)) {
            throw new InvalidPasswordException("كلمة المرور القديمة غير صحيحة");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordModifiedDate(LocalDateTime.now());  // ✅ mise à jour de la date

        userRepository.save(user);
    }


	@Override
	public User getById(Long userId) {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));
		return user;
	}
	
}
