import { Injectable } from "@angular/core";
import { LoginMembership, LoginResponse } from "../models/auth.model";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private readonly USER_KEY = 'sunshine_user';
  private readonly RESTAURANT_KEY = 'sunshine_current_restaurant';

  setSession(LoginResponse: LoginResponse): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(LoginResponse));
  }

  getSession(): LoginResponse | null {
    const raw = localStorage.getItem(this.USER_KEY);
    return raw ? JSON.parse(raw) : null;
  }

  clearSession(): void {
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.RESTAURANT_KEY);
  }

  setCurrentRestaurant(membership: LoginMembership): void {
    localStorage.setItem(this.RESTAURANT_KEY, JSON.stringify(membership));
  }

  getCurrentRestaurant(): LoginMembership | null {
    const raw = localStorage.getItem(this.RESTAURANT_KEY);
    return raw ? JSON.parse(raw) : null;
  }

  isLoggedIn(): boolean {
    return !!this.getSession();
  }

  getCurrentUserName(): string {
    return this.getSession()?.fullName ?? '';
  }

  getCurrentRole(): string {
    return this.getCurrentRestaurant()?.role ?? '';
  }

  getCurrentRestaurantId(): string {
    return this.getCurrentRestaurant()?.restaurantId ?? '';
  }

  getCurrentRestaurantName(): string {
    return this.getCurrentRestaurant()?.restaurantName ?? '';
  }
}
