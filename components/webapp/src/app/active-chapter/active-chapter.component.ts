import {Component, computed, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EditableComponent} from '../common/editable/editable.component';
import {StageChange} from '../story/story-dto';
import {PanelComponent} from '../common/panel/panel.component';
import {PanelEntryComponent} from '../common/panel-entry/panel-entry.component';
import {PanelsComponent} from '../common/panels/panels.component';
import {org} from '../../../public';
import {PanelActionComponent} from '../common/panel-action/panel-action.component';
import ComponentReference = org.ledgers.domain.component.ComponentReference;

@Component({
  selector: 'app-active-chapter',
  standalone: true,
  imports: [CommonModule, EditableComponent, PanelComponent, PanelEntryComponent, PanelsComponent, PanelActionComponent],
  templateUrl: './active-chapter.component.html',
  styleUrl: './active-chapter.component.scss'
})
export class ActiveChapterComponent {

  number = input.required<number>();
  name = input.required<string>();
  changes = input.required<StageChange[]>();

  changeNameProvider = input.required<{ getComponentDisplayName(reference: ComponentReference, chapter: number): string }>();

  label = computed<string>(() => `Chapter ${(this.number() + 1)}${this.name() ? ' - ' : ''}`);

  onChapterNameChange = output<string>();
  onChangeRemovalFromStage = output<StageChange>();

  onNameChange(newName: string) {
    this.onChapterNameChange.emit(newName);
  }

  getChangeLabel(change: StageChange) {
    const reference = ComponentReference.Companion.fromReferenceParts(change.component.reference.type, change.component.reference.id, change.component.reference.version)
    return `${change.type} ${change.component.reference.type} ${this.changeNameProvider().getComponentDisplayName(reference, this.number())}`;
  }

  removeFromStage(change: StageChange) {
    this.onChangeRemovalFromStage.emit(change)
  }

}
