package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.model.Role;
import com.proxym.yacine.codintest.repository.RoleRepository;
import com.proxym.yacine.codintest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
