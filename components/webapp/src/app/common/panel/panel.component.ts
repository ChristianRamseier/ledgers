import {Component, HostBinding, Input} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [CommonModule],
  providers: [],
  templateUrl: './panel.component.html',
  styleUrl: './panel.component.scss'
})
export class PanelComponent {

  @Input()
  panelTitle = ''

  expanded: boolean = true;

  constructor() {
  }

  toggle() {
    this.expanded = !this.expanded
  }
}
