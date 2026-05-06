import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import { MatCardModule } from "@angular/material/card";
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from "@angular/material/button";

import { SessionService } from "../../../core/services/session.service";
import { SalesReport, SalesSummary } from "../../../core/models/report.model";
import { ReportsServie } from "../../../core/services/reports.service";


@Component({
  selector: 'app-sales-report',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
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

  summary: SalesSummary | null = null;

  from = '';
  to = '';

  loading = false;
  error = '';

  ngOnInit(): void {
    if (!this.sessionService.canViewReports()) {
      this.router.navigate(['/pos']);
      return;
    }

    this.restaurantId = this.sessionService.getCurrentRestaurantId();
    this.restaurantName = this.sessionService.getCurrentRestaurantName();

    if (!this.restaurantId) {
      this.router.navigate(['/login']);
      return;
    }

    const today = new Date().toISOString().slice(0, 10);
    this.from = today;
    this.to = today;

    this.loadReport();
    this.loadSummary();
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

  loadSummary(): void {
    if (!this.restaurantId || !this.from || !this.to) return;

    this.loading = true;
    this.error = '';
    this.summary = null;

    this.reportsService.getSalesSummary(this.restaurantId, this.from, this.to).subscribe({
      next: (data) => {
        this.summary = data;
        this.loading = false
      },
      error: () => {
        this.error = 'No se pudo cargar el resumen de ventas';
        this.loading = false;
      }
    });
  }
}
