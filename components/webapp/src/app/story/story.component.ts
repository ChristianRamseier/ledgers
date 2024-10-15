import {Component, input, Input, OnInit, Signal, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PanelComponent} from '../common/panel/panel.component';
import {PanelsComponent} from '../common/panels/panels.component';
import {PanelEntryComponent} from '../common/panel-entry/panel-entry.component';
import {Story} from './story'
import {State} from '../state'
import {ChaptersComponent} from '../chapters/chapters.component';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, PanelComponent, PanelsComponent, PanelEntryComponent, ChaptersComponent],
  templateUrl: './story.component.html',
  styleUrl: './story.component.scss'
})
export class StoryComponent implements OnInit {

  constructor(private httpClient: HttpClient) {
  }

  story?: Story;

  ngOnInit(): void {
    this.httpClient
      .get<Story>('/api/story/50d1ebca-0d9c-4e46-888b-5b78ee94e09d')
      .subscribe(story => this.story = story)
  }

  state: State = {chapter: 0, step: 0}

  onChapterChange(chapter: number) {
    this.state = {
      ...this.state,
      chapter: chapter
    }
  }

}
