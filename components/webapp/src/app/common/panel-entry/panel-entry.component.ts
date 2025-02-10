import {Component, input} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-panel-entry',
  standalone: true,
  imports: [CommonModule],
  providers: [],
  templateUrl: './panel-entry.component.html',
  styleUrl: './panel-entry.component.scss'
})
export class PanelEntryComponent {

  selected = input<boolean>()

  entryTitle = input<string>()

  constructor() {
  }

}
