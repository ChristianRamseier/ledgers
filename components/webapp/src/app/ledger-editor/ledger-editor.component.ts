import {Component, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LedgerFormControl} from './ledger-form-control';
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

  formControl = input.required<LedgerFormControl>();
  organizations = input.required<Organization[]>();

  onSave = output()

  onCancel = output()

  save() {
    this.onSave.emit()
  }

  cancel() {
    this.onCancel.emit()
  }

}
