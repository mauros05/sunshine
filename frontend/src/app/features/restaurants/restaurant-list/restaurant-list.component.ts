import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { RestaurantsService } from '../../../core/services/restaurants.service';
import { Restaurant } from '../../../core/models/restaurant.model';
import { RestaurantFormComponent } from '../restaurant-form/restaurant-form.component';
import { Router } from '@angular/router';
import { SessionService } from '../../../core/services/session.service';


@Component({
  selector: 'app-restaurant-list',
  imports: [CommonModule, MatCardModule, RestaurantFormComponent],
  templateUrl: './restaurant-list.component.html',
  styleUrl: './restaurant-list.component.css',
})
export class RestaurantListComponent implements OnInit {
  private restaurantsService = inject(RestaurantsService);
  private sessionService = inject(SessionService);
  private router = inject(Router);

  restaurants: Restaurant[] = [];
  loading = false;
  error = '';

  ngOnInit(): void {
    if (!this.sessionService.canViewRestaurants()) {
      this.router.navigate(['/pos']);
      return;
    }

    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.loading = true;
    this.error = '';

    this.restaurantsService.getRestaurants().subscribe({
      next: (data) => {
        this.restaurants = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los restaurantes';
        this.loading = false;
      }
    });
  }
}
