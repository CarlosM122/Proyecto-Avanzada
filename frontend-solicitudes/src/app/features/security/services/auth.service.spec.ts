import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';

import { describe, it, expect, beforeEach } from 'vitest';

import { AuthService } from './auth.service';

describe('AuthService', () => {

    let service: AuthService;

    beforeEach(() => {

        TestBed.configureTestingModule({

            providers: [
                provideHttpClient()
            ]

        });

        service = TestBed.inject(AuthService);

    });

    it('should create service', () => {

        expect(service).toBeTruthy();

    });

});