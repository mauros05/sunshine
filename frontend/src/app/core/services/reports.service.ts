import { Injectable, inject } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";
import {  } from "../models/report.model";
import { SalesReport } from '../../features/reports/sales-report/sales-report';


@Injectable({
  providedIn: 'root',
})
export class ReportsServie {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/restaurants`;

  getSalesReport(restaurantId: string): Observable<SalesReport> {
    return this.http.get<SalesReport>(`${this.baseUrl}/${restaurantId}/sales-report`);
  }

}
