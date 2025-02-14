import {Component, Input, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LedgerFormGroup} from './ledger-form-group';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Ledger, NewLedger, NewOrganization, Organization} from '../story/story-dto';

@Component({
  selector: 'app-ledger-editor',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './ledger-editor.component.html',
  styleUrl: './ledger-editor.component.scss'
})
export class LedgerEditorComponent {

  formControl = input.required<LedgerFormGroup>();
  organizations = input.required<Organization[]>();

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
