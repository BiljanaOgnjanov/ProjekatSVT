import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { EMPTY, Observable, Subject, catchError, switchMap, tap } from 'rxjs';
import { RegisterDto, AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  
  usernameControl = new FormControl(null, [Validators.required])
  passwordControl = new FormControl(null, [Validators.required, Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/)])
  firstNameControl = new FormControl(null, [Validators.required])
  lastNameControl = new FormControl(null, [Validators.required])
  emailControl = new FormControl(null, [Validators.required, Validators.email])
  confirmPasswordControl = new FormControl(null, [Validators.required])
  form: FormGroup = new FormGroup({
    password: this.passwordControl,
    confirmPassword: this.confirmPasswordControl
  }, { validators: this.confirmPasswordValidator() });

  error$: Observable<string | null> = this.authService.error$;
  register$: Subject<any> = new Subject().pipe(
    switchMap((data: RegisterDto | unknown) => {
      return this.authService.register(data as RegisterDto).pipe(
        catchError(() => EMPTY)
      );
    }),
    tap((data) => {
      this.toastr.success(data.message);
      this.router.navigate(['/home'])
    })
  ) as Subject<any>;

  get passwordFormField() {
    return this.form.get('password');
  }

  getPasswordErrorMessage(): string | null {
    const password = this.passwordFormField?.value;
  
    if (this.passwordControl.hasError('required')) {
      return 'Enter your password';
    }
  
    if (!password?.match('.{8,}')) {
      return 'Password must be at least eight characters long';
    }
  
    if (!password?.match('^(?=.*[A-Z])')) {
      return 'Password must contain at least one uppercase letter';
    }
  
    if (!password?.match('(?=.*[a-z])')) {
      return 'Password must contain at least one lowercase letter';
    }
  
    if (!password?.match('(.*[0-9].*)')) {
      return 'Password must contain at least one digit';
    }
  
    if (!password?.match('(?=.*[!@#$%^&*])')) {
      return 'Password must contain at least one special character such as !, @, #...';
    }
  
    return null;
  }

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) { }

  onRegister(e: any) {
    e.preventDefault();

    this.authService.clearError();
    if (!this.usernameControl.valid || !this.passwordControl.valid || !this.firstNameControl.valid || !this.lastNameControl.valid || !this.emailControl.valid || !this.confirmPasswordControl.valid)
      return;
    const data: RegisterDto = {
      username: this.usernameControl.value ?? "",
      password: this.passwordControl.value ?? "",
      firstName: this.firstNameControl.value ?? "",
      lastName: this.lastNameControl.value ?? "",
      email: this.emailControl.value ?? "",
    };
    this.register$.next(data);
  }

  confirmPasswordValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const passwordControl = control.get('password');
      const confirmPasswordControl = control.get('confirmPassword');
  
      if (!passwordControl || !confirmPasswordControl) {
        return null;
      }
  
      if (passwordControl.value !== confirmPasswordControl.value) {
        confirmPasswordControl.setErrors({ confirmPasswordMismatch: true });
        return { confirmPasswordMismatch: true };
      } else {
        confirmPasswordControl.setErrors(null);
        return null;
      }
    };
  }
}
