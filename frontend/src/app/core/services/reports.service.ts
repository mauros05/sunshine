import { Injectable, inject } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";
import { SalesReport } from "../models/report.model";


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
