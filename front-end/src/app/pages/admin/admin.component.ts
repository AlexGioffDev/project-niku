import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { EditRoleComponent } from '../../components/edit-role/edit-role.component';
import { DeleteBookComponent } from '../../components/delete-book/delete-book.component';

@Component({
  selector: 'app-admin',
  imports: [EditRoleComponent, RouterLink, DeleteBookComponent],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent implements OnInit {
  constructor(
    public authService: AuthService,
    private router: Router,
    private apiService: ApiService
  ) {}

  users: any;
  books: any;

  ngOnInit(): void {
    if (this.authService.role()?.toUpperCase() !== 'ADMIN') {
      this.router.navigate(['']);
      return;
    }

    const token = localStorage.getItem('auth_token');

    this.apiService.getAllUsers(token!).subscribe({
      next: (data) => {
        this.users = data['Users'];
      },
    });

    this.apiService.allBooks().subscribe({
      next: (data) => {
        this.books = data['Books'];
      },
    });
  }
}
