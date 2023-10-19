package com.example.teamprojectmk2.repository;

import com.example.teamprojectmk2.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}