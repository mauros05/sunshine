import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { Restaurant } from '../../../core/models/restaurant.model';
import { Order } from '../../../core/models/order.model';
import { RestaurantsService } from '../../../core/services/restaurants.service';
import { OrdersService } from '../../../core/services/orders.service';


@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css',
})
export class OrderListComponent implements OnInit {
  private restaurantsService = inject(RestaurantsService);
  private ordersService = inject(OrdersService);

  restaurants: Restaurant[] = [];
  orders: Order[] = [];

  selectedRestaurantId = '';
  selectedStatus = '';

  loading = false;
  error = ''

  statusOptions = [
    { value: '', label: 'Todas' },
    { value: 'OPEN', label: 'Abiertas' },
    { value: 'PAID', label: 'Pagadas' },
    { value: 'CANCELLED', label: 'Canceladas' }
  ];

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.restaurantsService.getRestaurants().subscribe({
      next: (data) => {
        this.restaurants = data;
        if (data.length > 0) {
          this.selectedRestaurantId = data[0].id;
          this.loadOrders;
        }
      },
      error: () => {
        this.error = 'No se pudieron cargar los restaurantes';
      }
    });
  }

  loadOrders(): void {
    if (!this.selectedRestaurantId) return;

    this.loading = true;
    this.error = '';

    const status = this.selectedStatus || undefined;

    this.ordersService.getOrders(this.selectedRestaurantId, status).subscribe({
      next: (data) => {
        this.orders = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las ordenes';
        this.loading = false;
      }
    });
  }

  onRestaurantChange(restaurantId: string): void {
    this.selectedRestaurantId = restaurantId;
    this.loadOrders();
  }

  onStatusChange(status: string): void {
    this.selectedStatus = status;
    this.loadOrders();
  }

}
