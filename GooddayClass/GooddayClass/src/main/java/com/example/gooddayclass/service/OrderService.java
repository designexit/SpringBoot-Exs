package com.example.gooddayclass.service;

import com.example.ch7_8_test.dto.OrderDto;
import com.example.ch7_8_test.dto.OrderHistDto;
import com.example.ch7_8_test.dto.OrderItemDto;
import com.example.ch7_8_test.repository.ItemImgRepository;
import com.example.ch7_8_test.repository.ItemRepository;
import com.example.ch7_8_test.repository.MemberRepository;
import com.example.ch7_8_test.repository.OrderRepository;
import com.example.gooddayclass.entity.ItemImg;
import com.example.gooddayclass.entity.Order;
import com.example.gooddayclass.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        // 상품 아이디를 이용해 디비에 조회
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 구매자 조회 (로그인 유저)
        Member member = memberRepository.findByEmail(email);

        // 주문 된 상품 리스트
        List<OrderItem> orderItemList = new ArrayList<>();
        // 상품 엔티티 클래스 영속화 : 1. 상품 번호, 2. 상품 수량
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        // 주문된 상품 리스트 추가
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member, orderItemList);
        // 중간 테이블에 저장(영속화) -> 실제 테이블에 반영할 때 트랜젝션에 커밋이 되는 시점
        orderRepository.save(order);

        // 주문 번호 반환
        return order.getId();
    }

    // 주문 이력 조회
    // 트랜젝션에서 추가, 수정 변경이 없어서 조회만.. 성능상 부분 고려
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            // 주문을 꺼내서 dto로 다시 담기
            OrderHistDto orderHistDto = new OrderHistDto(order);
            // 주문 엔티티 클래스 -> 주문된 상품들의 목록
            List<OrderItem> orderItems = order.getOrderItems();
            // 주문 상품들 중에서 하나씩 꺼내서 대표 이미지 가지고 옴
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    // 주문 유효성 체크 : 주문 아이디, 구매자, 로그인한 유저 맞는지...
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        // 현재 유저와 주문 유저가 같다면 -> true
        // 다르면 -> false
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email){

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

}