package com.melardev.spring.shoppingcartweb.admin.controllers;

import com.melardev.spring.shoppingcartweb.admin.forms.RoleCreateEditDto;
import com.melardev.spring.shoppingcartweb.dtos.response.admin.RolesDtoResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.roles.RoleDetailsResponse;
import com.melardev.spring.shoppingcartweb.models.Role;
import com.melardev.spring.shoppingcartweb.services.RolesService;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("admin/roles")
public class RolesController {


    private final UsersService usersService;

    private RolesService rolesService;

    @Autowired
    public RolesController(UsersService usersService, RolesService rolesService) {
        this.usersService = usersService;
        this.rolesService = rolesService;
    }

    @GetMapping
    public RolesDtoResponse index(HttpServletRequest request,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "page_size", defaultValue = "30") int pageSize) {
        Page<Role> rolesPage = this.rolesService.findAll(page, pageSize);
        return RolesDtoResponse.build(rolesPage, request.getRequestURI());
    }

    @GetMapping("by_name/{name}")
    public RoleDetailsResponse show(@PathVariable("name") String roleName) {
        Role role = this.rolesService.get(roleName, true);
        return RoleDetailsResponse.build(role);
    }

    @GetMapping("by_id/{id}")
    public RoleDetailsResponse show(@PathVariable("id") Long id) {
        Role role = this.rolesService.findById(id);
        return RoleDetailsResponse.build(role);
    }

    @PostMapping
    public RoleDetailsResponse create(@Valid @RequestBody RoleCreateEditDto roleCreateEditDto) {
        Role role = new Role();
        role.setName(roleCreateEditDto.getName());
        role.setDescription(roleCreateEditDto.getDescription());
        role = rolesService.save(role);
        return RoleDetailsResponse.build(role);
    }

    @PutMapping("{id}")
    public RoleDetailsResponse update(@PathVariable("id") Long id, @Valid @RequestBody RoleCreateEditDto roleCreateEditDto) {
        Role role = this.rolesService.getReference(id);
        role.setName(roleCreateEditDto.getName());
        role.setDescription(roleCreateEditDto.getDescription());
        role = rolesService.update(role);
        return RoleDetailsResponse.build(role);
    }
}
