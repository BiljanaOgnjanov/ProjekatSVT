import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EMPTY, Observable, Subject, catchError, switchMap, tap } from 'rxjs';
import { LoginDto, AuthService } from '../../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form = new FormGroup({})
  usernameControl = new FormControl(null, [Validators.required])
  passwordControl = new FormControl(null, [Validators.required])
  error$: Observable<string | null> = this.authService.error$;
  login$: Subject<any> = new Subject().pipe(
    switchMap((data: LoginDto | unknown) => {
      return this.authService.login(data as LoginDto).pipe(
        catchError((error: any) => {
          this.toastr.error(error.error.message);
          return EMPTY
        })
      );
    }),
    tap((data) => {
      this.toastr.success(data.message);
      this.router.navigate(['/home']);
    })
  ) as Subject<any>;

  onLogin(e: any): void {
    e.preventDefault();

    this.authService.clearError();
    if (!this.usernameControl.valid || !this.passwordControl.valid)
      return;
    const data: LoginDto = {
      username: this.usernameControl.value ?? "",
      password: this.passwordControl.value ?? "",
    }
    this.login$.next(data);
  }

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) { }

}
