import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';


import { Product } from "../../../core/models/product.model";
import { Restaurant } from '../../../core/models/restaurant.model';
import { ProductsService } from "../../../core/services/products.service";
import { RestaurantsService } from '../../../core/services/restaurants.service';
import { ProductFormComponent } from '../product-form/product-form.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    ProductFormComponent
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})

export class ProductListComponent implements OnInit {
  private productsService = inject(ProductsService);
  private restaurantsService = inject(RestaurantsService);

  restaurants: Restaurant[] = [];
  products: Product[] = [];

  selectedRestaurantId = '';

  loadingRestaurants = false;
  loadingProducts = false;
  error = '';

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.loadingRestaurants = true;
    this.error = '';

    this.restaurantsService.getRestaurants().subscribe({
      next: (data) => {
        this.restaurants = data;
        this.loadingRestaurants = false;

        if (data.length > 0) {
          this.selectedRestaurantId = data[0].id;
          this.loadProducts();
        }
      },
      error: () => {
        this.loadingRestaurants = false;
        this.error = 'No se pudieron cargar los restaurantes';
      }
    });
  }

  loadProducts(): void {
    if(!this.selectedRestaurantId) {
      this.products = [];
      return;
    }

    this.loadingProducts = true;
    this.error = ''

    this.productsService.getProducts(this.selectedRestaurantId).subscribe({
      next: (data) => {
        this.products = data;
        this.loadingProducts = false;
      },
      error: () => {
        this.loadingProducts = false;
        this.error = 'No se pudieron cargar productos'
      }
    });
  }

  onRestaurantChange(restaurantId: string): void {
    this.selectedRestaurantId = restaurantId;
    this.loadProducts();
  }
}
