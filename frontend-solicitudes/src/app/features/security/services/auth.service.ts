import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import { LoginRequest } from '../models/login-request.model';

import { AuthResponse } from '../models/auth-response';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  login(request: LoginRequest): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/auth/login`,
      request
    );

  }

  saveToken(token: string): void {

    localStorage.setItem('token', token);

  }

  getToken(): string | null {

    return localStorage.getItem('token');

  }

  isAuthenticated(): boolean {

    return !!this.getToken();

  }

  logout(): void {

    localStorage.removeItem('token');

  }

}