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

  page = 0;
  size = 10;
  totalPages = 0;
  totalItems = 0;
  hasNext = false;
  hasPrevious = false;

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

    this.ordersService.getOrders(this.restaurantId, status, this.page, this.size).subscribe({
      next: (data) => {
        this.orders = data.items;
        this.page = data.page;
        this.size = data.size;
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.hasNext = data.hasNext;
        this.hasPrevious = data.hasPrevious
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
    this.page = 0;
    this.loadOrders();
  }

  nextPage(): void {
    if (!this.hasNext) return;
    this.page += 1;
    this.loadOrders();
  }

  previousPage(): void {
    if (!this.hasPrevious) return;
    this.page -= 1;
    this.loadOrders();
  }

}
