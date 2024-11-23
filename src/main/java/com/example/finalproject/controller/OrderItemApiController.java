package com.example.finalproject.controller;


import com.example.finalproject.model.OrderItem;
import com.example.finalproject.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemApiController {

    private static final Logger log = LoggerFactory.getLogger(OrderItemApiController.class);

    private final OrderItemService orderItemService;

    // Constructor injection
    public OrderItemApiController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    @Operation(summary = "Get all order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderItem.class)))),
            @ApiResponse(responseCode = "404", description = "No order items found")
    })
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        return orderItems != null && !orderItems.isEmpty() ? ResponseEntity.ok(orderItems) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    public ResponseEntity<OrderItem> getOrderById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) {
        try {
            OrderItem createdOrderItem = orderItemService.createOrderItem(orderItem);
            return new ResponseEntity<>(createdOrderItem, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating order item: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Improve error handling here
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated"),
            @ApiResponse(responseCode = "404", description = "Order item not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItem updatedOrderItem) {
        try {
            boolean success = orderItemService.updateOrderItem(id, updatedOrderItem);
            return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating order item: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Improve error handling here
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted"),
            @ApiResponse(responseCode = "404", description = "Order item not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        try {
            orderItemService.deleteOrderItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting order item: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Improve error handling here
        }
    }
}
