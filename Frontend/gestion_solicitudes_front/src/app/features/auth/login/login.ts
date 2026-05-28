import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { Component, inject, ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
  styles: ``,
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  form: FormGroup = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  loading = false;
  errorMsg = '';

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMsg = '';

    this.authService.login(this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/solicitudes']);
      },
      error: (err) => {
        if (err.status === 0) {
          this.loading = false;
          this.errorMsg =
            'No se pudo conectar al servidor. Por favor, verifica tu conexión e inténtalo de nuevo.';
        } else if (err.status === 401) {
          this.loading = false;
          this.errorMsg = 'Correo o contraseña incorrectos. Por favor, inténtalo de nuevo.';
        } else {
          this.loading = false;
          this.errorMsg = 'Ocurrió un error inesperado. Por favor, inténtalo de nuevo más tarde.';
        }
        this.cdr.detectChanges();
      },
    });
  }

  get correo() {
    return this.form.get('correo')!;
  }

  get password() {
    return this.form.get('password')!;
  }

  irRegister() {
    this.router.navigate(['/register']);
  }
}
