package com.mauricio.sunshine.api;

import com.mauricio.sunshine.persistence.entity.RestaurantEntity;
import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import com.mauricio.sunshine.persistence.entity.UserEntity;
import com.mauricio.sunshine.persistence.entity.UserRole;
import com.mauricio.sunshine.persistence.repository.RestaurantMembershipRepository;
import com.mauricio.sunshine.persistence.repository.RestaurantRepository;
import com.mauricio.sunshine.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PosFlowITTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantMembershipRepository membershipRepository;

    @Test
    void fullOrderPaymentFlow_shouldWork() throws Exception {

        // 1) Crear restaurante
        String restaurantResponse = mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Super Tortas Tampico",
                                  "address": "Coatzacoalcos"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Super Tortas Tampico"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String restaurantId = JsonTestUtils.readJsonField(restaurantResponse, "id");
        String ownerUserId = createOwnerMembership(restaurantId);

        // 2) Crear producto
        String productResponse = mockMvc.perform(post("/api/restaurants/" + restaurantId + "/products")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Torta de Pierna",
                                  "price": 95.00,
                                  "category": "Tortas"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Torta de Pierna"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String productId = JsonTestUtils.readJsonField(productResponse, "id");

        // 3) Crear orden
        String orderResponse = mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.total").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String orderId = JsonTestUtils.readJsonField(orderResponse, "id");

        // 4) Agregar item
        mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders/" + orderId + "/items")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": "%s",
                                  "quantity": 2
                                }
                                """.formatted(productId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.total").value(190.00))
                .andExpect(jsonPath("$.items[0].productName").value("Torta de Pierna"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].subtotal").value(190.00));

        // 5) Pagar orden
        mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders/" + orderId + "/pay")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "method": "CASH",
                                  "amount": 190.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.method").value("CASH"))
                .andExpect(jsonPath("$.amount").value(190.00));

        // 6) Verificar que la orden ya no está OPEN sino PAID
        mockMvc.perform(get("/api/restaurants/" + restaurantId + "/orders/" + orderId)
                        .header("X-User-Id", ownerUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.total").value(190.00));
    }

    @Test
    void cancelOpenOrder_shouldWork() throws Exception {

        String restaurantResponse = mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Restaurante Cancel Test",
                                  "address": "CDMX"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String restaurantId = JsonTestUtils.readJsonField(restaurantResponse, "id");
        String ownerUserId = createOwnerMembership(restaurantId);

        String orderResponse = mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String orderId = JsonTestUtils.readJsonField(orderResponse, "id");

        mockMvc.perform(patch("/api/restaurants/" + restaurantId + "/orders/" + orderId + "/cancel")
                        .header("X-User-Id", ownerUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancelPaidOrder_shouldFail() throws Exception {

        String restaurantResponse = mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Restaurante Paid Test",
                                  "address": "CDMX"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String restaurantId = JsonTestUtils.readJsonField(restaurantResponse, "id");
        String ownerUserId = createOwnerMembership(restaurantId);

        String productResponse = mockMvc.perform(post("/api/restaurants/" + restaurantId + "/products")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Refresco",
                                  "price": 30.00,
                                  "category": "Bebidas"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String productId = JsonTestUtils.readJsonField(productResponse, "id");

        String orderResponse = mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String orderId = JsonTestUtils.readJsonField(orderResponse, "id");

        mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders/" + orderId + "/items")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": "%s",
                                  "quantity": 1
                                }
                                """.formatted(productId)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/restaurants/" + restaurantId + "/orders/" + orderId + "/pay")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "method": "CARD",
                                  "amount": 30.00
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/restaurants/" + restaurantId + "/orders/" + orderId + "/cancel")
                        .header("X-User-Id", ownerUserId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateMembershipRole_shouldReturnMembershipWithUserAndRestaurantData() throws Exception {
        String restaurantResponse = mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Restaurante Roles",
                                  "address": "Monterrey"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String restaurantId = JsonTestUtils.readJsonField(restaurantResponse, "id");
        String ownerUserId = createOwnerMembership(restaurantId);
        String uniqueEmail = "rol-update-" + System.nanoTime() + "@test.com";

        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Juan Perez",
                                  "email": "%s"
                                }
                                """.formatted(uniqueEmail)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String userId = JsonTestUtils.readJsonField(userResponse, "id");

        String membershipResponse = mockMvc.perform(post("/api/restaurants/" + restaurantId + "/members")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "role": "CASHIER"
                                }
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String membershipId = JsonTestUtils.readJsonField(membershipResponse, "id");

        mockMvc.perform(patch("/api/restaurants/" + restaurantId + "/members/" + membershipId + "/role")
                        .header("X-User-Id", ownerUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "role": "MANAGER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(membershipId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.userName").value("Juan Perez"))
                .andExpect(jsonPath("$.userEmail").value(uniqueEmail))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId))
                .andExpect(jsonPath("$.restaurantName").value("Restaurante Roles"))
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    private String createOwnerMembership(String restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(UUID.fromString(restaurantId))
                .orElseThrow();

        UserEntity owner = userRepository.save(
                new UserEntity("Owner Test", "owner-" + UUID.randomUUID() + "@test.com")
        );

        membershipRepository.save(new RestaurantMembershipEntity(owner, restaurant, UserRole.OWNER));
        return owner.getId().toString();
    }
}
