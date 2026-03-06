package com.microservice.order.controller;

import com.microservice.order.db.OrderRepository;
import com.microservice.order.dto.OrderDTO;
import com.microservice.order.dto.ProductDTO;
import com.microservice.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class SlashOrder {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClient;

    @GetMapping
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<OrderDTO>> createOrder(@RequestBody Order order) {
        //fetct Product Details from product service
        return webClient.build().get().uri("http://localhost:8081/products/" + order.getProductId()).retrieve()
                .bodyToMono(ProductDTO.class).map(productDTO -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setProductId(productDTO.getProductId());
                    orderDTO.setQuantity(order.getQuantity());
                    orderDTO.setProductName(productDTO.getProductName());
                    orderDTO.setTotalPrice(orderDTO.getQuantity() * productDTO.getPrice());
                    orderRepository.save(order);
                    orderDTO.setOrderId(order.getOrderId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
                });
    }

}
