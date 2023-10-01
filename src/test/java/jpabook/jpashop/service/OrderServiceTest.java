package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() {
        // given
        Member member = createMember("member1", "서울", "강가", "12345");
        Item book = createItem("시골 JPA", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(getOrder.getStatus(), OrderStatus.ORDER);
        assertEquals(1, getOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }
    @Test
    void 상품주문_재고수량초과() {
        // given
        Member member = createMember("member1", "서울", "강가", "12345");
        Item book = createItem("시골 JPA", 10000, 10);

        int orderCount = 11;

        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });
    }

    @Test
    void 주문취소() {
        // given
        Member member = createMember("member1", "서울", "강가", "12345");
        Item book = createItem("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancel(orderId);

        // test
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(getOrder.getStatus(), OrderStatus.CANCEL);
        assertEquals(book.getStockQuantity(), 10);

    }

    private Item createItem(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        em.persist(member);

        return member;
    }
}