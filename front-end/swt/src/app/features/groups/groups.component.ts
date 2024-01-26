import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

interface Group {
  id: number;
  name: string;
  description: string;
  isMember: boolean;
}

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {

  groups: Group[] = [];

  createGroupForm = new FormGroup({
    name: new FormControl(null, [Validators.required]),
    description: new FormControl(null, [Validators.required])
  });
  showCreateGroup: boolean = false;

  constructor(private http: HttpClient, private toastr: ToastrService) { }

  ngOnInit() {
    this.getGroups();
  }

  getGroups(): void {
    this.http.get<any>(`${environment.apiURL}/group`).subscribe(resp => {
      this.groups = resp.data
    });
  }

  toggleCreateGroup(): void {
    this.showCreateGroup = !this.showCreateGroup;

    if (!this.showCreateGroup) {
      this.createGroupForm.reset();
    }
  }

  createGroup(): void {
    if (this.createGroupForm.valid) {
      const groupData = this.createGroupForm.value;

      this.http.post(`${environment.apiURL}/group/create`, groupData).pipe(
        catchError(error => {
          this.toastr.error(error.error.message);
          return throwError(() => error);
        })
      ).subscribe(
        () => {
          this.toastr.success('Group created successfully');
          this.getGroups();
          this.createGroupForm.reset();
        }
      );
    }
  }

  onJoin(groupId: number) {
    this.http.post(`${environment.apiURL}/group/${groupId}/create-request`, {}).pipe(
      catchError(error => {
        this.toastr.error(error.error.message);
        return throwError(() => error);
      })
    ).subscribe(
      () => {
        this.toastr.success('Request sent successfully');
      }
    );
  }

}
