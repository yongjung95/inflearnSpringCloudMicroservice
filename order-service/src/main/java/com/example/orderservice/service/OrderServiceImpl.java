package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.repository.OrderEntity;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);

        orderRepository.save(orderEntity);

        return mapper.map(orderEntity, OrderDto.class);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        return new ModelMapper().map(orderRepository.findByOrderId(orderId), OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        List<OrderEntity> orderList = orderRepository.findByUserId(userId);

        List<OrderDto> result = new ArrayList<>();

        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, OrderDto.class));
        });

        return result;
    }
}
