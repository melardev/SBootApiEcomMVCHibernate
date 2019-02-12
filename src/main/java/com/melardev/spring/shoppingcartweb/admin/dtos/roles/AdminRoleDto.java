package com.melardev.spring.shoppingcartweb.admin.dtos.roles;

import com.melardev.spring.shoppingcartweb.models.Role;

import java.time.ZonedDateTime;

public class AdminRoleDto {

    private final long usersBelongingToThis;
    protected Long id;

    public AdminRoleDto(Long id, String name, String description, long usersBelongingToThis) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.usersBelongingToThis = usersBelongingToThis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;
    private String description;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static AdminRoleDto build(Role role) {
        return new AdminRoleDto(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getUsersBelongingToThis()
        );
    }

    public long getUsersBelongingToThis() {
        return usersBelongingToThis;
    }
}
