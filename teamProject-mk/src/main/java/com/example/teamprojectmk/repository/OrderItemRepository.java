package com.example.teamprojectmk.repository;


import com.example.teamprojectmk.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}