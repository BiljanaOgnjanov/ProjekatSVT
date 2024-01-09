import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { CommentDialogData } from "./home.component";

@Component({
  selector: "comment-dialog.component",
  templateUrl: "comment-dialog.component.html",
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatDialogModule]
})
export class CommentDialog {

  constructor(public dialogRef: MatDialogRef<CommentDialog>, @Inject(MAT_DIALOG_DATA) public data: CommentDialogData) {}

  onCancel() : void {
    this.dialogRef.close()
  };
}