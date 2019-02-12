package com.melardev.spring.shoppingcartweb.dtos.response.users.partials;

import com.melardev.spring.shoppingcartweb.dtos.response.shared.PageMeta;
import com.melardev.spring.shoppingcartweb.models.User;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDataSection {
    private PageMeta pageMeta;
    private List<UserIdAndUsernameDto> users;

    public UserDataSection(PageMeta pageMeta, List<UserIdAndUsernameDto> userDtos) {
        this.pageMeta = pageMeta;
        this.users = userDtos;
    }

    public static UserDataSection build(Page<User> usersPage, String basePath) {
        List<UserIdAndUsernameDto> userDtos = usersPage.getContent().stream()
                .map(UserIdAndUsernameDto::build)
                .collect(Collectors.toList());
        return new UserDataSection(
                PageMeta.build(usersPage, basePath),
                userDtos
        );
    }

    public Collection<UserIdAndUsernameDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserIdAndUsernameDto> users) {
        this.users = users;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public void setPageMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }

}
