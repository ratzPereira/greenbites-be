package com.ratz.greenbites.services;


import com.ratz.greenbites.entity.Role;

public interface RoleService {

    Role getRoleByPlayerId(Long playerId);
}
