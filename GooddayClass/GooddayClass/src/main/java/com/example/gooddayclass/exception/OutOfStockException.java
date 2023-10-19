package com.example.gooddayclass.exception;

public class OutOfStockException extends RuntimeException{

    // 상품 주문시, 재고 수량보다 많은 주문을 할 경우 오류발생
    // 기본적 유효성 체크
    public OutOfStockException(String message) {
        super(message);
    }

}