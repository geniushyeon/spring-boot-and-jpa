package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 거울에는 연관관계의 거울임을 mappedBy로 표시해준다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
