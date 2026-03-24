import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateRestaurantRequest, Restaurant } from '../models/restaurant.model';

@Injectable({
  providedIn: 'root',
})
export class RestaurantsService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/restaurants`;

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.baseUrl);
  }

  getRestaurant(id: string): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.baseUrl}/${id}`);
  }

  createRestaurant(payload: CreateRestaurantRequest): Observable<Restaurant> {
    return this.http.post<Restaurant>(this.baseUrl, payload);
  }
}
