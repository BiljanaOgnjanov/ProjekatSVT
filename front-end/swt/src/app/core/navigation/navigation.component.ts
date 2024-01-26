import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { CreatePostDialogComponent } from 'src/app/features/post/create-post-dialog.component';

export interface NavRoute {
  path: string;
  icon: string;
  protected?: boolean;
  tooltip: string;
  isDialog?: boolean;
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
      tooltip: 'Create a new post',
      isDialog: true
    },
    {
      path: '/profile',
      icon: 'person',
      tooltip: 'View your profile'
    },
    {
      path: '/group',
      icon: 'groups',
      tooltip: 'Manage groups'
    },
    {
      path: '/search',
      icon: 'search',
      tooltip: 'Search users'
    },
    {
      path: '/notifications',
      icon: 'notifications',
      tooltip: 'Notifications'
    }
  ]

  constructor(private authService: AuthService, public dialog: MatDialog) { }

  openDialog(): void {
    this.dialog.open(CreatePostDialogComponent);
  }
  
  logout(): void {
    this.authService.logout();
  }

}
