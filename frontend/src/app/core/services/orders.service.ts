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


}
