import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-panel-action',
  standalone: true,
  imports: [CommonModule],
  providers: [],
  templateUrl: './panel-action.component.html',
  styleUrl: './panel-action.component.scss'
})
export class PanelActionComponent {

  @Input()
  actionTitle = ''

  constructor() {
  }

}
