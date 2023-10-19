package com.example.teamprojectmk.repository;


import com.example.teamprojectmk.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}