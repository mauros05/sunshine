import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';

import { Product } from '../../../core/models/product.model';
import { Order } from '../../../core/models/order.model';

import { ProductsService } from '../../../core/services/products.service';
import { OrdersService } from '../../../core/services/orders.service';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-pos-screen',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule
  ],
  templateUrl: './pos-screen.component.html',
  styleUrl: './pos-screen.component.css'
})
export class PosScreenComponent implements OnInit {
  private productsService = inject(ProductsService);
  private ordersService = inject(OrdersService);
  private sessionService = inject(SessionService);
  private router = inject(Router);

  products: Product[] = [];

  selectedRestaurantId = '';
  restaurantName = '';
  currentOrder: Order | null = null;

  loading = false;
  error = '';
  successMessage = '';

  payMethod: 'CASH' | 'CARD' | 'TRANSFER' = 'CASH';

  quantities: Record<string, number> = {};

  ngOnInit(): void {
    this.selectedRestaurantId = this.sessionService.getCurrentRestaurantId();
    this.restaurantName = this.sessionService.getCurrentRestaurantName();

    if(!this.selectedRestaurantId) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadProducts();
    this.loadOpenOrderIfExist();
  }

  loadProducts(): void {
    if (!this.selectedRestaurantId) return;

    this.productsService.getProducts(this.selectedRestaurantId).subscribe({
      next: (data) => {
        this.products = data.filter(product => product.active);

        this.quantities = {};
        this.products.forEach(product => {
          this.quantities[product.id] = 1;
        })
      },
      error: () => {
        this.error = 'No se pudieron cargar los productos';
      }
    });
  }

  loadOpenOrderIfExist(): void {
    if (!this.selectedRestaurantId) return;

    this.ordersService.getOrders(this.selectedRestaurantId, 'OPEN', 0, 1).subscribe({
      next: (page) => {
        if (page.items.length > 0) {
          this.currentOrder = page.items[0];
        } else {
          this.currentOrder = null;
        }
      },
      error: () => {
        this.currentOrder = null;
      }
    });
  }

  createOrder(): void {
    if (!this.selectedRestaurantId) return;

    this.error = '';
    this.successMessage = '';

    if (this.currentOrder && this.currentOrder.status == 'OPEN') {
      this.error = 'Ya existe una orden abierta para este restaurante.';
      return;
    }

    this.ordersService.createOrder(this.selectedRestaurantId).subscribe({
      next: (order) => {
        this.currentOrder = order;
        this.successMessage = 'Orden creada correctamente';
      },
      error: () => {
        this.error = 'No se pudo crear la orden';
      }
    });
  }

  addProduct(product: Product): void {
    if (!this.selectedRestaurantId || !this.currentOrder) {
      this.error = 'Primero debes crear una orden.';
      return;
    }

    const quantity = this.quantities[product.id] || 1;

    if (quantity < 1) {
      this.error = 'La cantidad debe ser al menos 1.';
      return;
    }

    this.error = '';
    this.successMessage = '';

    this.ordersService.addItem(this.selectedRestaurantId, this.currentOrder.id, {
      productId: product.id,
      quantity
    }).subscribe({
      next: (order) => {
        this.currentOrder = order;
        this.successMessage = `${product.name} agregado correctamente.`;
        this.quantities[product.id] = 1;
        this.refreshCurrentOrder();
      },
      error: () => {
        this.error = 'No se pudo agregar el producto';
      }
    });
  }

  payOrder(): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;
    if (this.currentOrder.total <= 0) {
      this.error = 'No puedes pagar una orden con total 0.';
      return;
    }

    this.error = '';
    this.successMessage = '';

    this.ordersService.payOrder(this.selectedRestaurantId, this.currentOrder.id, {
      method: this.payMethod,
      amount: this.currentOrder.total
    }).subscribe({
      next: () => {
        this.successMessage = 'Orden pagada correctamente.';
        this.currentOrder = null;
        this.loadOpenOrderIfExist();
      },
      error: () => {
        this.error = 'No se pudo pagar la orden';
      }
    });
  }

  cancelOrder(): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;

    this.error = '';
    this.successMessage = '';

    this.ordersService.cancelOrder(this.selectedRestaurantId, this.currentOrder.id).subscribe({
      next: () => {
        this.successMessage = 'Orden cancelada correctamente.';
        this.currentOrder = null;
        this.loadOpenOrderIfExist();
      },
      error: () => {
        this.error = 'Error al cancelar la orden';
      }
    });
  }

  clearMessages(): void {
    this.error = '';
    this.successMessage = '';
  }

  private refreshCurrentOrder(): void {
    if (!this.selectedRestaurantId || !this.currentOrder) return;

    this.ordersService.getOrder(this.selectedRestaurantId, this.currentOrder.id).subscribe({
      next: (order) => {
        this.currentOrder = order;
      },
      error: () => {
      }
    });
  }

}
