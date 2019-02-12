package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "roles")
@Entity
public class Role extends TimestampedEntity {

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    @Transient
    private long usersBelongingToThis;


    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public long getUsersBelongingToThis() {
        return usersBelongingToThis;
    }

    public void setUsersBelongingToThis(long usersBelongingToThis) {
        this.usersBelongingToThis = usersBelongingToThis;
    }
}
