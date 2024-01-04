import { Component, EventEmitter, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";

@Component({
  selector: "delete-dialog.component",
  templateUrl: "delete-dialog.component.html",
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatDialogModule]
})
export class DeleteDialog {
  @Output() deleteConfirmed = new EventEmitter<void>();
  
  constructor(public dialogRef: MatDialogRef<DeleteDialog>) {}

  onConfirm(): void {
    this.dialogRef.close();
    this.deleteConfirmed.emit();
  }

  onCancel() : void {
    this.dialogRef.close()
  };
}