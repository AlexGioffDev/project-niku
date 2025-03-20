import { Component, Input, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-edit-role',
  imports: [ReactiveFormsModule],
  templateUrl: './edit-role.component.html',
  styleUrl: './edit-role.component.css',
})
export class EditRoleComponent implements OnInit {
  constructor(private apiService: ApiService) {}

  myForm: FormGroup = new FormGroup('');
  @Input() user: any;

  ngOnInit(): void {
    this.myForm = new FormGroup({
      newRole: new FormControl(this.user['role'], [Validators.required]),
    });
  }

  onSubmit(form: FormGroup): void {
    if (form.invalid) return;

    const token = localStorage.getItem('auth_token');

    if (!token) return;

    this.apiService
      .editUserRole(token, this.user['id'], form.value['newRole'])
      .subscribe({
        next: (data) => {
          console.log(data);
        },
      });
  }
}
