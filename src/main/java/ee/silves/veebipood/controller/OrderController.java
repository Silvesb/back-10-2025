package ee.silves.veebipood.controller;

import ee.silves.veebipood.entity.Order;
import ee.silves.veebipood.entity.Person;
import ee.silves.veebipood.entity.Product;
import ee.silves.veebipood.model.everypay.EveryPayLink;
import ee.silves.veebipood.repository.OrderRepository;
import ee.silves.veebipood.repository.PersonRepository;
import ee.silves.veebipood.repository.ProductRepository;
import ee.silves.veebipood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    OrderService orderService;

    @GetMapping("orders")
    public List<Order> getOrders(){
        Long id = Long.parseLong(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString()
        );
        Person dbPerson = personRepository.findById(id).orElseThrow();

        return orderRepository.findAllByPerson(dbPerson);
    }

    @PostMapping("orders")
    public EveryPayLink createOrder(@RequestBody List<Product> products, @RequestParam String parcelMachine) {
        return orderService.saveOrder(products, parcelMachine);
    }
}
