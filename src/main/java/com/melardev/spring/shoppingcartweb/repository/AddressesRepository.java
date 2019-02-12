package com.melardev.spring.shoppingcartweb.repository;

import com.melardev.spring.shoppingcartweb.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressesRepository extends JpaRepository<Address, Long> {

    @Query("from Address a where a.user.id=:userId")
    List<Address> findAllFromUser(@Param("userId") Long userId);

}
