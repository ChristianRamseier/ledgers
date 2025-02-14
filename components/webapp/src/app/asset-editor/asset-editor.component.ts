import {Component, input, InputSignal, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {AssetFormGroup} from './asset-form-group';

@Component({
  selector: 'app-asset-editor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './asset-editor.component.html',
  styleUrl: './asset-editor.component.scss'
})
export class AssetEditorComponent {

  deletable: InputSignal<Boolean> = input.required<Boolean>()

  formControl = input.required<AssetFormGroup>();

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
