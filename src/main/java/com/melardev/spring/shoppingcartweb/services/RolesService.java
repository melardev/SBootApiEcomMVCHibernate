package com.melardev.spring.shoppingcartweb.services;

import com.melardev.spring.shoppingcartweb.errors.exceptions.ResourceNotFoundException;
import com.melardev.spring.shoppingcartweb.models.Role;
import com.melardev.spring.shoppingcartweb.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    @Autowired
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Role save(Role role) {
        return this.rolesRepository.save(role);
    }

    public Role save(String name) {
        return this.rolesRepository.save(new Role(name));
    }

    public Role get(String name, boolean throwExceptionIfNotFound) {
        Optional<Role> role = this.rolesRepository.findByName(name);
        if (throwExceptionIfNotFound && !role.isPresent())
            throw new ResourceNotFoundException();
        return role.orElse(null);
    }

    public Role getRoleDontThrow(String name) {
        return get(name, false);
    }

    public Role getRoleOrThrow(String name) {
        return get(name, true);
    }

    public List<Role> findAll() {
        return rolesRepository.findAll();
    }

    public Role getOrCreate(String roleName) {
        Optional<Role> role = rolesRepository.findByName(roleName);
        return role.orElseGet(new Supplier<Role>() {
            @Override
            public Role get() {
                return rolesRepository.save(new Role(roleName));
            }
        });
    }

    public Page<Role> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Role> pagedRoles = this.rolesRepository.findAll(pageRequest);
        Object[][] result = rolesRepository.findUsersCountForRoleIds(pagedRoles.stream().map(r -> r.getId()).collect(Collectors.toList()));

        for (int i = 0; i < result.length; i++) {
            Object[] roleIdAndUserId = (Object[]) result[i];
            long roleId = (long) roleIdAndUserId[0];
            long userCount = (long) roleIdAndUserId[1];
            pagedRoles.forEach(r -> {
                if (r.getId() == roleId)
                    r.setUsersBelongingToThis(userCount);
            });
        }
        return pagedRoles;
    }

    public Role findById(Long id) {
        return this.rolesRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Role update(Role role) {
        return rolesRepository.save(role);
    }

    public Role getReference(Long id) {
        return this.rolesRepository.getOne(id);
    }

    public Role getOrCreate(String name, String description) {
        return null;
    }

    public Role findOrCreateByName(String name) {
        return findOrCreateByName(name, null);
    }

    public Role findOrCreateByName(String name, String description) {
        Role tag = rolesRepository.findByName(name).orElse(null);
        if (tag == null) {
            tag = rolesRepository.save(new Role(name, description));
        }
        return tag;
    }
}

