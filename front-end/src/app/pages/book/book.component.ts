import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CreateCommentComponent } from '../../components/create-comment/create-comment.component';
import { CommentComponent } from '../../components/comment/comment.component';
import { NoBookComponent } from '../../components/no-book/no-book.component';
import { LoadingComponent } from '../../components/loading/loading.component';

@Component({
  selector: 'app-book',
  imports: [
    CommentComponent,
    CreateCommentComponent,
    NoBookComponent,
    LoadingComponent,
    RouterLink,
  ],
  templateUrl: './book.component.html',
  styleUrl: './book.component.css',
})
export class BookComponent implements OnInit, OnDestroy {
  private activatedRoute = inject(ActivatedRoute);
  constructor(
    private apiService: ApiService,
    public authService: AuthService
  ) {}
  book: any;
  isLoading: boolean = true;
  bookId?: number;

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params) => {
      this.bookId = params['id'];
      this.loadBookData();
    });
  }

  ngOnDestroy(): void {
    this.book = null;
    this.bookId = undefined;
  }

  private loadBookData(): void {
    this.apiService.getBook(this.bookId!).subscribe({
      next: (data) => {
        this.book = data['Book'];
        this.isLoading = false;
      },
      error: (error) => {
        this.book = undefined;
        this.isLoading = false;
      },
    });
  }
}
