package com.example.ch5test.entity;


import com.example.ch5test.constant.ItemSellStatus;
import com.example.ch5test.repository.ItemRepository;
import com.example.ch5test.repository.MemberRepository;
import com.example.ch5test.repository.OrderItemRepository;
import com.example.ch5test.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());

        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0;i<3;i++){
            // 테스트 상품 3개 만들기
            Item item = this.createItem();
            // 중간 테이블에 저장
            itemRepository.save(item);
            // 주문이 '된' 상품
            OrderItem orderItem = new OrderItem();
            // 주문이 된 상품위에 더미 상품 추가
            // !!!중요!!!
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            // !!!중요!!!
            // 주문이 된 상품을 넣어주는 비지니스 로직
            order.getOrderItems().add(orderItem);
        }

        // 주문 엔티티 클래스의 중간테이블에 저장 및 실제 테이블에 저장
        orderRepository.saveAndFlush(order);
        // 중간 테이블 비움
        // 조회시 연관 관계 테이블이 조인이 되면서 같이 참조 되는 부분 확인
        em.clear();

        //조회 시 중간 테이블에 내용이 비워져 실제 테이블에서 내용을 가져올때 참조하는 테이블을 확인
        // 현재 lazy 지연 로딩
        // eager 즉시 로딩 -> 즉시 로딩일 경우 연관 없는 부분도 로딩되어 성능상 이슈발생
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder(){
        // 주문에 담겨진 요소 : 1) 주문 상품을 요소로 하는 리스트, 2)회원
        // 주문 -> 상품 추가
        // -> 주문 상품 추가
        // -> 주문 상품 아이템들을 요소로 가지는 리스트에 추가
        // -> 회원추가
        // -> 주문, 회원 추가 (주문자)
        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        // 주문
        Order order = this.createOrder();
        //주문 클래스 안에 필드로 주문 아이템 리스트 멤버가 존재
        Long orderItemId = order.getOrderItems().get(0).getId();
        // 실제 DB에 반영
        em.flush();
        // 중간 저장소를 비우기 전에 orderItem 주문 상품의 번호를 기록
        em.clear();
        // 주문 상품 조회 시 실제 디비에서 찾기를 할 때
        // 연관 있는 것만(Lazy) 조회 , 연관 없는것(eager) 조회할지??
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("===========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("===========================");
    }

}