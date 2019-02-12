package com.melardev.spring.shoppingcartweb.repository;


import com.melardev.spring.shoppingcartweb.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {

    @Query("Select o from Order o where o.user.id = :id")
    Page<Order> findOrdersByUserId(@Param("id") Long id, Pageable pageRequest);
}
