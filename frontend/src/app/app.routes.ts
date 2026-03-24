import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list/restaurant-list.component';
import { ProductListComponent } from './features/products/product-list/product-list.component';
import { OrderList } from './features/orders/order-list/order-list';
import { SalesReport } from './features/reports/sales-report/sales-report';

export const routes: Routes = [
  { path: '', redirectTo: 'restaurants', pathMatch: 'full' },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'orders', component: OrderList },
  { path: 'reports', component: SalesReport },
  { path: '**', redirectTo: 'restaurants' },
];
