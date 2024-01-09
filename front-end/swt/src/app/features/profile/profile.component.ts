import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from "@angular/forms";
import { ToastrService } from "ngx-toastr";
import { catchError, throwError } from "rxjs";
import { environment } from "src/environments/environment";

interface User {
  username: string;
  displayName?: string;
  email: string;
  firstName: string;
  lastName: string;
  description?: string;
}

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User = {
    username: "",
    email: "",
    firstName: "",
    lastName: ""
  };
  showChangePassword: boolean = false;
  currentPassword = new FormControl('', [Validators.required]);
  newPassword = new FormControl('', [Validators.required, Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/)]);
  repeatNewPassword = new FormControl('', [Validators.required]);
  changePasswordForm: FormGroup = new FormGroup({
    password: this.newPassword,
    confirmPassword: this.repeatNewPassword
  }, { validators: this.confirmPasswordValidator() });

  ngOnInit(): void {
    this.getUser()
  }

  getUser(): void {
    this.http.get<any>(`${environment.apiURL}/user`).subscribe(resp => {
      this.user = resp.data
    });
  }

  constructor(private http: HttpClient, private toastr: ToastrService) { }


  onDisplayNameEdit() {
    this.http.patch<any>(`${environment.apiURL}/user/display-name`, {"displayName": this.user.displayName}).subscribe(resp => {
      this.toastr.success(resp.message);
    });
  }
  
  onDescriptionEdit() {
    this.http.patch<any>(`${environment.apiURL}/user/description`, {"description": this.user.description}).subscribe(resp => {
      this.toastr.success(resp.message);
    });
  }

  toggleChangePassword(): void {
    this.showChangePassword = !this.showChangePassword;

    if (!this.showChangePassword) {
      this.currentPassword.reset();
      this.newPassword.reset()
      this.repeatNewPassword.reset()
    }
  }

  changePassword(): void {
    if (!this.changePasswordForm.valid) {
      return;
    }
    this.http.patch<any>(`${environment.apiURL}/user/password-change`, {
      "currentPassword": this.currentPassword.value,
      "newPassword": this.newPassword.value
    }).pipe(
      catchError(error => {
        this.toastr.error(error.error.message);
        return throwError(() => error);
      })
    ).subscribe(resp => {
      this.toastr.success(resp.message);
    });
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