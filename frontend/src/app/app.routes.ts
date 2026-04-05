import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RestaurantListComponent } from './features/restaurants/restaurant-list/restaurant-list.component';
import { ProductListComponent } from './features/products/product-list/product-list.component';
import { PosScreenComponent } from './features/orders/pos-screen/pos-screen.component';
import { OrderListComponent } from './features/orders/order-list/order-list.component';
import { SalesReportComponent } from './features/reports/sales-report/sales-report.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent},
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'restaurants', pathMatch: 'full'},
      { path: 'restaurants', component: RestaurantListComponent },
      { path: 'products', component: ProductListComponent },
      { path: 'pos', component: PosScreenComponent },
      { path: 'orders', component: OrderListComponent },
      { path: 'reports', component: SalesReportComponent }
    ]
  }
];
