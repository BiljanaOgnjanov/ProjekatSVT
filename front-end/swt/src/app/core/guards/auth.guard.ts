import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable, map, take } from "rxjs";
import { AuthService } from "../services/auth.service";
import { inject } from "@angular/core";

export interface GuardType {
  inverse: boolean;
}

const resolve = (isAllowed: boolean, router: Router): boolean | UrlTree => {
  return isAllowed ? true : router.createUrlTree(['/login']);
};

export const AuthGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> => {
  
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);

  return authService.isLogged$.pipe(
    take(1),
    map((isLogged: boolean) =>
      next.data['inverse']
        ? resolve(!isLogged, router)
        : resolve(isLogged, router)
    )
  );
};