import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list/restaurant-list.component';
import { ProductListComponent } from './features/products/product-list/product-list.component';
import { OrderListComponent } from './features/orders/order-list/order-list.component';
import { SalesReportComponent } from './features/reports/sales-report/sales-report.component';

export const routes: Routes = [
  { path: '', redirectTo: 'restaurants', pathMatch: 'full' },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'orders', component: OrderListComponent },
  { path: 'reports', component: SalesReportComponent }
  ];
