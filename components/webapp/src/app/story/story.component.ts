import {ChangeDetectorRef, Component, effect, EventEmitter, input, InputSignal, OnInit, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PanelComponent} from '../common/panel/panel.component';
import {PanelsComponent} from '../common/panels/panels.component';
import {PanelEntryComponent} from '../common/panel-entry/panel-entry.component';
import {Asset, ComponentType, Ledger, Link, Organization, StageChange, StoryDto} from './story-dto'
import {State} from '../state'
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {OrganizationEditorComponent} from '../organization-editor/organization-editor.component';
import {OrganizationFormGroup} from '../organization-editor/organization-form-group';
import {LedgerFormGroup} from '../ledger-editor/ledger-form-group';
import {LedgerEditorComponent} from '../ledger-editor/ledger-editor.component';
import {org} from '../../../public';
import {Patch} from '../patch';
import {PanelActionComponent} from '../common/panel-action/panel-action.component';
import {PanelSideComponent} from '../common/panel-side/panel-side.component';
import {AssetFormGroup} from '../asset-editor/asset-form-group';
import {AssetEditorComponent} from '../asset-editor/asset-editor.component';
import {StorylineComponent} from '../storyline/storyline.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import Story = org.ledgers.domain.Story;
import dto = org.ledgers.dto;
import ComponentReference = org.ledgers.domain.component.ComponentReference;
import OrganizationId = org.ledgers.domain.architecture.OrganizationId;
import AssetType = org.ledgers.domain.AssetType;
import Box = org.ledgers.domain.stage.Box;
import AnchorReference = org.ledgers.domain.stage.AnchorReference;

@Component({
  selector: 'app-story',
  standalone: true,
  imports: [CommonModule, PanelComponent, PanelsComponent, PanelEntryComponent, OrganizationEditorComponent, LedgerEditorComponent, AssetEditorComponent, PanelActionComponent, PanelSideComponent, StorylineComponent, DragDropModule],
  templateUrl: './story.component.html',
  styleUrl: './story.component.scss'
})
export class StoryComponent implements OnInit {

  storyId: InputSignal<string> = input.required<string>()

  storyUpdated = output<{ story: Story, chapter: number, step: number }>();

  storyInitialized = output<{ eventEmitter: EventEmitter<any>, emptyStory: Story }>();

  storyInputs = new EventEmitter<any>();

  Editor = Editor

  constructor(private httpClient: HttpClient, private changeDetector: ChangeDetectorRef) {
    effect(() => {
      const newStoryId = this.storyId();
      if (this.currentStoryId !== newStoryId) {
        this.currentStoryId = newStoryId
        this.httpClient
          .get(`/api/story/${this.currentStoryId}`, {responseType: 'text'})
          .subscribe(json => {
            this.storyInitialized.emit({
              eventEmitter: this.storyInputs,
              emptyStory: Story.Companion.create()
            });
            this.setStory(dto.fromJson(json));
          })
      }
    });
  }

  state: State = {chapter: 0, step: 0}
  editor = Editor.None;

  organizationFormControl = OrganizationFormGroup.createDefault()
  ledgerFormControl = LedgerFormGroup.createDefault()
  assetFormControl = AssetFormGroup.createDefault()

  currentStoryId?: string;
  story?: Story;
  storyDto?: StoryDto;

  ngOnInit(): void {
    this.storyInputs.subscribe(event => {
      if (event.type === 'move-ledger') {
        const reference = ComponentReference.Companion.fromString(event.reference);
        const box = new Box(event.x, event.y, event.width, event.height);
        const story = this.story!!.withLedgerInChapter(this.state.chapter, reference, box)
        this.saveStory(story)
      } else if (event.type === 'create-link') {
        const fromAnchorReference = AnchorReference.Companion.fromString(event.startAnchor)
        const toAnchorReference = AnchorReference.Companion.fromString(event.endAnchor)
        const story = this.story!!.withLinkInChapter(this.state.chapter, fromAnchorReference, toAnchorReference)
        this.saveStory(story)
      }
      this.changeDetector.markForCheck();
    });
  }

  onChapterChange(chapter: number) {
    this.state = {
      ...this.state,
      chapter: chapter
    }
    this.emitStoryUpdate();
  }

