import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { LoadingComponent } from '../../components/loading/loading.component';
import { NoBookComponent } from '../../components/no-book/no-book.component';

@Component({
  selector: 'app-category',
  imports: [LoadingComponent, NoBookComponent, RouterLink],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css',
})
export class CategoryComponent implements OnInit {
  category?: number;
  categoryName?: string;
  books = [];
  loading: boolean = true;
  error: boolean = false;

  private activatedRouted = inject(ActivatedRoute);

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.activatedRouted.params.subscribe((params) => {
      this.category = params['category'];

      if (!this.category || this.category <= 0) {
        this.router.navigate(['']);
        return;
      }

      this.loadCategory();
    });
  }

  private loadCategory(): void {
    this.apiService.categoryBooks(this.category!).subscribe({
      next: (data) => {
        this.categoryName = data['category']['categoryName'];
        this.books = data['category']['books'];
        this.error = false;
        this.loading = false;
      },
      error: (error) => {
        this.error = true;
        this.loading = false;
      },
    });
  }
}
