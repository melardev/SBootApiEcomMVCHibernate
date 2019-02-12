package com.melardev.spring.shoppingcartweb.admin.dtos.users;

import com.melardev.spring.shoppingcartweb.admin.dtos.users.partials.AdminUserSummary;
import com.melardev.spring.shoppingcartweb.dtos.response.shared.PageMeta;
import com.melardev.spring.shoppingcartweb.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class AdminUsersListResponse {
    private final List<AdminUserSummary> users;
    private PageMeta pageMeta;

    public AdminUsersListResponse(PageMeta pageMeta, List<AdminUserSummary> userDtos) {
        this.pageMeta = pageMeta;
        this.users = userDtos;
    }

    public static AdminUsersListResponse build(Page<User> usersPage, String basePath) {
        List<AdminUserSummary> userDtos = usersPage.getContent().stream()
                .map(AdminUserSummary::build)
                .collect(Collectors.toList());

        return new AdminUsersListResponse(
                PageMeta.build(usersPage, basePath),
                userDtos
        );
    }

    public List<AdminUserSummary> getUsers() {
        return users;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }
}
