import { HttpClient, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

interface SearchedUser {
  id: number;
  username: string;
  displayName?: string;
  firstName: string;
  lastName: string;
  isFriend: boolean;
}

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {

  searchQuery = '';
  searchedUsers: SearchedUser[] = [];

  constructor(private http: HttpClient, private toastr: ToastrService) { }

  search(): void {
    let firstName = '';
    let lastName = '';
    const words = this.searchQuery.split(' ');

    if (words.length > 0) {
      firstName = words[0];
    }
    if (words.length > 1) {
      lastName = words[1];
    }

    let params = new HttpParams();
    if (firstName.trim() !== '') {
      params = params.set('firstName', firstName);
    }
    if (lastName.trim() !== '') {
      params = params.set('lastName', lastName);
    }
    this.http.get<any>(`${environment.apiURL}/user/search`, { params }).subscribe(resp => {
      this.searchedUsers = resp.data;
    });
  }

  addFriend(userId: number) {
    this.http.post(`${environment.apiURL}/user/friend-requests/send`, {userId: userId}).pipe(
      catchError(error => {
        this.toastr.error(error.error.message);
        return throwError(() => error);
      })
    ).subscribe(
      () => {
        this.toastr.success('Friend request sent successfully');
      }
    );
  }
}
