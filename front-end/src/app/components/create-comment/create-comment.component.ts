import { Component, Input, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-create-comment',
  imports: [ReactiveFormsModule],
  templateUrl: './create-comment.component.html',
  styleUrl: './create-comment.component.css',
})
export class CreateCommentComponent implements OnInit {
  @Input() bookId: number = 0;

  myForm: FormGroup = new FormGroup('');
  hasError: boolean = false;

  constructor(private apiService: ApiService, private location: Location) {}

  ngOnInit(): void {
    this.myForm = new FormGroup({
      content: new FormControl('', [Validators.required]),
      rating: new FormControl('1', [Validators.required]),
    });
    this.myForm.valueChanges.subscribe(() => {
      this.hasError = false;
    });
  }

  onSubmit(form: FormGroup): void {
    if (form.invalid || !this.bookId) {
      this.hasError = true;
      return;
    }

    const commentData = {
      content: form.value.content,
      rating: parseInt(form.value.rating, 10),
    };

    const token = localStorage.getItem('auth_token');

    this.apiService.addComment(this.bookId!, commentData, token!).subscribe({
      next: (response) => {
        form.reset();
        window.location.reload();
        this.hasError = false;
      },
      error: (error) => {
        this.hasError = true;
      },
    });
  }
}
