import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

export interface NavRoute {
  path: string;
  icon: string;
  protected?: boolean;
  tooltip: string;
}

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent {

  isLogged$ = this.authService.isLogged$;

  routes: NavRoute[] = [
    {
      path: '/home',
      icon: 'home',
      tooltip: 'Home page'
    },
    {
      path: '/post',
      icon: 'add',
      tooltip: 'Create a new post'
    },
    {
      path: '/profile',
      icon: 'person',
      tooltip: 'View your profile'
    },
    {
      path: '/group',
      icon: 'group_add',
      tooltip: 'Create a new group'
    }
  ]

  constructor(private authService: AuthService) { }
  
  logout(): void {
    this.authService.logout();
  }

}
