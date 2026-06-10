package uz.pdp.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.dto.order_dto.OrderItemsRequestDto;
import uz.pdp.dto.order_dto.OrderRequestDto;
import uz.pdp.dto.order_dto.OrderResponseDto;
import uz.pdp.dto.order_dto.UserOrderUpdateDto;
import uz.pdp.entity.Order;
import uz.pdp.entity.OrderItems;
import uz.pdp.entity.Product;
import uz.pdp.enums.OrderStatus;
import uz.pdp.exception_handling.InSufficientStockException;
import uz.pdp.exception_handling.OrderNotFoundException;
import uz.pdp.exception_handling.ProductNotFoundException;
import uz.pdp.mapper.CustomMapper;
import uz.pdp.repository.OrderRepository;
import uz.pdp.repository.ProductRepository;
import uz.pdp.security_config.SessionUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomMapper customMapper;
    private final SessionUser sessionUser;

    public ResponseEntity<@NonNull List<OrderResponseDto>> findAll() {
        List<OrderResponseDto> list = customMapper.toListOrderDto(orderRepository.findAllOrders());
        if (list.isEmpty())
            throw new OrderNotFoundException("Order not found");

        return ResponseEntity.ok()
                .body(list);

    }

    public ResponseEntity<@NonNull List<OrderResponseDto>> findAllOrderBelongUsers() {
        String username = sessionUser.sessionUser();
        List<@NonNull Order> all = orderRepository.findAllByUsername(username);
        if (all.isEmpty())
            throw new OrderNotFoundException("Order not found");
        List<OrderResponseDto> listOrderDto = customMapper.toListOrderDto(all);
        return ResponseEntity.ok()
                .body(listOrderDto);

    }

    public ResponseEntity<@NonNull OrderResponseDto> findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderResponseDto dto = customMapper.toOrderDto(order);

        return ResponseEntity.ok()
                .body(dto);
    }

    @Transactional
    public ResponseEntity<@NonNull Long> create(OrderRequestDto dto) {

        List<OrderItems> orderItemsList = new ArrayList<>();
        Order order = new Order();

        CustomUserDetails userDetails = sessionUser.userDetails();

        order.setCustomerEmail(userDetails.getEmail());
        order.setCustomerName(userDetails.getUsername());
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemsRequestDto orderItemsDto : dto.getItemsDto()) {
            Product product = productRepository.findById(orderItemsDto.getProductId())
                    .orElseThrow(() -> new OrderNotFoundException("Product not found given id : %s".formatted(orderItemsDto.getProductId())));

            if (product.getStock() < orderItemsDto.getQuantity()) {
                throw new InSufficientStockException("Product stock not enough");
            }

            OrderItems orderItems = new OrderItems();
            product.setStock(product.getStock() - orderItemsDto.getQuantity());
            orderItems.setProduct(product);
            orderItems.setQuantity(orderItemsDto.getQuantity());
            orderItems.setUnitPrice(product.getPrice());
            orderItems.setOrder(order);

            BigDecimal totalPrice = orderItems.getUnitPrice()
                    .multiply(BigDecimal.valueOf(orderItemsDto.getQuantity()));

            orderItems.setTotalPrice(totalPrice);
            totalAmount = totalAmount.add(totalPrice);
            orderItemsList.add(orderItems);
        }
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItemsList);


        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @Transactional
    public void update(Long id, String status) {
        Order order = orderRepository
                .findById(id).orElseThrow(() -> new OrderNotFoundException("Item not found"));

        OrderStatus currentStatus = order.getOrderStatus();

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled Order cannot be changed");
        }

        boolean valid = (currentStatus == OrderStatus.PENDING &&
                (status.equals(OrderStatus.CONFIRMED.name()) || status.equals(OrderStatus.CANCELLED.name())))
                || (currentStatus == OrderStatus.CONFIRMED && status.equals(OrderStatus.SHIPPED.name()))
                || (currentStatus == OrderStatus.SHIPPED && status.equals(OrderStatus.DELIVERED.name()));
        if (!valid) {
            throw new IllegalStateException("Order Not Confirmed");
        }

        if (status.equals(OrderStatus.CANCELLED.name())) {
            for (OrderItems orderItem : order.getOrderItems()) {
                Integer stock = orderItem.getProduct().getStock();
                Integer quantity = orderItem.getQuantity();
                orderItem.getProduct().setStock(stock + quantity);
            }
        }

        order.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderRepository.deleteById(order.getId());
    }

    public List<OrderResponseDto> findOrdersOfCustomer(String email) {

        List<Order> orders = orderRepository
                .findByEmail(email);
        return customMapper.toListOrderDto(orders);
    }

    public ResponseEntity<@NonNull OrderResponseDto> findByIdBelongUser(Long orderId, CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        Order order = orderRepository
                .findByUserNameAndOrderId(username, orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return ResponseEntity
                .ok()
                .body(customMapper.toOrderDto(order));

    }

    @Transactional
    public void deleteBelongUser(Long id, CustomUserDetails userDetails) {
        Order order = orderRepository
                .findByUserNameAndOrderId(userDetails.getUsername(), id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Pending Order cannot be changed");
        }
        orderRepository.delete(order);
    }

    @Transactional
    public ResponseEntity<@NonNull String> userUpdate(UserOrderUpdateDto dto, CustomUserDetails userDetails) {

        Order order = orderRepository
                .findByUserNameAndOrderId(userDetails.getUsername(), dto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!Objects.equals(order.getOrderStatus().name(), OrderStatus.PENDING.name()))
            throw new OrderNotFoundException("Order Not Pending");

        Product product = productRepository
                .findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItems orderItem : order.getOrderItems()) {
            if (orderItem.getId().equals(dto.getOrderItemId())) {
                if (!(product.getStock() - (dto.getQuantity() - orderItem.getQuantity()) < 0))
                    product.setStock(product.getStock() - (dto.getQuantity() - orderItem.getQuantity()));
                else
                    throw new InSufficientStockException("Insufficient stock");
                orderItem.setQuantity(dto.getQuantity());
                orderItem.setProduct(product);
                orderItem.setUnitPrice(product.getPrice());
                BigDecimal totalPrice = orderItem
                        .getUnitPrice()
                        .multiply(BigDecimal.valueOf(dto.getQuantity()));
                orderItem.setTotalPrice(totalPrice);
            }
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
        return ResponseEntity.ok().body("Order successfully updated");
    }
}
