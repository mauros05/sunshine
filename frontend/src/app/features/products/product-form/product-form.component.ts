import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ProductsService } from "../../../core/services/products.service";
import { Product } from '../../../core/models/product.model';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css',
})
export class ProductFormComponent {
  private fb = inject(FormBuilder);
  private productsService = inject(ProductsService);

  @Input({ required: true }) restaurantId!: string;
  @Output() created = new EventEmitter<Product>();

  loading = false;
  error = '';

  form = this.fb.group({
    name: ['', [Validators.required]],
    price: [0, [Validators.required, Validators.min(0.01)]],
    category: ['', [Validators.required]]
  });

  submit(): void {
    if (!this.restaurantId) {
      this.error = 'Debes seleccionar un restaurante';
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    this.productsService.createProduct(this.restaurantId, {
      name: this.form.value.name!,
      price: Number(this.form.value.price),
      category: this.form.value.category!
    }).subscribe({
      next: (product) => {
        this.form.reset({ name: '', price: 0, category: '' });
        this.loading = false;
        this.created.emit(product);
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo crear producto';
      }
    });
  }
}
