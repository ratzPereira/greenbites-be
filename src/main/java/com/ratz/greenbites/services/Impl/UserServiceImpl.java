package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.RegisterFormDTO;
import com.ratz.greenbites.DTO.UserDTO;
import com.ratz.greenbites.entity.Role;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.exception.ApiException;
import com.ratz.greenbites.mapper.UserMapper;
import com.ratz.greenbites.repository.RoleRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.ratz.greenbites.enums.RoleType.ROLE_USER;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserDTO createUser(RegisterFormDTO registerFormDTO) {
        log.info("Attempting to register a new user with username: {}", registerFormDTO.getUsername());
        // Check the email is unique
        if (userRepository.existsByEmail(registerFormDTO.getEmail())) {
            throw new ApiException("Email already exists");
        }

        // Check the username is unique
        if (userRepository.existsByUsername(registerFormDTO.getUsername())) {
            throw new ApiException("Username already exists");
        }

        //save user
        try {

            User user = new User();
            // Add role to user
            Role userRole = roleRepository.findByName(ROLE_USER.name())
                    .orElseThrow(() -> new ApiException("Role not found"));

            user.getRoles().add(userRole);
            user.setEmail(registerFormDTO.getEmail());
            user.setUsername(registerFormDTO.getUsername());

            // Encode password
            user.setPassword(encoder.encode(registerFormDTO.getPassword()));

            // TODO send verification email
            user.setUsingMfa(false);
            user.setEnabled(true);
            user.setNotLocked(true);

            // Convert and Return the newly created user
            userRepository.save(user);
            return UserMapper.INSTANCE.userToUserDTO(user);


            // If errors, throw exception with proper message
        } catch (Exception e) {
            throw new ApiException("Error creating user");
        }
    }
}
