package com.melardev.spring.shoppingcartweb.repository;


import com.melardev.spring.shoppingcartweb.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    // @Query("select r.id from Role r inner join r.users u where r.id in :roleIds group by some")
    @Query("select r.id, count(*) from com.melardev.spring.shoppingcartweb.models.User u inner join u.roles r where r.id in :roleIds group by r.id")
    Object[][] findUsersCountForRoleIds(@Param("roleIds") List<Long> roleIds);
}
