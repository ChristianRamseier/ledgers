import {Component, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-chapter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chapter.component.html',
  styleUrl: './chapter.component.scss'
})
export class ChapterComponent {

  number = input.required<number>();

  selected = input.required<boolean>()

  onSelect = output()

  select() {
    this.onSelect.emit()
  }
}
