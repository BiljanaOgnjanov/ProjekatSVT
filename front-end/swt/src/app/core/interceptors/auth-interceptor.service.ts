import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { AuthService } from "../services/auth.service";
import { ToastrService } from "ngx-toastr";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private toastr: ToastrService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = this.addToken(req, this.authService.token)

    return next.handle(req).pipe(
      catchError((error) => {
        if (error.status === 401) {
          this.authService.logout()
          this.toastr.info("Session expired. Please log in to continue.");
        }
        return throwError(() => error);
      })
    )
  }

  private addToken(req: HttpRequest<any>, token: string | null): HttpRequest<any> {
    return !token
      ? req
      : req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
  }
}