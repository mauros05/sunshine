import { Component, HostListener, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule, DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { Order, Ticket } from '../../../core/models/order.model';
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
export class OrderListComponent implements OnInit, OnDestroy {
  private sessionService = inject(SessionService);
  private ordersService = inject(OrdersService);
  private router = inject(Router);
  private document = inject(DOCUMENT);

  private previousBodyOverflow = '';
  private bodyScrollLocked = false;

  orders: Order[] = [];

  restaurantId = '';
  restaurantName = '';
  selectedStatus = '';
  selectedTicket: Ticket | null = null;

  page = 0;
  size = 10;
  totalPages = 0;
  totalItems = 0;
  hasNext = false;
  hasPrevious = false;

  loading = false;
  error = '';
  ticketError = '';

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

  ngOnDestroy(): void {
    this.unlockBodyScroll();
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

  viewTicket(order: Order): void {
    this.ticketError = '';
    this.selectedTicket = null;


    if (order.status !== 'PAID') {
      this.ticketError = 'Solo las ordenes pagadas tienen ticket.';
      return;
    }

    this.ordersService.getTicket(this.restaurantId, order.id).subscribe({
      next: (ticket) => {
        this.selectedTicket = ticket;
        this.lockBodyScroll();
      },
      error: () => {
        this.ticketError = 'No se pudo cargar el ticket';
      }
    });
  }

  closeTicket(): void {
    this.selectedTicket = null;
    this.ticketError = '';
    this.unlockBodyScroll();
  }

  printTicket(): void {
    if (!this.selectedTicket) {
      return;
    }

    const ticket = this.selectedTicket;

    const itemsHtml = ticket.items.map(item => `
      <div class="ticket-row">
        <span>${this.escapeHtml(item.productName)} x${item.quantity}</span>
        <span>${this.formatCurrency(item.subtotal)}</span>
      </div>
    `).join('');

    const html = `
      <!doctype html>
      <html lang="es">
      <head>
        <meta charset="utf-8" />
        <title>Ticket ${this.escapeHtml(ticket.orderId)}</title>
        <style>
          body { margin: 0; padding: 16px; background: #fff; color: #111; font-family: "Courier New", monospace; }
          .ticket { width: 280px; margin: 0 auto; border: 1px dashed #777; padding: 12px; }
          .center { text-align: center; }
          .row, .total { display: flex; justify-content: space-between; gap: 12px; margin: 6px 0; }
          .total { font-weight: 700; margin-top: 12px; }
          hr { border: 0; border-top: 1px dashed #999; margin: 10px 0; }
        </style>
      </head>
      <body>
        <section class="ticket">
          <h2 class="center">${this.escapeHtml(ticket.restaurantName)}</h2>
          <p><strong>Orden:</strong> ${this.escapeHtml(ticket.orderId)}</p>
          <p><strong>Fecha:</strong> ${this.escapeHtml(this.formatDate(ticket.createdAt))}</p>
          <hr />
          ${itemsHtml}
          <hr />
          <div class="total">
            <span>Total</span>
            <span>${this.formatCurrency(ticket.total)}</span>
          </div>
        </section>
      </body>
      </html>
    `;

    const printFrame = this.document.createElement('iframe');
    printFrame.style.position = 'fixed';
    printFrame.style.right = '0';
    printFrame.style.bottom = '0';
    printFrame.style.width = '0';
    printFrame.style.height = '0';
    printFrame.style.border = '0';
    printFrame.style.visibility = 'hidden';
    printFrame.setAttribute('aria-hidden', 'true');

    const cleanup = () => {
      if (printFrame.parentNode) {
        printFrame.parentNode.removeChild(printFrame);
      }
    };

    printFrame.onload = () => {
      const frameWindow = printFrame.contentWindow;
      if (!frameWindow) {
        cleanup();
        this.ticketError = 'No se pudo abrir la vista de impresión.';
        return;
      }

      frameWindow.onafterprint = cleanup;
      frameWindow.focus();
      frameWindow.print();

      // Fallback: algunos navegadores no disparan onafterprint.
      setTimeout(cleanup, 1500);
    };

    this.document.body.appendChild(printFrame);
    printFrame.srcdoc = html;
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('es-MX', {
      style: 'currency',
      currency: 'MXN'
    }).format(value);
  }

  private formatDate(value: string): string {
    return new Intl.DateTimeFormat('es-MX', {
      dateStyle: 'short',
      timeStyle: 'short'
    }).format(new Date(value));
  }

  private escapeHtml(value: string): string {
    return value
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }

  @HostListener('document:keydown.escape', ['$event'])
  onEscape(event: Event): void {
    if (!this.selectedTicket) {
      return;
    }

    event.preventDefault();
    this.closeTicket();
  }

  private lockBodyScroll(): void {
    if (this.bodyScrollLocked) {
      return;
    }

    this.previousBodyOverflow = this.document.body.style.overflow;
    this.document.body.style.overflow = 'hidden';
    this.bodyScrollLocked = true;
  }

  private unlockBodyScroll(): void {
    if (!this.bodyScrollLocked) {
      return;
    }

    this.document.body.style.overflow = this.previousBodyOverflow;
    this.bodyScrollLocked = false;
  }

}
