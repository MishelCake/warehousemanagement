package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.dto.OrderBasicInfoDto;
import com.example.warehousemanagement.dto.OrderDto;
import com.example.warehousemanagement.dto.OrderResponseDto;
import com.example.warehousemanagement.dto.OrderItemDto;
import com.example.warehousemanagement.dto.UpdateOrderDto;
import com.example.warehousemanagement.enums.OrderStatus;
import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.mapper.OrderMapper;
import com.example.warehousemanagement.model.Item;
import com.example.warehousemanagement.model.Order;
import com.example.warehousemanagement.model.OrderItem;
import com.example.warehousemanagement.model.User;
import com.example.warehousemanagement.repository.ItemRepository;
import com.example.warehousemanagement.repository.OrderItemRepository;
import com.example.warehousemanagement.repository.OrderRepository;
import com.example.warehousemanagement.repository.UserRepository;
import com.example.warehousemanagement.service.EmailService;
import com.example.warehousemanagement.service.OrderService;
import com.example.warehousemanagement.util.Constants;
import com.example.warehousemanagement.util.LoggedInUserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper mapper;
    private final OrderMapper orderMapper;
    private final LoggedInUserInfo loggedInUserInfo;
    private final EmailService emailService;

    @Override
    public OrderResponseDto createOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId()).orElse(null);

        if (user == null) {
            throw new WarehouseException(Constants.USER_NOT_FOUND);
        }
        checkAccess(user.getEmail());

        String orderNumber = "ORD-" + UUID.randomUUID();

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUser(user);
        order.setSubmittedDate(LocalDateTime.now());
        if (orderDto.getDeadline().isBefore(LocalDateTime.now()) || orderDto.getDeadline().isEqual(LocalDateTime.now())) {
            throw new WarehouseException(Constants.WRONG_DEADLINE);
        }
        order.setDeadlineDate(orderDto.getDeadline());
        order.setOrderStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();

        //add order items to the order
        for (OrderItemDto orderItemDto : orderDto.getOrderItemDto()) {
            Item item = itemRepository.findById(orderItemDto.getItemId()).orElse(null);
            if (item == null) {
                throw new WarehouseException(Constants.ITEM_WITH_ID.concat(orderItemDto.getItemId().toString()).concat(Constants.NOT_FOUND));
            }

            if (item.getQuantity() < orderItemDto.getQuantity()) {
                throw new WarehouseException(Constants.INSUFFICIENT_QUANTITY_AVAILABLE.concat(item.getItemName()));
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setOrderQuantity(orderItemDto.getQuantity());

            orderItems.add(orderItem);

            // Update item quantity in inventory
            item.setQuantity(item.getQuantity() - orderItemDto.getQuantity());
            itemRepository.save(item);
        }
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto addItemsToOrder(UpdateOrderDto updateOrderDto) {
        Order order = orderRepository.findById(updateOrderDto.getOrderId()).orElse(null);
        if (order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }

        checkAccess(order.getUser().getEmail());

        if (!order.getOrderStatus().equals(OrderStatus.CREATED) && !order.getOrderStatus().equals(OrderStatus.DECLINED)) {
            throw new WarehouseException(Constants.ORDER_CANNOT_BE_UPDATED);
        }

        List<OrderItemDto> newItems = updateOrderDto.getNewItems();
        List<OrderItem> newOrderItems = new ArrayList<>();

        for(OrderItemDto newItem : newItems) {
            Item item = itemRepository.findById(newItem.getItemId())
                    .orElseThrow(() -> new WarehouseException(Constants.ITEM_WITH_ID + newItem.getItemId() + Constants.NOT_FOUND));

            //check if this item already exists in OrderItem table
            if(orderItemRepository.existsByOrder_OrderIdAndItem_ItemId(order.getOrderId(), item.getItemId())) {
                throw new WarehouseException(Constants.ITEM_WITH_ID + item.getItemId() + " already exists. " +
                        "Please update the quantity!");
            }

            //check of item quantity is sufficient
            if(item.getQuantity() < newItem.getQuantity()) {
                throw new WarehouseException(Constants.INSUFFICIENT_QUANTITY_AVAILABLE + item.getItemName());
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setOrderQuantity(newItem.getQuantity());

            newOrderItems.add(orderItem);

            order.getOrderItems().add(orderItem);

            // Update item quantity in inventory
            item.setQuantity(item.getQuantity() - newItem.getQuantity());
            itemRepository.save(item);
        }
        orderRepository.save(order);
        orderItemRepository.saveAll(newOrderItems);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderResponseDto removeItemsFromOrder(Long orderId, List<Long> itemsToRemoveIds) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }

        checkAccess(order.getUser().getEmail());

        if (!order.getOrderStatus().equals(OrderStatus.CREATED) && !order.getOrderStatus().equals(OrderStatus.DECLINED)) {
            throw new WarehouseException(Constants.ORDER_CANNOT_BE_UPDATED);
        }

        List<OrderItem> orderItemsToRemove = new ArrayList<>();
        for(Long itemId : itemsToRemoveIds) {
            OrderItem orderItem = orderItemRepository.findOrderItemByOrder_OrderIdAndItem_ItemId(order.getOrderId(), itemId)
                    .orElseThrow(() -> new WarehouseException(Constants.ITEM_WITH_ID + itemId + " is not in this order "));

            //remove item from order
            order.getOrderItems().remove(orderItem);
            orderItemsToRemove.add(orderItem);

            // Update item quantity in inventory
            Item item = orderItem.getItem();
            item.setQuantity(item.getQuantity() + orderItem.getOrderQuantity());
            itemRepository.save(item);
        }
        orderRepository.save(order);
        orderItemRepository.deleteAll(orderItemsToRemove);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto modifyQuantityOfOrderItems(Long orderId, List<OrderItemDto> modifiedItems) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }

        checkAccess(order.getUser().getEmail());

        if (!order.getOrderStatus().equals(OrderStatus.CREATED) && !order.getOrderStatus().equals(OrderStatus.DECLINED)) {
            throw new WarehouseException(Constants.ORDER_CANNOT_BE_UPDATED);
        }

        for(OrderItemDto orderItemDto : modifiedItems) {
            OrderItem orderItemToModify = orderItemRepository.findOrderItemByOrder_OrderIdAndItem_ItemId(order.getOrderId(), orderItemDto.getItemId())
                    .orElseThrow(() -> new WarehouseException(Constants.ITEM_WITH_ID + orderItemDto.getItemId() + " is not in this order "));

            Item item = orderItemToModify.getItem();

            Integer newQuantity = orderItemDto.getQuantity();
            Integer previousOrderQuantity = orderItemToModify.getOrderQuantity();
            Integer difference = newQuantity - previousOrderQuantity;

            if (difference > 0) {
                if (item.getQuantity() >= difference) {
                    item.setQuantity(item.getQuantity() - difference);
                    orderItemToModify.setOrderQuantity(newQuantity);
                } else {
                    throw new WarehouseException(Constants.INSUFFICIENT_QUANTITY_IN_INVENTORY);
                }
            } else if (difference < 0) {
                item.setQuantity(item.getQuantity() - difference);
                orderItemToModify.setOrderQuantity(newQuantity);
            }
            orderItemRepository.save(orderItemToModify);
            itemRepository.save(item);
        }
        return orderMapper.toDto(order);
    }


    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }

        if (!loggedInUserInfo.getLoggedInUserEmail().equals(order.getUser().getEmail())) {
            throw new WarehouseException(Constants.NOT_AUTHORIZED_TO_CANCEL_ORDER);
        }

        if (order.getOrderStatus().equals(OrderStatus.CANCELED) || order.getOrderStatus().equals(OrderStatus.FULFILLED)
                || order.getOrderStatus().equals(OrderStatus.UNDER_DELIVERY)) {
            throw new WarehouseException(Constants.THIS_ORDER_IS + order.getOrderStatus());
        }

        order.getOrderItems().forEach(orderItem -> {
            Item item = orderItem.getItem();
            item.setQuantity(item.getQuantity() + orderItem.getOrderQuantity());
            itemRepository.save(item);
        });
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    public void submitOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }

        if (!loggedInUserInfo.getLoggedInUserEmail().equals(order.getUser().getEmail())) {
            throw new WarehouseException(Constants.NOT_AUTHORIZED_TO_CANCEL_ORDER);
        }

        if (order.getOrderStatus().equals(OrderStatus.CREATED) || order.getOrderStatus().equals(OrderStatus.DECLINED)) {
            order.setOrderStatus(OrderStatus.AWAITING_APPROVAL);
            orderRepository.save(order);
        } else {
            throw new WarehouseException("Can not submit. Order status: " + order.getOrderStatus());
        }
    }

    @Override
    public Page<OrderResponseDto> getAllOrdersOfLoggedInUser(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Order> pageResult;

        if (orderStatus != null) {
            pageResult = orderRepository.findAllByUserEmailAndOrderStatus(loggedInUserInfo.getLoggedInUserEmail(), orderStatus, pageable);
        } else {
            pageResult = orderRepository.findAllByUserEmail(loggedInUserInfo.getLoggedInUserEmail(), pageable);
        }
        return pageResult.map(orderMapper :: toDto);
    }

    @Override
    public Page<OrderBasicInfoDto> getAllOrders(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Order> pageResult;

        if (orderStatus != null) {
            pageResult = orderRepository.findAllByOrderStatus(orderStatus, pageable);
        } else {
            pageResult = orderRepository.findAll(pageable);
        }
        return pageResult.map(order -> mapper.map(order, OrderBasicInfoDto.class));
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }
        return orderMapper.toDto(order);
    }

    @Override
    public void approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }
        if (order.getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL)) {
            order.setOrderStatus(OrderStatus.APPROVED);
            orderRepository.save(order);
        } else {
            throw new WarehouseException(Constants.THIS_ORDER_IS + order.getOrderStatus().name());
        }
    }

    @Override
    public void declineOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            throw new WarehouseException(Constants.ORDER_NOT_FOUND);
        }
        if (order.getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL)) {
            order.setOrderStatus(OrderStatus.DECLINED);

            emailService.sendMessageWhenOrderDeclined(order.getUser().getEmail(), order.getUser().getName(), order.getOrderNumber(), reason);

            orderRepository.save(order);
        } else {
            throw new WarehouseException(Constants.THIS_ORDER_IS + order.getOrderStatus().name());
        }
    }

    private void checkAccess(String email) {
        String loggedEmail = loggedInUserInfo.getLoggedInUserEmail();
        if (!loggedEmail.equals(email))
            throw new WarehouseException("No access");
    }

}
