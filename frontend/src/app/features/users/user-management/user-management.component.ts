import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { SessionService } from '../../../core/services/session.service';
import { UsersService } from '../../../core/services/users.service';
import { RestaurantMembership, User } from '../../../core/models/user.model';
import { NewLineKind } from 'typescript';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
})
export class UserManagementComponent implements OnInit {
  private sessionService = inject(SessionService);
  private usersService = inject(UsersService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  restaurantId = '';
  restaurantName = '';

  users: User[] = [];
  members: RestaurantMembership[] = [];

  loadingUsers = false;
  loadingMembers = false;
  error = '';
  successMessage = '';

  roleOptions: Array<'OWNER' | 'MANAGER' | 'CASHIER'> = ['OWNER', 'MANAGER', 'CASHIER'];

  editingMembershipId: string | null = null;
  selectedRole: 'OWNER' | 'MANAGER' | 'CASHIER' = 'CASHIER';

  createUserForm = this.fb.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]]
  });

  addMemberForm = this.fb.group({
    userId: ['', [Validators.required]],
    role: ['CASHIER' as 'OWNER' | 'MANAGER' | 'CASHIER', [Validators.required]]
  });

  ngOnInit(): void {
    if (!this.sessionService.isOwner()) {
      this.router.navigate(['/pos']);
      return;
    }

    this.restaurantId = this.sessionService.getCurrentRestaurantId();
    this.restaurantName = this.sessionService.getCurrentRestaurantName();

    if (!this.restaurantId) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadUsers();
    this.loadMembers();
  }

  loadUsers(): void {
    this.loadingUsers = true;
    this.usersService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.loadingUsers = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los usuarios';
        this.loadingUsers = false;
      }
    });
  }

  loadMembers(): void {
    this.loadingMembers = true;
    this.usersService.getRestaurantMembers(this.restaurantId).subscribe({
      next: (data) => {
        this.members = data;
        this.loadingMembers = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los miembros';
        this.loadingMembers = false;
      }
    });
  }

  createUser(): void {
    if (this.createUserForm.invalid) {
      this.createUserForm.markAllAsTouched();
      return;
    }

    this.error = '';
    this.successMessage = '';

    this.usersService.createUser({
      fullName: this.createUserForm.value.fullName!,
      email: this.createUserForm.value.email!
    }).subscribe({
      next: () => {
        this.successMessage = "Usuario creado correctamente";
        this.createUserForm.reset();
        this.loadUsers();
      },
      error: () => {
        this.error = 'No se pudo crear el usuario'
      }
    })
  }

  addUserToRestaurant(): void {
    if (this.addMemberForm.invalid) {
      this.addMemberForm.markAllAsTouched();
      return;
    }

    this.error = '';
    this.successMessage = '';

    this.usersService.addUserToRestaurant(this.restaurantId, {
      userId: this.addMemberForm.value.userId!,
      role: this.addMemberForm.value.role!
    }).subscribe({
      next: () => {
        this.successMessage = 'Usuario agregado al restaurante correctamente.';
        this.addMemberForm.patchValue({ userId: '', role: 'CASHIER'});
        this.loadMembers();
      },
      error: () => {
        this.error = 'No se pudo agregar el usuario al restaurante';
      }
    });
  }

  startEditRole(member: RestaurantMembership): void {
    this.editingMembershipId = member.id;
    this.selectedRole = member.role;
    this.error = '';
    this.successMessage = '';
  }

  cancelEditRole(): void {
    this.editingMembershipId = null;
    this.selectedRole = 'CASHIER';
  }

  saveRole(memberId: string): void {
    this.error = '';
    this.successMessage = '';

    this.usersService.updateMembershipRole(this.restaurantId, memberId, this.selectedRole).subscribe({
      next: () => {
        this.successMessage = 'Rol actualizado correctamente';
        this.editingMembershipId = null;
        this.loadMembers()
      },
      error: () => {
        this.error = 'No se pudo actualizar el rol';
      }
    });
  }

  removeMember(memberId: string): void {
    this.error = ''
    this.successMessage = ''

    this.usersService.removeMembership(this.restaurantId, memberId).subscribe({
      next: () => {
        this.successMessage = 'Miembro eliminado correctamente';
        this.loadMembers();
      },
      error: () => {
        this.error = 'No se pudo eliminar al miembro';
      }
    });
  }

}
