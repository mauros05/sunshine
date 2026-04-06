import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { Order } from '../../../core/models/order.model';
import { OrdersService } from '../../../core/services/orders.service';
import { SessionService } from '../../../core/services/session.service';

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
  private sessionService = inject(SessionService);
  private ordersService = inject(OrdersService);
  private router = inject(Router);

  orders: Order[] = [];

  restaurantId = '';
  restaurantName = '';
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
    this.restaurantId = this.sessionService.getCurrentRestaurantId();
    this.restaurantName = this.sessionService.getCurrentRestaurantName();

    if (!this.restaurantId) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadOrders();
  }

  loadOrders(): void {
    if (!this.restaurantId) return;

    this.loading = true;
    this.error = '';

    const status = this.selectedStatus || undefined;

    this.ordersService.getOrders(this.restaurantId, status).subscribe({
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

  onStatusChange(status: string): void {
    this.selectedStatus = status;
    this.loadOrders();
  }

}
