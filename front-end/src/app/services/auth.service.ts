import { Injectable, signal } from '@angular/core';
import { ApiService } from './api.service';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private tokenKey = 'auth_token';
  private expirationTokenKey = 'auth_token_expiration';

  isAuthenticated = signal(false);
  username = signal<string | null>(null);
  role = signal<string | null>(null);
  favoriteBooks = signal<any[]>([]);

  constructor(private apiService: ApiService) {
    this.checkAuth();
  }

  login(username: string, password: string) {
    return this.apiService.login({ username, password }).pipe(
      tap((response) => {
        const { token, username, role, favoriteBooks, expirationDate } =
          response;
        localStorage.setItem(this.tokenKey, token);
        localStorage.setItem(this.expirationTokenKey, expirationDate);
        this.isAuthenticated.set(true);
        this.username.set(username);
        this.role.set(role);
        this.favoriteBooks.set(favoriteBooks);
      })
    );
  }

  register(username: string, password: string) {
    return this.apiService.register({ username, password });
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.expirationTokenKey);

    this.isAuthenticated.set(false);
    this.username.set(null);
    this.role.set(null);
    this.favoriteBooks.set([]);
  }

  private checkAuth() {
    const token = localStorage.getItem(this.tokenKey);
    const expiration = localStorage.getItem(this.expirationTokenKey);

    if (token && expiration) {
      const now = new Date().getTime();
      const expirationTime = new Date(expiration).getTime();

      if (now < expirationTime) {
        this.isAuthenticated.set(true);
        this.fetchUserData(token);
      } else {
        this.logout();
      }
    }
  }

  fetchUserData(token: string) {
    this.apiService.getUserData(token).subscribe({
      next: (data) => {
        this.username.set(data['Username']);
        this.role.set(data['role']);
        this.favoriteBooks.set(data['favoriteBooks']);
      },
      error: (err) => {
        this.logout();
      },
    });
  }
}
