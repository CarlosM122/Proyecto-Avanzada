import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    semestre: [null, [Validators.required, Validators.min(1), Validators.max(10)]],
    telefono: [''],
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

    const body = {
      ...this.form.value,
      role: 'ESTUDIANTE',
    };

    this.authService.register(body).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/solicitudes']);
      },
      error: (err) => {
        this.loading = false;

        if (err.status === 0) {
          this.errorMsg = 'No se pudo conectar al servidor.';
        } else if (err.status === 400) {
          this.errorMsg = 'El correo ya está registrado.';
        } else {
          this.errorMsg = 'Ocurrió un error inesperado. Intenta de nuevo.';
        }

        this.cdr.detectChanges();
      },
    });
  }

  irLogin() {
    this.router.navigate(['/login']);
  }

  get nombre() {
    return this.form.get('nombre')!;
  }
  get correo() {
    return this.form.get('correo')!;
  }
  get password() {
    return this.form.get('password')!;
  }
  get semestre() {
    return this.form.get('semestre')!;
  }
}
