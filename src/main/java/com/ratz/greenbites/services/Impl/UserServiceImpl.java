package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.RegisterFormDTO;
import com.ratz.greenbites.DTO.UserDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.Role;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.exception.ApiException;
import com.ratz.greenbites.mapper.UserMapper;
import com.ratz.greenbites.messaging.QueueService;
import com.ratz.greenbites.repository.ProfileRepository;
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
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder encoder;
    private final QueueService queueService;

    @Override
    public UserDTO createUser(RegisterFormDTO registerFormDTO) {
        log.info("Attempting to register a new user with email: {}", registerFormDTO.getEmail());
        // Check the email is unique
        if (userRepository.existsByEmail(registerFormDTO.getEmail())) {
            throw new ApiException("Email already exists");
        }

        //save user
        try {

            User user = new User();
            // Add role to user
            Role userRole = roleRepository.findByName(ROLE_USER.name())
                    .orElseThrow(() -> new ApiException("Role not found"));

            user.getRoles().add(userRole);
            user.setEmail(registerFormDTO.getEmail());

            // Encode password
            user.setPassword(encoder.encode(registerFormDTO.getPassword()));

            // TODO send verification email
            user.setUsingMfa(false);
            user.setEnabled(true);
            user.setNotLocked(true);

            //create empty profile
            Profile profile = new Profile();
            profile.setFirstName(registerFormDTO.getFirstName());
            profile.setLastName(registerFormDTO.getLastName());
            user.setProfile(profile);

            //save user
            userRepository.save(user);

            //save profile
            profile.setUser(user);
            profileRepository.save(profile);

            //send welcome email
            queueService.sendEmailRequest(user.getEmail());

            // Convert and Return the newly created user
            return UserMapper.INSTANCE.userToUserDTO(user);

            // If errors, throw exception with proper message
        } catch (Exception e) {
            throw new ApiException("Error creating user");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }
}
