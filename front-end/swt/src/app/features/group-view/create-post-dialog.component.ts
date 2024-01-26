import { Component, Inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { CreatePostDialogData } from "src/app/shared/model";

@Component({
  selector: "create-post-dialog.component",
  templateUrl: "create-post-dialog.component.html",
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatDialogModule]
})
export class CreatePostDialog {

  constructor(public dialogRef: MatDialogRef<CreatePostDialog>, @Inject(MAT_DIALOG_DATA) public data: CreatePostDialogData) {}

  onCancel() : void {
    this.dialogRef.close()
  };
}