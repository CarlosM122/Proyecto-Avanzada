import { Component } from '@angular/core';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

@Component({
  selector: 'app-login',

  standalone: true,

  imports: [
    ReactiveFormsModule
  ],

  templateUrl: './login.html',

  styleUrls: ['./login.css']
})
export class Login {

  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder
  ) {

    this.loginForm = this.fb.group({

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6)
        ]
      ]

    });

  }

  onSubmit(): void {

    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();

      return;

    }

    console.log(this.loginForm.value);

  }

}