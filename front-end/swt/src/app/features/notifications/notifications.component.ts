import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';

interface FriendRequest {
  id: number;
  createdAt: Date;
  username: string;
  displayName?: string;
  firstName: string;
  lastName: string;
}


@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  friendRequests: FriendRequest[] = [];
  
  constructor(private http: HttpClient, private toastr: ToastrService) { }

  ngOnInit() {
    this.getFriendRequests();
  }

  getFriendRequests(): void {
    this.http.get<any>(`${environment.apiURL}/user/friend-requests`).subscribe(resp => {
      this.friendRequests = resp.data
    });
  }

  approveRequest(requestId: number): void {
    this.http.patch(`${environment.apiURL}/user/friend-requests/${requestId}`, {"approved": true}).subscribe(() => {
      this.toastr.info("Friend request approved");
      this.getFriendRequests();
    });
  }

  rejectRequest(requestId: number): void {
    this.http.patch(`${environment.apiURL}/user/friend-requests/${requestId}`, {"approved": false}).subscribe(() => {
      this.toastr.info("Friend request rejected");
      this.getFriendRequests();
    });
  }
}
