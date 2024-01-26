import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { EditCommentDialogData } from "src/app/shared/model";

@Component({
  selector: "edit-comment-dialog.component",
  templateUrl: "edit-comment-dialog.component.html",
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatDialogModule]
})
export class EditCommentDialog {

  constructor(public dialogRef: MatDialogRef<EditCommentDialog>, @Inject(MAT_DIALOG_DATA) public data: EditCommentDialogData) {}

  onCancel() : void {
    this.dialogRef.close()
  };
}