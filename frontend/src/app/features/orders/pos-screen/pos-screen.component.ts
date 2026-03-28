import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { Restaurant } from '../../../core/models/restaurant.model';
import { Product } from '../../../core/models/product.model';
import { Order } from '../../../core/models/order.model';

import { RestaurantsService } from '../../../core/services/restaurants.service';
import { ProductsService } from '../../../core/services/products.service';
import { OrdersService } from '../../../core/services/orders.service';

@Component({
  selector: 'app-pos-screen',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './pos-screen.component.html',
  styleUrl: './pos-screen.component.css'
})
export class PosScreenComponent implements OnInit {
  private restaurantsService = inject(RestaurantsService);
  private productsService = inject(ProductsService);
  private ordersService = inject(OrdersService);

  restaurants: Restaurant[] = [];
  products: Product[] = [];

  selectedRestaurantId = '';
  currentOrder: Order | null = null;

  loading = false;
  error = '';
  payMethod: 'CASH' | 'CARD' | 'TRANSFER' = 'CASH';

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.restaurantsService.getRestaurants().subscribe({
      next: (data) => {
        this.restaurants = data;
        if (data.length > 0) {
          this.selectedRestaurantId = data[0].id;
          this.loadProducts();
        }
      },
      error: () => {
        this.error = 'No se pudieron cargar los restaurantes';
      }
    });
  }

  onRestaurantChange(restaurantId: string): void {
    this.selectedRestaurantId = restaurantId;
    this.currentOrder = null;
    this.loadProducts();
  }

  loadProducts(): void {
    if (!this.selectedRestaurantId) return;

    this.productsService.getProducts(this.selectedRestaurantId).subscribe({
      next: (data) => {
        this.products = data.filter(product => product.active);
      },
      error: () => {
        this.error = 'No se pudieron cargar los productos';
      }
    });
  }

  createOrder(): void {
    if (!this.selectedRestaurantId) return;

    this.ordersService.createOrder(this.selectedRestaurantId).subscribe({
      next: (order) => {
        this.currentOrder = order;
      },
      error: () => {
        this.error = 'No se pudo cargar la orden'
      }
    });
  }

  addProduct(product: Product): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;

    this.ordersService.addItem(this.selectedRestaurantId, this.currentOrder.id, {
      productId: product.id,
      quantity: 1
    }).subscribe({
      next: (order) => {
        this.currentOrder = order;
      },
      error: () => {
        this.error = 'No se pudo agregar el producto';
      }
    });
  }

  payOrder(): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;

    this.ordersService.payOrder(this.selectedRestaurantId, this.currentOrder.id, {
      method: this.payMethod,
      amount: this.currentOrder.total
    }).subscribe({
      next: () => {
        this.refreshCurrentOrder();
      },
      error: () => {
        this.error = 'No se pudo pagar la orden';
      }
    });
  }

  cancelOrder(): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;

    this.ordersService.cancelOrder(this.selectedRestaurantId, this.currentOrder.id).subscribe({
      next: (order) => {
        this.currentOrder = order;
      },
      error: () => {
        this.error = 'Error al cancelar la orden';
      }
    })
  }

  refreshCurrentOrder(): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;

    this.ordersService.getOrder(this.selectedRestaurantId, this.currentOrder.id).subscribe({
      next: (order) => {
        this.currentOrder = order;
      }
    });
  }

}
