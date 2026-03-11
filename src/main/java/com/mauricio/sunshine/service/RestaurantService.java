package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.RestaurantEntity;
import com.mauricio.sunshine.persistence.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepo;

    public RestaurantService(RestaurantRepository restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }

    public RestaurantEntity createRestaurant(String name, String address){
        RestaurantEntity restaurant = new RestaurantEntity(name, address);
        return restaurantRepo.save(restaurant);
    }

    public RestaurantEntity getRestaurant(UUID id){
        return restaurantRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Restaurant not found"));
    }
}
