import {Component, computed, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EditableComponent} from '../common/editable/editable.component';

@Component({
  selector: 'app-active-chapter',
  standalone: true,
  imports: [CommonModule, EditableComponent],
  templateUrl: './active-chapter.component.html',
  styleUrl: './active-chapter.component.scss'
})
export class ActiveChapterComponent {

  number = input.required<number>();

  name = input.required<string>();

  label = computed<string>(() => `Chapter ${(this.number() + 1)}${this.name() ? ' - ' : ''}`);

  onChapterNameChange = output<string>();

  onChange(newName: string) {
    this.onChapterNameChange.emit(newName);
  }

}
