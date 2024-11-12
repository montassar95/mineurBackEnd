package com.cgpr.mineur.service;

import com.cgpr.mineur.dto.AppelEnfantDto;
import com.cgpr.mineur.dto.DelegationDto;
import com.cgpr.mineur.modelsSecurity.User;


 



public interface UserService {
    
    boolean checkOldPassword(Long userId, String oldPassword);
    
    void updatePassword(Long userId, String oldPassword, String newPassword);
      User getById(Long id) ;
}

