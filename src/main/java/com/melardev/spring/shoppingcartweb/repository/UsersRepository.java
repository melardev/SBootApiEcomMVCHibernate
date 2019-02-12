package com.melardev.spring.shoppingcartweb.repository;

import com.melardev.spring.shoppingcartweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.roles r where u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void changePassword(@Param("id") Long id, @Param("password") String password);

    @Query(value = "SELECT * FROM users order by rand() limit 1", nativeQuery = true)
    User findRandom();

}
