import { Observable, map, tap } from "rxjs";
import { GenericDataService } from "../../shared/generic-data.service";
import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Router } from "@angular/router";
import { environment } from "src/environments/environment";

export interface LoginDto {
  username: string;
  password: string;
}

export interface RegisterDto {
  
    username: string;
    password: string;
    firstName: string;
    lastName: string;
    email: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService extends GenericDataService<string> {
  token: string | null = null;
  isLogged$: Observable<boolean> = this.data$.pipe(map((token) => !!token));

  constructor(private http: HttpClient, private router: Router) {
    super();
    const token: string | null = localStorage.getItem('access_token');
    this.setData = token;
  }

  override set setData(data: string | null) {
    super.setData = data;
    this.token = data;
    data !== null ? localStorage.setItem('access_token', data) : localStorage.removeItem('access_token');
  }

  login(data: LoginDto): Observable<any> {
    return this.http.post(`${environment.apiURL}/auth/login`, data)
        .pipe(tap((res: any) => (this.setData = res.data ?? null)))
  }

  register(data: RegisterDto): Observable<any> {
    return this.addErrorHandler(
      this.http.post(`${environment.apiURL}/auth/register`, data)
        .pipe(tap((res: any) => (this.setData = res.data ?? null)))
    )
  }

  logout(): void {
    this.setData = null;
    this.router.navigate(['/login']);
  }
}