import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { MatDialogRef } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { environment } from "src/environments/environment";


@Component({
  templateUrl: './create-post-dialog.component.html',
  styleUrls: ['./create-post-dialog.component.css']
})
export class CreatePostDialogComponent {

  content: string = '';

  constructor(
    public dialogRef: MatDialogRef<CreatePostDialogComponent>, 
    private http: HttpClient, 
    private toastr: ToastrService,
    private router: Router
    ) {}

  onSubmit(): void {
    if (this.content) {
      this.http.post<any>(`${environment.apiURL}/post/create`, {"content": this.content}).subscribe((data) => {
        this.toastr.success(data.message);
        this.dialogRef.close();
        this.router.navigate(['/home']);
      })
    }
  }

  onCancel(): void {
    this.dialogRef.close()
  };

}