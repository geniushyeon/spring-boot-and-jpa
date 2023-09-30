package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter // 변경 불가능하게 설계
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 스펙상 엔티티나 Embeddable 타입은 public 혹은 protected 타입의 기본 생성자 필요
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
