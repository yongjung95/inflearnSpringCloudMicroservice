package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequque.KafkaProducer;
import com.example.orderservice.messagequque.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;

    private final OrderService orderService;

    private final KafkaProducer kafkaProducer;

    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody OrderDto orderDto) {
        log.info("Before add orders data");

        orderDto.setUserId(userId);

        /* jpa */
        OrderDto createdOrder = orderService.createOrder(orderDto);

        /* kafka */
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        /* Send an order to the kafka */
        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);

        log.info("After added orders data");

        return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(createdOrder, ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) throws Exception {
        log.info("Before retrieve orders data");

        List<OrderDto> resultOrderDto = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        resultOrderDto.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

//        try {
//            Thread.sleep(1000);
//            throw new Exception("장애 발생");
//        } catch (InterruptedException exception) {
//            log.error(exception.getMessage());
//        }

        log.info("Add retrieve orders data");

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
