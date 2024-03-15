package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.RegisterFormDTO;
import com.ratz.greenbites.DTO.UserDTO;
import com.ratz.greenbites.entity.User;

public interface UserService {

    UserDTO createUser(RegisterFormDTO registerFormDTO);

    User getUserByEmail(String email);
}
