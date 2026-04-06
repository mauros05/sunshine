import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";

import { SessionService } from "../../../core/services/session.service";
import { SalesReport } from "../../../core/models/report.model";
import { ReportsServie } from "../../../core/services/reports.service";


@Component({
  selector: 'app-sales-report',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './sales-report.component.html',
  styleUrl: './sales-report.component.css',
})
export class SalesReportComponent implements OnInit {
  private sessionService = inject(SessionService);
  private reportsService = inject(ReportsServie);
  private router = inject(Router);

  restaurantId = '';
  restaurantName = '';
  report: SalesReport | null = null;

  loading = false;
  error = '';

  ngOnInit(): void {
    this.restaurantId = this.sessionService.getCurrentRestaurantId();
    this.restaurantName = this.sessionService.getCurrentRestaurantName();

    if (!this.restaurantId) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadReport();
  }

  loadReport(): void {
    if (!this.restaurantId) return;

    this.loading = true;
    this.error = '';
    this.report = null;

    this.reportsService.getSalesReport(this.restaurantId).subscribe({
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
