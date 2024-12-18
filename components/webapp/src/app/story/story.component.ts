import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PanelComponent} from '../common/panel/panel.component';
import {PanelsComponent} from '../common/panels/panels.component';
import {PanelEntryComponent} from '../common/panel-entry/panel-entry.component';
import {Ledger, Organization, StoryDto} from './story-dto'
import {State} from '../state'
import {ChaptersComponent} from '../chapters/chapters.component';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {OrganizationEditorComponent} from '../organization-editor/organization-editor.component';
import {OrganizationFormControl} from '../organization-editor/organization-form-control';
import {LedgerFormControl} from '../ledger-editor/ledger-form-control';
import {LedgerEditorComponent} from '../ledger-editor/ledger-editor.component';
import {org} from '../../../public';
import Story = org.ledgers.domain.Story;
import dto = org.ledgers.dto;
import ComponentReference = org.ledgers.domain.component.ComponentReference;
import {Patch} from '../patch';
import OrganizationId = org.ledgers.domain.architecture.OrganizationId;
import {PanelActionComponent} from '../common/panel-action/panel-action.component';
import {PanelSideComponent} from '../common/panel-side/panel-side.component';

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, PanelComponent, PanelsComponent, PanelEntryComponent, ChaptersComponent, OrganizationEditorComponent, LedgerEditorComponent, PanelActionComponent, PanelSideComponent],
  templateUrl: './story.component.html',
  styleUrl: './story.component.scss'
})
export class StoryComponent implements OnInit {

  Editor = Editor

  constructor(private httpClient: HttpClient) {
  }

  state: State = {chapter: 0, step: 0}
  editor = Editor.None;

  organizationFormControl = OrganizationFormControl.createDefault()
  ledgerFormControl = LedgerFormControl.createDefault()

  story?: Story;
  storyDto?: StoryDto;

  ngOnInit(): void {
    this.httpClient
      .get('/api/story/50d1ebca-0d9c-4e46-888b-5b78ee94e09d', {responseType: 'text'})
      .subscribe(json => this.setStory(dto.fromJson(json)))
  }

  onChapterChange(chapter: number) {
    this.state = {
      ...this.state,
      chapter: chapter
    }
  }

  addOrganization() {
    this.editor = Editor.Organization
    this.organizationFormControl.setData({name: ''})
  }

  editOrganization(organization: Organization) {
    this.editor = Editor.Organization
    this.organizationFormControl.setData(organization)
  }

  editLedger(ledger: Ledger) {
    this.editor = Editor.Ledger
    this.ledgerFormControl.setData(ledger)
  }

  addLedger() {
    this.editor = Editor.Ledger
    this.ledgerFormControl.setData({name: '', ownerId: ''})
  }

  resetEditor() {
    this.editor = Editor.None
  }

  saveOrganization() {

    if (this.organizationFormControl.id) {
      const organization = this.organizationFormControl.toOrganization() as Organization
      const reference = ComponentReference.Companion.forOrganization(organization.id, organization.version)
      const updatedStory = this.story!!.withChangedOrganization(reference, organization.name)
      this.saveStory(updatedStory)
    } else {
      const organization = this.organizationFormControl.toOrganization()
      const updatedStory = this.story!!.addOrganization(organization.name)
      this.saveStory(updatedStory)
    }
    this.resetEditor()
  }

  saveLedger() {
    if (this.ledgerFormControl.id) {
      const ledger = this.ledgerFormControl.toLedger() as Ledger
      const reference = ComponentReference.Companion.forLedger(ledger.id, ledger.version)
      const updatedStory = this.story!!.withChangedLedger(reference, ledger.name, new OrganizationId(ledger.ownerId))
      this.saveStory(updatedStory)
    } else {
      const ledger = this.ledgerFormControl.toLedger()
      const updatedStory = this.story!!.addLedger(ledger.name, new OrganizationId(ledger.ownerId))
      this.saveStory(updatedStory)
    }
    this.resetEditor()
  }

  private saveStory(updatedStory: Story) {
    this.setStory(updatedStory)
    const headers = new HttpHeaders({
      "Content-Type": "application/json",
      "Accept": "application/json"
    })
    this.httpClient
      .post(`/api/story/${updatedStory.id}`, dto.toJson(updatedStory), {headers: headers})
      .subscribe()
  }

  private setStory(updatedStory: Story) {
    this.story = updatedStory
    this.storyDto = Patch.apply(this.storyDto, JSON.parse(dto.toJson(updatedStory)))
  }

  isOrganizationBeingEdited(organization: Organization) {
    return this.editor == Editor.Organization
      && organization.id == this.organizationFormControl.id
      && organization.version == this.organizationFormControl.version
  }

  isLedgerBeeingEdited(ledger: Ledger) {
    return this.editor == Editor.Ledger
      && ledger.id == this.ledgerFormControl.id
      && ledger.version == this.ledgerFormControl.version
  }
}

enum Editor {
  None, Organization, Ledger
}
