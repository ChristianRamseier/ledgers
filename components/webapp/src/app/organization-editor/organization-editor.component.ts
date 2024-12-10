import {Component, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OrganizationFormControl} from './organization-form-control';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-organization-editor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './organization-editor.component.html',
  styleUrl: './organization-editor.component.scss'
})
export class OrganizationEditorComponent {

  formControl = input.required<OrganizationFormControl>();

  onSave = output()

  onCancel = output()

  save() {
    this.onSave.emit()
  }

  cancel() {
    this.onCancel.emit()
  }

}
