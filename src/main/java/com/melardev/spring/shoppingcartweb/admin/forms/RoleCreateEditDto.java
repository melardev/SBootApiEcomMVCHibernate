package com.melardev.spring.shoppingcartweb.admin.forms;

import javax.validation.constraints.NotEmpty;

public class RoleCreateEditDto {

    @NotEmpty
    private String name;

    private String description;

    private Long id;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
