import {Component, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {AssetFormControl} from './asset-form-control';

@Component({
  selector: 'app-asset-editor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './asset-editor.component.html',
  styleUrl: './asset-editor.component.scss'
})
export class AssetEditorComponent {

  formControl = input.required<AssetFormControl>();

  onSave = output()

  onCancel = output()

  save() {
    this.onSave.emit()
  }

  cancel() {
    this.onCancel.emit()
  }

}
