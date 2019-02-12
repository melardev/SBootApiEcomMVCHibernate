package com.melardev.spring.shoppingcartweb.dtos.response.roles;

import com.melardev.spring.shoppingcartweb.models.Role;

import java.time.ZonedDateTime;

public class RoleDetailsResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final long usersBelongingToThis;
    private final ZonedDateTime updatedAt;
    private final ZonedDateTime createdAt;

    public RoleDetailsResponse(Long id, String name, String description, ZonedDateTime createdAt, ZonedDateTime updatedAt, long usersBelongingToThis) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.usersBelongingToThis = usersBelongingToThis;

    }

    public static RoleDetailsResponse build(Role role) {
        return new RoleDetailsResponse(role.getId(), role.getName(), role.getDescription(), role.getCreatedAt(), role.getUpdatedAt(), role.getUsersBelongingToThis());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getUsersBelongingToThis() {
        return usersBelongingToThis;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
