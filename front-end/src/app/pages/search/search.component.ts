import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { LoadingComponent } from '../../components/loading/loading.component';
import { NoBookComponent } from '../../components/no-book/no-book.component';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-search',
  imports: [LoadingComponent, NoBookComponent, RouterLink],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css',
})
export class SearchComponent implements OnInit {
  loading: boolean = true;
  query: string = '';
  books: Book[] = [];
  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe({
      next: (params) => {
        this.query = params['query'] || '';
        if (this.query === '') {
          this.router.navigate(['']);
        } else {
          this.apiService.searchBook(this.query).subscribe({
            next: (data) => {
              this.books = data['Books Found'];
              this.loading = false;
            },
            error: (error) => {
              this.loading = false;
            },
          });
        }
      },
    });
  }
}
