import { Component, EventEmitter, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RestaurantsService } from '../../../core/services/restaurants.service';

@Component({
  selector: 'app-restaurant-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './restaurant-form.component.html',
  styleUrl: './restaurant-form.component.css'
})
export class RestaurantFormComponent {
  private fb = inject(FormBuilder);
  private restaurantsService = inject(RestaurantsService);

  @Output() created = new EventEmitter<void>();

  loading = false;
  error = '';

  form = this.fb.group({
    name: ['', [Validators.required]],
    address: ['', [Validators.required]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    this.restaurantsService.createRestaurant({
      name: this.form.value.name!,
      address: this.form.value.address!
    }).subscribe({
      next: () => {
        this.form.reset();
        this.loading = false;
        this.created.emit();
      },
      error: () => {
       this.loading = false;
       this.error = 'No se pudo crear el restaurante';
      }
    });
  }
}
