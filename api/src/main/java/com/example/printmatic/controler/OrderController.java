package com.example.printmatic.controler;

import com.example.printmatic.dto.request.OrderCreationDTO;
import com.example.printmatic.dto.response.MessageResponseDTO;
import com.example.printmatic.dto.response.OrderDTO;
import com.example.printmatic.dto.response.UserOrderDTO;
import com.example.printmatic.enums.OrderStatus;
import com.example.printmatic.enums.SortBy;
import com.example.printmatic.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> createOrder(@Valid @RequestBody OrderCreationDTO orderCreationDTO,
                                                           BindingResult bindingResult , Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(400, bindingResult.getAllErrors().getFirst().getDefaultMessage()));
        }

        MessageResponseDTO result = orderService.createOrder(orderCreationDTO, principal);
        return ResponseEntity.status(result.status()).body(result);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) SortBy sortBy,
            Pageable pageable
    ){
        return ResponseEntity.ok(orderService.getOrders(status, sortBy, pageable));
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserOrderDTO>> getUserOrders(
            Principal principal,
            Pageable pageable
    ){
        return ResponseEntity.ok(orderService.getOrdersOfUser(principal, pageable));
    }

    @PostMapping("/updateOrderStatus/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    public ResponseEntity<MessageResponseDTO> setOrderStatus(
            @PathVariable Long id,
            @RequestParam(required = true) OrderStatus orderStatus
    ) {
        MessageResponseDTO result = orderService.updateOrderStatus(id, orderStatus);
        return ResponseEntity.status(result.status()).body(result);
    }

}