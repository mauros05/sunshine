package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.CreateRestaurantRequest;
import com.mauricio.sunshine.api.dto.RestaurantResponse;
import com.mauricio.sunshine.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public RestaurantResponse createRestaurant(@Valid @RequestBody CreateRestaurantRequest req) {

        var restaurant = restaurantService.createRestaurant(req.name(), req.address());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCreatedAt()
        );
    }


    @GetMapping("/{id}")
    public RestaurantResponse getRestaurant(@PathVariable UUID id) {

        var restaurant = restaurantService.getRestaurant(id);

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCreatedAt()
        );
    }

}
