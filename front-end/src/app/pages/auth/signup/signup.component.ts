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
  selector: 'app-signup',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent implements OnInit {
  myForm: FormGroup = new FormGroup('');
  hasError: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.myForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [
        Validators.minLength(6),
        Validators.required,
      ]),
    });

    this.myForm.valueChanges.subscribe(() => {
      this.hasError = false;
    });
  }

  onSubmit(form: FormGroup) {
    if (form.valid) {
      const newUser = ({ username: String, password: String } = {
        username: form.value.username,
        password: form.value.password,
      });

      this.authService.register(newUser.username, newUser.password).subscribe({
        next: (user) => {
          this.hasError = false;
          this.router.navigate(['auth', 'login']);
        },
        error: (error) => {
          console.log(error);
          this.hasError = true;
        },
      });
    } else {
      this.hasError = true;
    }
  }
}
