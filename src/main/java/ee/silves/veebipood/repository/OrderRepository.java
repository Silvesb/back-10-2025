package ee.silves.veebipood.repository;

import ee.silves.veebipood.entity.Order;
import ee.silves.veebipood.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByPerson(Person dbPerson);
}
