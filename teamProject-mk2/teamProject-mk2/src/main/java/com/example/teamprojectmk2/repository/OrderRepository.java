package com.example.teamprojectmk2.repository;

import com.example.teamprojectmk2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}