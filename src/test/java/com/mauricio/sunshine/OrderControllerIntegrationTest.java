package com.mauricio.sunshine;

import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.ProductEntity;
import com.mauricio.sunshine.persistence.entity.RestaurantEntity;
import com.mauricio.sunshine.persistence.repository.OrderItemRepository;
import com.mauricio.sunshine.persistence.repository.OrderRepository;
import com.mauricio.sunshine.persistence.repository.PaymentRepository;
import com.mauricio.sunshine.persistence.repository.ProductRepository;
import com.mauricio.sunshine.persistence.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void cleanDatabase() {
        paymentRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void createOrderWithItemsReturnsItemsAndTotal() throws Exception {
        RestaurantEntity restaurant = restaurantRepository.save(new RestaurantEntity("Restaurante Test", "Calle 1"));
        ProductEntity product = productRepository.save(
                new ProductEntity(restaurant, "Taco", new BigDecimal("45.50"), "Food")
        );

        String requestBody = """
                {
                  "items": [
                    {
                      "productId": "%s",
                      "quantity": 2
                    }
                  ]
                }
                """.formatted(product.getId());

        mockMvc.perform(post("/api/restaurants/{restaurantId}/orders", restaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.total").value(91.0))
                .andExpect(jsonPath("$.items[0].productId").value(product.getId().toString()))
                .andExpect(jsonPath("$.items[0].productName").value("Taco"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].subtotal").value(91.0));
    }

    @Test
    void addItemReturnsOrderWithLoadedProductData() throws Exception {
        RestaurantEntity restaurant = restaurantRepository.save(new RestaurantEntity("Restaurante Test", "Calle 1"));
        ProductEntity product = productRepository.save(
                new ProductEntity(restaurant, "Agua", new BigDecimal("20.00"), "Drink")
        );
        OrderEntity order = orderRepository.save(new OrderEntity(restaurant));

        String requestBody = """
                {
                  "productId": "%s",
                  "quantity": 3
                }
                """.formatted(product.getId());

        mockMvc.perform(post("/api/restaurants/{restaurantId}/orders/{orderId}/items", restaurant.getId(), order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId().toString()))
                .andExpect(jsonPath("$.total").value(60.0))
                .andExpect(jsonPath("$.items[0].productName").value("Agua"))
                .andExpect(jsonPath("$.items[0].quantity").value(3));
    }
}
