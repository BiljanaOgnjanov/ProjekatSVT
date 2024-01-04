import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { EditDialogData } from "./home.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";

@Component({
  selector: "edit-dialog.component",
  templateUrl: "edit-dialog.component.html",
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatDialogModule]
})
export class EditDialog {

  constructor(public dialogRef: MatDialogRef<EditDialog>, @Inject(MAT_DIALOG_DATA) public data: EditDialogData) {}

  onCancel() : void {
    this.dialogRef.close()
  };
}