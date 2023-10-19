package com.example.teamprojectmk3.repository;


import com.example.teamprojectmk3.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}