package com.melardev.spring.shoppingcartweb.dtos.response.admin;

import com.melardev.spring.shoppingcartweb.admin.dtos.roles.AdminRoleDto;
import com.melardev.spring.shoppingcartweb.dtos.response.shared.PageMeta;
import com.melardev.spring.shoppingcartweb.models.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class RolesDtoResponse {

    private final PageMeta pageMeta;
    private final List<AdminRoleDto> roles;


    public RolesDtoResponse(PageMeta pageMeta, List<AdminRoleDto> roleDtos) {
        this.pageMeta = pageMeta;
        this.roles=roleDtos;

    }

    public static RolesDtoResponse build(Page<Role> rolesPage, String basePath) {
        List<AdminRoleDto> rolesDtoResponse = rolesPage.getContent().stream()
                .map(AdminRoleDto::build)
                .collect(Collectors.toList());

        return new RolesDtoResponse(
                PageMeta.build(rolesPage, basePath),
                rolesDtoResponse
        );
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public List<AdminRoleDto> getRoles() {
        return roles;
    }
}
