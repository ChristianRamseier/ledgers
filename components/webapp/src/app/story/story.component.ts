import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PanelComponent} from '../common/panel/panel.component';
import {PanelsComponent} from '../common/panels/panels.component';
import {PanelEntryComponent} from '../common/panel-entry/panel-entry.component';
import {Organization, StoryDto} from './story-dto'
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

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, PanelComponent, PanelsComponent, PanelEntryComponent, ChaptersComponent, OrganizationEditorComponent, LedgerEditorComponent],
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

  addLedger() {
    this.editor = Editor.Ledger
    this.ledgerFormControl.setData({name: '', ownerId: ''})
  }

  resetEditor() {
    this.editor = Editor.None
  }

  saveOrganization() {
    const organization = this.organizationFormControl.toOrganization()
    if (this.organizationFormControl.id) {
      const organization = this.organizationFormControl.toOrganization() as Organization
      const reference = ComponentReference.Companion.forOrganization(organization.id, organization.version)
      const updatedStory = this.story!!.withChangedOrganization(reference, organization.name)
      this.saveStory(updatedStory)
    } else {
      const updatedStory = this.story!!.addOrganization(organization.name)
      this.saveStory(updatedStory)
    }
    this.resetEditor()
  }

  saveLedger() {

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

}

enum Editor {
  None, Organization, Ledger
}
