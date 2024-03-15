package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.RegisterFormDTO;
import com.ratz.greenbites.DTO.UserDTO;

public interface UserService {

    UserDTO createUser(RegisterFormDTO registerFormDTO);
}
