import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest } from '../models/auth.model';

const TOKEN_KEY = 'jwt_token';
const API = 'http://localhost:8081';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private _token = signal<string | null>(localStorage.getItem(TOKEN_KEY));

  readonly isLoggedIn = computed(() => !!this._token());
  readonly token = this._token.asReadonly();

  constructor(private http: HttpClient) {}

  login(body: LoginRequest) {
    return this.http
      .post<LoginResponse>(`${API}/auth/login`, body)
      .pipe(tap((res) => this.saveToken(res.token)));
  }

  register(body: RegisterRequest) {
    return this.http
      .post<LoginResponse>(`${API}/auth/register`, body)
      .pipe(tap((res) => this.saveToken(res.token)));
  }

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    this._token.set(null);
  }

  getRole(): string | null {
    const t = this._token();
    if (!t) return null;
    try {
      const payload = JSON.parse(atob(t.split('.')[1]));
      return payload.role ?? null;
    } catch {
      return null;
    }
  }

  private saveToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
    this._token.set(token);
  }

  crearUsuario(body: any) {
    return this.http.post(`${API}/usuarios/crear`, body, { responseType: 'text' });
  }
}
