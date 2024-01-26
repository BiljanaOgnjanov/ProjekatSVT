import { Component, Inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { SuspendGroupDialogData } from "src/app/shared/model";

@Component({
  selector: "suspend-group-dialog.component",
  templateUrl: "suspend-group-dialog.component.html",
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatDialogModule]
})
export class SuspendGroupDialog {

  constructor(public dialogRef: MatDialogRef<SuspendGroupDialog>, @Inject(MAT_DIALOG_DATA) public data: SuspendGroupDialogData) {}

  onCancel() : void {
    this.dialogRef.close()
  };
}