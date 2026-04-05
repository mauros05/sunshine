import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';

import { SessionService } from '../../core/services/session.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatButtonModule
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent implements OnInit {
  private sessionService = inject(SessionService);
  private router = inject(Router);

  ngOnInit(): void {
    if(!this.sessionService.isLoggedIn()) {
      this.router.navigate(['/login']);
    }
  }

  get userName(): string {
    return this.sessionService.getCurrentUserName();
  }

  get restaurantName(): string {
    return this.sessionService.getCurrentRestaurantName();
  }

  get role(): string {
    return this.sessionService.getCurrentRole();
  }

  logout(): void {
    this.sessionService.clearSession();
    this.router.navigate(['/login']);
  }

}
