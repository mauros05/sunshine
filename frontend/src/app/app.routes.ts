import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list/restaurant-list.component';
import { ProductList } from './features/products/product-list/product-list';
import { OrderList } from './features/orders/order-list/order-list';
import { SalesReport } from './features/reports/sales-report/sales-report';

export const routes: Routes = [
  { path: '', redirectTo: 'restaurants', pathMatch: 'full' },
  { path: 'restaurans', redirectTo: 'restaurants', pathMatch: 'full' },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'products', component: ProductList },
  { path: 'orders', component: OrderList },
  { path: 'reports', component: SalesReport },
  { path: '**', redirectTo: 'restaurants' },
];
