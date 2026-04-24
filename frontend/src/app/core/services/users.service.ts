import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AddUserToRestaurantRequest,
  CreateUserRequest,
  RestaurantMembership,
  User
} from '../models/user.model'

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}`;

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users`);
  }

  createUser(payload: CreateUserRequest): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/users`, payload);
  }

  getRestaurantMembers(restaurantId: string): Observable<RestaurantMembership[]> {
    return this.http.get<RestaurantMembership[]>(`${this.baseUrl}/restaurants/${restaurantId}/members`);
  }

  addUserToRestaurant(
    restaurantId: string,
    payload: AddUserToRestaurantRequest
  ): Observable<RestaurantMembership> {
    return this.http.post<RestaurantMembership>(
      `${this.baseUrl}/restaurants/${restaurantId}/members`,
      payload
    );
  }

  updateMembershipRole(
    restaurantId: string,
    membershipId: string,
    role: 'OWNER' | 'MANAGER' | 'CASHIER'
  ) {
    return this.http.patch<RestaurantMembership>(
      `${this.baseUrl}/restaurants/${restaurantId}/members/${membershipId}/role`,
      { role }
    );
  }

  removeMembership(
    restaurantId: string,
    membershipId: string
  ) {
    return this.http.delete(
      `${this.baseUrl}/restaurants/${restaurantId}/members/${membershipId}`
    );
  }

}
