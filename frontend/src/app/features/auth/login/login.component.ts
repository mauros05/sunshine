import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { AuthService } from '../../../core/services/auth.service';
import { SessionService } from '../../../core/services/session.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private sessionService = inject(SessionService);
  private router = inject(Router);

  loading = false;
  error = '';

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    const email = this.form.value.email!;

    this.authService.login(email).subscribe({
      next: (response) => {
        this.sessionService.setSession(response);
        const memberships = response.memberships ?? [];

        if (memberships.length === 0) {
          this.error = 'El usuario no pertenece a ningun restaurante';
          this.loading = false;
          return;
        }

        if (memberships.length === 1) {
          this.sessionService.setCurrentRestaurant(memberships[0]);
          this.loading = false;
          this.router.navigate(['/pos']);
          return;
        }

        this.sessionService.setCurrentRestaurant(memberships[0]);
        this.loading = false;
        this.router.navigate(['/pos']);
      },
      error: () => {
        this.error = 'No se pudo iniciar sesion';
        this.loading = false;
      }
    });
  }


}
