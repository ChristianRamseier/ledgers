import {Component, Input, input, InputSignal, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Storyline} from '../story/story';
import {ChapterComponent} from '../chapter/chapter.component';
import {State} from '../state';

@Component({
  selector: 'app-chapters',
  standalone: true,
  imports: [CommonModule, ChapterComponent],
  templateUrl: './chapters.component.html',
  styleUrl: './chapters.component.scss'
})
export class ChaptersComponent {

  storyline: InputSignal<Storyline> = input.required<Storyline>()

  state = input.required<State>();

  onChapterChange = output<number>()

  select(chapter: number) {
    this.onChapterChange.emit(chapter)
  }
}
