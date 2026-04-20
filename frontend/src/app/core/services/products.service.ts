import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateProductRequest, Product } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/restaurants`;

  getProducts(restaurantId: string): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.baseUrl}/${restaurantId}/products`);
  }

  createProduct(restaurantId: string, payload: CreateProductRequest): Observable<Product> {
    return this.http.post<Product>(`${this.baseUrl}/${restaurantId}/products`, payload);
  }

  updateProduct(restaurantId: string, productId: string, payload: CreateProductRequest) {
    return this.http.put<Product>(
      `${this.baseUrl}/${restaurantId}/products/${productId}`,
      payload
    );
  }

  updateProductStatus(restaurantId: string, productId: string, active: boolean) {
    return this.http.patch<Product>(
      `${this.baseUrl}/${restaurantId}/products/${productId}/status`,
      { active }
    );
  }
}
