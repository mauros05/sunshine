import { Injectable, inject } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";
import { AddOrderItemRequest, Order, PayOrderRequest } from "../models/order.model";

@Injectable({
  providedIn: 'root',
})
export class OrdersService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/restaurants`;

  createOrder(restaurantId: string): Observable<Order> {
    return this.http.post<Order>(`${this.baseUrl}/${restaurantId}/orders`, {});
  }

  getOrder(restaurantId: string, orderId: string): Observable<Order> {
    return this.http.get<Order>(`${this.baseUrl}/${restaurantId}/orders/${orderId}`);
  }

  getOrders(restaurantId: string, status?: string): Observable<Order[]> {
    let url = `${this.baseUrl}/${restaurantId}/orders`;

    if (status){
      url += `?status=${status}`;
    }

    return this.http.get<Order[]>(url);
  }

  addItem(restaurantId: string, orderId: string, payload: AddOrderItemRequest): Observable <Order> {
    return this.http.post<Order>(
      `${this.baseUrl}/${restaurantId}/orders/${orderId}/items`,
      payload
    );
  }

  payOrder(restaurantId: string, orderId: string, payload: PayOrderRequest): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/${restaurantId}/orders/${orderId}/pay`,
      payload
    );
  }

  cancelOrder(restaurantId: string, orderId: string): Observable<Order> {
    return this.http.patch<Order>(
      `${this.baseUrl}/${restaurantId}/orders/${orderId}/cancel`,
      {}
    );
  }
}
