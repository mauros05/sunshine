import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { Product } from "../../../core/models/product.model";
import { ProductsService } from "../../../core/services/products.service";
import { ProductFormComponent } from '../product-form/product-form.component';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    ProductFormComponent
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})

export class ProductListComponent implements OnInit {
  private productsService = inject(ProductsService);
  private sessionService = inject(SessionService);
  private router = inject(Router);

  products: Product[] = [];

  restaurantId = '';
  restaurantName = '';

  loadingProducts = false;
  error = '';
  successMessage = '';

  editingProductId: string | null = null;
  editModel = {
    name: '',
    price: 0,
    category: ''
  };

  ngOnInit(): void {
    if (!this.sessionService.canViewProducts()) {
      this.router.navigate(['/pos']);
      return;
    }

    this.restaurantId = this.sessionService.getCurrentRestaurantId();
    this.restaurantName = this.sessionService.getCurrentRestaurantName();

    if (!this.restaurantId) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadProducts();
  }

  loadProducts(): void {
    if(!this.restaurantId) {
      this.products = [];
      return;
    }

    this.loadingProducts = true;
    this.error = ''
    this.successMessage = '';

    this.productsService.getProducts(this.restaurantId).subscribe({
      next: (data) => {
        this.products = data;
        this.loadingProducts = false;
      },
      error: () => {
        this.loadingProducts = false;
        this.error = 'No se pudieron cargar productos'
      }
    });
  }

  startEdit(product: Product): void {
    this.editingProductId = product.id;
    this.editModel = {
      name: product.name,
      price: product.price,
      category: product.category
    };
    this.error = '';
    this.successMessage = '';
  }

  cancelEdit(): void {
    this.editingProductId = null;
    this.editModel = {
      name: '',
      price: 0,
      category: ''
    };
  }

  onProductCreated(product: Product): void {
    if (product.restaurantId === this.restaurantId) {
      // optimistic update so the UI reflects creation immediately
      this.products = [product, ...this.products];
    }
    // keep the list consistent with server-side calculations/filters
    this.loadProducts();
  }
}
