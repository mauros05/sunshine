import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { MatButtonModule } from "@angular/material/button";

import { Restaurant } from "../../../core/models/restaurant.model";
import { SalesReport } from "../../../core/models/report.model";
import { RestaurantsService } from "../../../core/services/restaurants.service";
import { ReportsServie } from "../../../core/services/reports.service";


@Component({
  selector: 'app-sales-report',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './sales-report.component.html',
  styleUrl: './sales-report.component.css',
})
export class SalesReportComponent implements OnInit {
  private restaurantsService = inject(RestaurantsService);
  private reportsService = inject(ReportsServie);

  restaurants: Restaurant[] = [];
  selectedRestaurantId = '';
  report: SalesReport | null = null;

  loading = false;
  error = '';

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.restaurantsService.getRestaurants().subscribe({
      next: (data) => {
        this.restaurants = data;
        if (data.length > 0) {
          this.selectedRestaurantId = data[0].id;
        }
      },
      error: () => {
        this.error = 'No se pudieron cargar los restaurantes';
      }
    })
  }

  loadReport(): void {
    if (!this.selectedRestaurantId) return;

    this.loading = true;
    this.error = '';
    this.report = null;

    this.reportsService.getSalesReport(this.selectedRestaurantId).subscribe({
      next: (data) => {
        this.report = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el reporte';
        this.loading = false;
      }
    });
  }
}
