package com.ratz.greenbites.services;

import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.entity.UserPrincipal;
import com.ratz.greenbites.repository.RoleRepository;
import com.ratz.greenbites.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        log.info("User found by email: {}", email);

        if (user == null) throw new UsernameNotFoundException("User not found");
        else return new UserPrincipal(user, roleRepository.getRoleByPlayerId(user.getId()).getPermission());
    }
}
