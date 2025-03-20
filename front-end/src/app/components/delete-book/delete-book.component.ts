import { Component, Input, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-delete-book',
  imports: [],
  templateUrl: './delete-book.component.html',
  styleUrl: './delete-book.component.css',
})
export class DeleteBookComponent {
  @Input() book: any;

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router
  ) {}

  onDelete() {
    const token = localStorage.getItem('auth_token');
    if (!token) {
      this.router.navigate(['']);
      return;
    }

    this.apiService.deleteBook(token, this.book['id']).subscribe({
      next: (data) => {
        this.router.navigate(['']);
      },
    });
  }
}
