import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-editable',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './editable.component.html',
  styleUrl: './editable.component.scss'
})
export class EditableComponent {

  inputValue = '';

  @Input() label: string = ''

  @Output() onChange = new EventEmitter<string>();

  @ViewChild('inputField') input?: ElementRef;

  isEditMode = false;

  @Input()
  set value(value: string) {
    this.inputValue = value;
  }

  edit() {
    this.isEditMode = true
    setTimeout(() => { // this will make the execution after the above boolean has changed
      this.input?.nativeElement.focus();
      this.input?.nativeElement.select();
    }, 0);
  }

  cancel() {
    this.isEditMode = false
  }

  save() {
    this.onChange.emit(this.input?.nativeElement.value)
    this.isEditMode = false
  }

  keyDown($event: KeyboardEvent) {
    if($event.code == 'Escape') {
      this.cancel()
    }
  }

  protected readonly localStorage = localStorage;
}
