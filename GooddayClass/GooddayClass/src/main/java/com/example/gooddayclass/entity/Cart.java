package com.example.gooddayclass.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public static Cart createCart(Member member){
        // 1명의 회원당 장바구니 한개
        Cart cart = new Cart();
        // 로그인한 유저 등록
        cart.setMember(member);
        return cart;
    }

}