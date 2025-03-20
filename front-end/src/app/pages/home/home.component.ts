import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { HeroComponent } from '../../components/hero/hero.component';
import { forkJoin } from 'rxjs';
import { SectionsComponent } from '../../components/sections/sections.component';
import { LoadingComponent } from '../../components/loading/loading.component';

@Component({
  selector: 'app-home',
  imports: [HeroComponent, SectionsComponent, LoadingComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  isLoading: boolean = true;
  book: any;
  warBooks = [];
  scieneBooks = [];
  horrorBooks = [];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    forkJoin({
      books: this.apiService.allBooks(),
      war: this.apiService.categoryBooks(2),
      science: this.apiService.categoryBooks(6),
      horror: this.apiService.categoryBooks(3),
    }).subscribe({
      next: (data) => {
        const books = data.books['Books'];
        if (books.length > 0) {
          this.book = books[Math.floor(Math.random() * books.length)];
        }

        this.warBooks = data.war['category']['books'];
        this.scieneBooks = data.science['category']['books'];
        this.horrorBooks = data.horror['category']['books'];

        this.isLoading = false;
      },
    });
  }
}
