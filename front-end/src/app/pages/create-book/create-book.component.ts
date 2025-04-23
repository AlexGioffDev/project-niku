import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-create-book',
  imports: [ReactiveFormsModule],
  templateUrl: './create-book.component.html',
  styleUrl: './create-book.component.css',
})
export class CreateBookComponent implements OnInit {
  myForm: FormGroup = new FormGroup('');
  hasError: boolean = false;
  token = localStorage.getItem('auth_token');
  categories: Category[] = [];

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (
      !this.authService.isAuthenticated() ||
      this.authService.role() !== 'ADMIN' ||
      this.token === null
    ) {
      this.router.navigate(['']);
      return;
    }

    this.myForm = new FormGroup({
      title: new FormControl('', [Validators.required]),
      author: new FormControl('', [Validators.required]),
      plot: new FormControl('', [Validators.required]),
      coverImage: new FormControl('', [Validators.required]),
      releaseYear: new FormControl('', [Validators.required]),
      categoryID: new FormControl('', [Validators.required]),
    });

    this.myForm.valueChanges.subscribe(() => {
      this.hasError = false;
    });

    this.apiService.getCategories(this.token!).subscribe({
      next: (data) => {
        this.categories = data['categories'];
      },
    });
  }

  onSubmit(form: FormGroup) {
    if (form.invalid || this.token === null) {
      this.hasError = true;
      return;
    }

    this.apiService
      .createBook(
        this.token!,
        form.value.title,
        form.value.author,
        form.value.plot,
        form.value['coverImage'],
        parseInt(form.value['releaseYear'], 10),
        parseInt(form.value['categoryID'], 10)
      )
      .subscribe({
        next: (data) => {
          form.reset();
          this.router.navigate(['']);
        },
        error: (error) => {
          console.log(error);
          this.hasError = true;
        },
      });
  }
}
