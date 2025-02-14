import {Component, input, output, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OrganizationFormGroup} from './organization-form-group';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-organization-editor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './organization-editor.component.html',
  styleUrl: './organization-editor.component.scss'
})
export class OrganizationEditorComponent {

  formControl = input.required<OrganizationFormGroup>();

  deletable = input.required<boolean>();

  onSave = output()
  onCancel = output()
  onDelete = output()

  save() {
    this.onSave.emit()
  }

  cancel() {
    this.onCancel.emit()
  }

  delete() {
    this.onDelete.emit()
  }

}
