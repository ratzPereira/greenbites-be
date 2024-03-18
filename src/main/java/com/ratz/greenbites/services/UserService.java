package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.auth.RegisterFormDTO;
import com.ratz.greenbites.DTO.auth.UserDTO;
import com.ratz.greenbites.entity.User;

public interface UserService {

    UserDTO createUser(RegisterFormDTO registerFormDTO);

    User getUserByEmail(String email);

    User getUserById(Long id);
}
