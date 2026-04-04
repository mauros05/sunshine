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
  templateUrl: './order-list-component.html',
  styleUrl: './order-list-component.css',
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
  ]


}
