package com.mauricio.sunshine.persistance.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public RestaurantEntity() {}

    public RestaurantEntity(String name, String address) {
        this.name = name;
        this.address = address;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddress(String address){
        this.address = address;
    }
}
