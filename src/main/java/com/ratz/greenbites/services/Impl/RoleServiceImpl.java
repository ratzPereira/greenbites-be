package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.entity.Role;
import com.ratz.greenbites.repository.RoleRepository;
import com.ratz.greenbites.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByPlayerId(Long playerId) {
        return roleRepository.getRoleByPlayerId(playerId);
    }
}