  onChapterNameChange(event: { chapter: number, name: string } | string) {
    let chapterNumber = this.state.chapter;
    let newName = '';

    if (typeof event === 'string') {
      // Handle old format for backward compatibility
      newName = event;
    } else {
      // Handle new format from timeline component
      chapterNumber = event.chapter;
      newName = event.name;
    }

    const updatedStory = this.story!!.withChapterNamed(chapterNumber, newName);
    this.saveStory(updatedStory);
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

  editAsset(asset: Asset) {
    this.editor = Editor.Asset
    this.assetFormControl.setData(asset)
  }


  addAsset() {
    this.editor = Editor.Asset
    this.assetFormControl.setData({name: '', assetType: ''})
  }

  resetEditor() {
    this.editor = Editor.None
  }

  saveAsset() {
    if (this.assetFormControl.id) {
      const asset = this.assetFormControl.toAsset() as Asset
      const reference = ComponentReference.Companion.forAsset(asset.id, asset.version)
      const updatedStory = this.story!!.withChangedAsset(reference, asset.name, AssetType.valueOf(asset.assetType))
      this.saveStory(updatedStory)
    } else {
      const asset = this.assetFormControl.toAsset()
      const updatedStory = this.story!!.addAsset(asset.name, AssetType.valueOf(asset.assetType))
      this.saveStory(updatedStory)
    }
    this.resetEditor()
  }

  deleteAsset() {
    if (this.assetFormControl.id) {
      const asset = this.assetFormControl.toAsset() as Asset
      const reference = ComponentReference.Companion.forAsset(asset.id, asset.version)
      const updatedStory = this.story!!.removeAsset(reference)
      this.saveStory(updatedStory)
    }
    this.resetEditor()
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

  deleteOrganization() {
    if (this.organizationFormControl.id) {
      const organization = this.organizationFormControl.toOrganization() as Organization
      const reference = ComponentReference.Companion.forOrganization(organization.id, organization.version)
      const updatedStory = this.story!!.removeOrganization(reference)
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

  deleteLedger() {
    if (this.ledgerFormControl.id) {
      const ledger = this.ledgerFormControl.toLedger() as Ledger
      const reference = ComponentReference.Companion.forLedger(ledger.id, ledger.version)
      const updatedStory = this.story!!.removeLedger(reference)
      this.saveStory(updatedStory)
    }
    this.resetEditor()
  }

  private saveStory(updatedStory: Story) {
    this.setStory(updatedStory);
    this.persistStory(updatedStory);
  }

  private persistStory(updatedStory: Story) {
    const headers = new HttpHeaders({
      "Content-Type": "application/json",
      "Accept": "application/json"
    });
    this.httpClient
      .post(`/api/story/${updatedStory.id}`, dto.toJson(updatedStory), {headers: headers})
      .subscribe();
  }

  private setStory(updatedStory: Story) {
    this.story = updatedStory
    this.storyDto = Patch.apply(this.storyDto, JSON.parse(dto.toJson(updatedStory)))
    this.emitStoryUpdate();
  }

  private emitStoryUpdate() {
    if (this.story) {
      this.storyUpdated.emit({
        story: this.story,
        chapter: this.state.chapter,
        step: this.state.step
      })
    }
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

  isAssetBeeingEdited(asset: Asset) {
    return this.editor == Editor.Asset
      && asset.id == this.assetFormControl.id
      && asset.version == this.assetFormControl.version;
  }

  isEditedComponentDeletable(): boolean {
    const reference = this.getEditedComponentReference();
    return reference !== undefined && !this.story!!.isComponentUsed(reference)
  }

  getEditedComponentReference(): ComponentReference | undefined {
    switch (this.editor) {
      case Editor.None:
        return undefined;
      case Editor.Asset:
        return this.assetFormControl.id ? ComponentReference.Companion.forAsset(this.assetFormControl.id!!, this.assetFormControl.version!!) : undefined;
      case Editor.Ledger:
        return this.ledgerFormControl.id ? ComponentReference.Companion.forLedger(this.ledgerFormControl.id!!, this.ledgerFormControl.version!!) : undefined;
      case Editor.Organization:
        return this.organizationFormControl.id ? ComponentReference.Companion.forOrganization(this.organizationFormControl.id!!, this.organizationFormControl.version!!) : undefined;
    }
    return undefined;
  }

  getLinkTitle(link: Link) {
    const linkReference = ComponentReference.Companion.forLink(link.id, link.version);
    const title = this.story!!.getLinkDisplayName(linkReference, this.state.chapter)
    return title
  }

  isLedgerOnStage(ledger: Ledger) {
    const ledgerReference = ComponentReference.Companion.forLedger(ledger.id, ledger.version);
    return this.isOnStage(ledgerReference)
  }

  isLinkOnStage(link: Link) {
    const linkReference = ComponentReference.Companion.forLink(link.id, link.version);
    return this.isOnStage(linkReference)
  }

  private isOnStage(reference: ComponentReference) {
    const currentStage = this.story!!.storyline.getStageAtChapter(this.state.chapter);
    return currentStage.has(reference)
  }

  addLedgerToStage(ledger: Ledger) {
    const ledgerReference = ComponentReference.Companion.forLedger(ledger.id, ledger.version);
    const updatedStory = this.story!!.withLedgerInChapter(this.state.chapter, ledgerReference);
    this.saveStory(updatedStory)
  }

  removeLedgerAppearance(ledger: Ledger) {
    const ledgerReference = ComponentReference.Companion.forLedger(ledger.id, ledger.version);
    const updatedStory = this.story!!.withoutLedgerAppearance(this.state.chapter, ledgerReference);
    this.saveStory(updatedStory)
  }

  removeLinkAppearance(link:Link) {
    const linkReference = ComponentReference.Companion.forLink(link.id, link.version);
    const updatedStory = this.story!!.withoutLinkAppearance(this.state.chapter, linkReference);
    this.saveStory(updatedStory)
  }

  withoutLedgerOnStage(ledger: Ledger) {
    const ledgerReference = ComponentReference.Companion.forLedger(ledger.id, ledger.version);
    const updatedStory = this.story!!.withoutChangeToComponentInChapter(this.state.chapter, ledgerReference);
    this.saveStory(updatedStory)
  }

  withoutLinkOnStage(link: Link) {
    const linkReference = ComponentReference.Companion.forLink(link.id, link.version);
    const updatedStory = this.story!!.withoutChangeToComponentInChapter(this.state.chapter, linkReference);
    this.saveStory(updatedStory)
  }

  onChangeRemovalFromChapter(change: StageChange) {
    if (change.component.reference.type == ComponentType.Ledger) {
      const ledgerReference = ComponentReference.Companion.forLedger(change.component.reference.id, change.component.reference.version);
      const updatedStory = this.story!!.withoutChangeToComponentInChapter(this.state.chapter, ledgerReference);
      this.saveStory(updatedStory)
    } else if (change.component.reference.type == ComponentType.Link) {
      const linkReference = ComponentReference.Companion.forLink(change.component.reference.id, change.component.reference.version);
      const updatedStory = this.story!!.withoutChangeToComponentInChapter(this.state.chapter, linkReference);
      this.saveStory(updatedStory)
    }
  }

  moveChangeToPreviousChapter(change: StageChange) {
    if (this.state.chapter > 0) {
      const reference = ComponentReference.Companion.fromReferenceParts(
        change.component.reference.type,
        change.component.reference.id,
        change.component.reference.version
      );


        const updatedStory = this.story!!.withChangeMovedToChapter(
          this.state.chapter,
          reference,
          this.state.chapter - 1
        );
        this.saveStory(updatedStory);
    }
  }

  moveChangeToNextChapter(change: StageChange) {
    const reference = ComponentReference.Companion.fromReferenceParts(
      change.component.reference.type,
      change.component.reference.id,
      change.component.reference.version
    );


      const updatedStory = this.story!!.withChangeMovedToChapter(
        this.state.chapter,
        reference,
        this.state.chapter + 1
      );
      this.saveStory(updatedStory);
  }

  onChangeMove(event: { change: StageChange, fromChapter: number, toChapter: number }) {
    // Validate chapters are within bounds
    if (event.toChapter < 0) {
      console.error("Cannot move to negative chapter index");
      return;
    }

    // Skip if source and destination are the same
    if (event.fromChapter === event.toChapter) {
      return;
    }

    // Create a reference from the change data
    const reference = ComponentReference.Companion.fromReferenceParts(
      event.change.component.reference.type,
      event.change.component.reference.id,
      event.change.component.reference.version
    );

    try {
      // Move the change to the new chapter
      const updatedStory = this.story!!.withChangeMovedToChapter(
        event.fromChapter,
        reference,
        event.toChapter
      );

      // Save the updated story without making HTTP request for immediate UI update
      this.story = updatedStory;
      this.storyDto = Patch.apply(this.storyDto, JSON.parse(dto.toJson(updatedStory)));
      this.emitStoryUpdate();

      // Only persist changes to backend when needed (can be throttled if needed)
      this.persistStory(updatedStory);
    } catch (error) {
      console.error("Could not move change between chapters:", error);
      // No alert for continuous movements to avoid disrupting user experience
    }
  }


}

enum Editor {
  None, Organization, Ledger, Asset
}
