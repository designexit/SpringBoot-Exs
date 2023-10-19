package com.example.gooddayclass.entity;

import com.example.gooddayclass.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    // 현재 주문 엔티티 클래스 멤버로 주문이 된 상품들 리스트를 가지고 있음
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문_상품을 추가하는 매서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 주문 만드는 과정 : 1. 구매자 2. 주문이 된 상품
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        // 각 각의 주문
        Order order = new Order();
        // 1.구매자
        order.setMember(member);

        // 2.주문이 된 상품을 반복문으로 추가
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        // 3.주문의 상태 표현 : 주문중, 배송중, 결제완료
        order.setOrderStatus(OrderStatus.ORDER);
        // 4.주문 일자
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}