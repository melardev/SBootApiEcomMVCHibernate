package com.melardev.spring.shoppingcartweb.services;

import com.melardev.spring.shoppingcartweb.models.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    @Query("select oi.order.id, count(*), sum(oi.price * oi.quantity) from OrderItem oi where oi.order.id in :orderIds group by oi.order.id")
    List<Object[]> getOrderItemsCountForOrderIds(@Param("orderIds") List<Long> orderIds);
}
