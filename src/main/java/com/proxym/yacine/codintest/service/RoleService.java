package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.model.Role;

public interface RoleService {
    Role save(Role role);
    Role findByName(String name);
}
