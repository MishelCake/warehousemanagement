package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.dto.ScheduleDeliveryDto;
import com.example.warehousemanagement.enums.OrderStatus;
import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.model.Delivery;
import com.example.warehousemanagement.model.Order;
import com.example.warehousemanagement.model.OrderItem;
import com.example.warehousemanagement.model.Truck;
import com.example.warehousemanagement.repository.DeliveryRepository;
import com.example.warehousemanagement.repository.OrderRepository;
import com.example.warehousemanagement.repository.TruckRepository;
import com.example.warehousemanagement.service.ScheduleDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleDeliveryServiceImpl implements ScheduleDeliveryService {
    private final OrderRepository orderRepository;
    private final TruckRepository truckRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    public void scheduleDelivery(ScheduleDeliveryDto scheduleDeliveryDto) {

        if(scheduleDeliveryDto.getDeliveryDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new WarehouseException("No deliveries are made on Sundays");
        }

        Order order = orderRepository.findById(scheduleDeliveryDto.getOrderId())
                .orElseThrow(() -> WarehouseException.notFoundById(Order.class.getSimpleName(), scheduleDeliveryDto.getOrderId()));

        if(!order.getOrderStatus().equals(OrderStatus.APPROVED)) {
            throw new WarehouseException("Order is not approved");
        }

        List<Truck> trucks = truckRepository.findAllById(scheduleDeliveryDto.getTruckIds());
        trucks.forEach(truck -> {
            if (truck.getDelivery() != null && truck.getDelivery().getDeliveryDate().equals(scheduleDeliveryDto.getDeliveryDate())) {
                throw new WarehouseException("Truck with license plate " + truck.getLicensePlate() + " is not available for this date");
            }
        });

        int totalQuantityItems = order.getOrderItems().stream().mapToInt(OrderItem::getOrderQuantity).sum();

        if(totalQuantityItems > trucks.size() * 10) {
            throw new WarehouseException("Select more trucks for this delivery");
        } else {
            Delivery delivery = new Delivery();
            order.setOrderStatus(OrderStatus.UNDER_DELIVERY);
            orderRepository.save(order);
            delivery.setOrder(order);
            delivery.setDeliveryDate(scheduleDeliveryDto.getDeliveryDate());
            String deliveryCode = "DLY-" + UUID.randomUUID();
            delivery.setDeliveryCode(deliveryCode);
            delivery.setTrucks(trucks);

            deliveryRepository.save(delivery);
        }
    }

    @Override
    public void fulfillOrder(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> WarehouseException.notFoundById(Delivery.class.getSimpleName(), deliveryId));

        Order order = delivery.getOrder();
        order.setOrderStatus(OrderStatus.FULFILLED);

        orderRepository.save(order);
    }
}
