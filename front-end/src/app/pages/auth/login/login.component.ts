import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  myForm: FormGroup = new FormGroup('');
  hasError: boolean = false;

  ngOnInit(): void {
    this.myForm = new FormGroup({
      username: new FormControl(''),
      password: new FormControl(''),
    });

    this.myForm.valueChanges.subscribe(() => {
      this.hasError = false;
    });
  }

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(form: FormGroup) {
    if (form.valid) {
      const newUser: {
        username: string;
        password: string;
      } = {
        username: form.value.username,
        password: form.value.password,
      };

      this.authService.login(newUser.username, newUser.password).subscribe({
        next: (user) => {
          this.router.navigate(['']);
          this.hasError = false;
        },
        error: (error) => {
          this.hasError = true;
        },
      });
    } else {
      this.hasError = true;
    }
  }
}
