import {AfterViewInit, Component, ElementRef, EventEmitter, HostListener, Input, NgZone, OnChanges, OnInit, Output, SimpleChanges, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CdkDragMove, DragDropModule} from '@angular/cdk/drag-drop';
import {ComponentType, StageChange, Storyline as StorylineModel} from '../story/story-dto';
import {State} from '../state';
import {EditableComponent} from '../common/editable/editable.component';
import {org} from '../../../public';
import ComponentReference = org.ledgers.domain.component.ComponentReference;

interface StorylineItem {
  chapter: number;
  change: StageChange;
  componentId: string;
  displayName: string;
  typeClass: string;
}

@Component({
  selector: 'app-storyline',
  standalone: true,
  imports: [CommonModule, DragDropModule, EditableComponent],
  templateUrl: './storyline.component.html',
  styleUrl: './storyline.component.scss'
})
export class StorylineComponent implements OnInit, OnChanges, AfterViewInit {
  @Input() storyline: StorylineModel | null = null;
  @Input() story: any | null = null;
  @Input() state: State = { chapter: 0, step: 0 };

  @Output() onChapterChange = new EventEmitter<number>();
  @Output() onChapterNameChange = new EventEmitter<{ chapter: number, name: string }>();
  @Output() onChangeMove = new EventEmitter<{ change: StageChange, fromChapter: number, toChapter: number }>();

  @ViewChild('storylineContainer') storylineContainer!: ElementRef;

  storylineItems: StorylineItem[] = [];
  availableChapters: number[] = []; // All chapters from the domain model
  displayedChapters: number[] = []; // Only the currently visible chapters
  changesByComponentId: Map<string, StorylineItem[]> = new Map();
  chapterWidth = 20; // Narrow chapter columns
  chapterHeight = 30; // Fixed height for component rows
  currentDrag: {
    item: StorylineItem;
    startChapter: number;
    startX: number;
    pendingChapter?: number;
  } | undefined = undefined;

  // Viewport management
  viewportWidth = 0;
  labelsColumnWidth = 150; // Width of the component labels column
  visibleChapterCount = 0;
  visibleStartChapter = 0;

  constructor(private ngZone: NgZone) {}

  ngOnInit() {
    this.initializeStoryline();
  }

  ngAfterViewInit() {
    // Initialize after view is ready
    setTimeout(() => {
      this.calculateVisibleChapters();
    }, 0);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['storyline'] || changes['story']) {
      this.initializeStoryline();
      setTimeout(() => {
        this.calculateVisibleChapters();
      }, 0);
    }
  }

  @HostListener('window:resize')
  onResize() {
    this.calculateVisibleChapters();
  }

  initializeStoryline() {
    if (!this.storyline || !this.story) return;

    this.storylineItems = [];
    this.changesByComponentId = new Map();

    // Get all chapters from the domain model
    const totalChapters = this.storyline.chapters.length;
    this.availableChapters = Array.from({ length: totalChapters }, (_, i) => i);

    // Generate storyline items
    this.storyline.chapters.forEach((chapter, chapterIndex) => {
      if (!chapter.changes) return;

      chapter.changes.forEach(change => {
        const componentId = `${change.component.reference.type}-${change.component.reference.id}`;
        const reference = ComponentReference.Companion.fromReferenceParts(
          change.component.reference.type,
          change.component.reference.id,
          change.component.reference.version
        );

        const displayName = this.story.getComponentDisplayName(reference, chapterIndex);

        const storylineItem: StorylineItem = {
          chapter: chapterIndex,
          change,
          componentId,
          displayName,
          typeClass: this.getTypeClass(change)
        };

        this.storylineItems.push(storylineItem);

        // Group storyline items by component for drawing connecting lines
        if (!this.changesByComponentId.has(componentId)) {
          this.changesByComponentId.set(componentId, []);
        }
        this.changesByComponentId.get(componentId)?.push(storylineItem);
      });
    });

    // Sort each component's storyline items by chapter
    this.changesByComponentId.forEach(items => {
      items.sort((a, b) => a.chapter - b.chapter);
    });
  }

  getTypeClass(change: StageChange): string {
    const baseClass = change.type.toLowerCase();

    // Add component-specific styling
    switch (change.component.reference.type) {
      case ComponentType.Ledger:
        return `${baseClass} ledger-change`;
      case ComponentType.Organization:
        return `${baseClass} organization-change`;
      case ComponentType.Asset:
        return `${baseClass} asset-change`;
      case ComponentType.Link:
        return `${baseClass} link-change`;
      default:
        return baseClass;
    }
  }

  // Find the first Add change for a component
  findFirstAddChange(items: StorylineItem[]): StorylineItem | null {
    return items.find(item => item.change.type === 'Add') || null;
  }

  // Find the last Remove change for a component
  findLastRemoveChange(items: StorylineItem[]): StorylineItem | null {
    return [...items].reverse().find(item => item.change.type === 'Remove') || null;
  }

  // Calculate how many chapters can fit in the viewport
  calculateVisibleChapters() {
    if (!this.storylineContainer || !this.availableChapters.length) return;

    const container = this.storylineContainer.nativeElement;
    this.viewportWidth = container.clientWidth;

    // Calculate available width for chapters (total width minus labels column)
    const availableWidth = Math.max(0, this.viewportWidth - this.labelsColumnWidth);

    // Calculate how many chapters can fit in the available width
    this.visibleChapterCount = Math.max(1, Math.floor(availableWidth / this.chapterWidth));

    // Ensure current chapter is visible
    if (this.state.chapter < this.visibleStartChapter) {
      this.visibleStartChapter = this.state.chapter;
    } else if (this.state.chapter >= this.visibleStartChapter + this.visibleChapterCount) {
      this.visibleStartChapter = Math.max(0, this.state.chapter - this.visibleChapterCount + 1);
    }

    // Ensure we don't go beyond available chapters
    const maxStartChapter = Math.max(0, this.availableChapters.length - this.visibleChapterCount);
    this.visibleStartChapter = Math.min(this.visibleStartChapter, maxStartChapter);

    // Update displayed chapters
    const endChapter = Math.min(this.visibleStartChapter + this.visibleChapterCount, this.availableChapters.length);
    this.displayedChapters = this.availableChapters.slice(this.visibleStartChapter, endChapter);
  }

  // Get the right edge position for the storyline
  getRightEdgePosition(): number {
    // Return the rightmost position of the visible area
    return (this.visibleStartChapter + this.visibleChapterCount) * this.chapterWidth;
  }

  // Get line connection coordinates for a component
  getLineCoordinates(componentId: string): { startX: number, endX: number, centerY: number } {
    const items = this.changesByComponentId.get(componentId) || [];

    if (items.length === 0) {
      return { startX: 0, endX: 0, centerY: this.chapterHeight / 2 };
    }

    const addChange = this.findFirstAddChange(items);
    const removeChange = this.findLastRemoveChange(items);

    // Start at the Add change or at the first change if no Add is found
    let startX = addChange
      ? (addChange.chapter * this.chapterWidth) + (this.chapterWidth / 2)
      : (items[0].chapter * this.chapterWidth) + (this.chapterWidth / 2);

    // Adjust for current viewport
    startX -= (this.visibleStartChapter * this.chapterWidth);

    // End at the Remove change or at the right edge if no Remove is found
    let endX;
    if (removeChange) {
      // Adjust for current viewport
      endX = (removeChange.chapter * this.chapterWidth) + (this.chapterWidth / 2) - (this.visibleStartChapter * this.chapterWidth);
    } else {
      // Extend to the right edge of the visible area
      endX = this.visibleChapterCount * this.chapterWidth;
    }

    // Constrain within visible area
    startX = Math.max(0, startX);
    endX = Math.min(endX, this.visibleChapterCount * this.chapterWidth);

    // If both points are outside visible area but span across it, make line go across the entire visible area
    if (addChange && removeChange) {
      if (addChange.chapter < this.visibleStartChapter && removeChange.chapter >= this.visibleStartChapter + this.visibleChapterCount) {
        startX = 0;
        endX = this.visibleChapterCount * this.chapterWidth;
      }
    }

    return {
      startX,
      endX,
      centerY: this.chapterHeight / 2
    };
  }

  selectChapter(chapter: number) {
    if (chapter !== this.state.chapter) {
      this.onChapterChange.emit(chapter);

      // Ensure selected chapter is visible
      if (chapter < this.visibleStartChapter || chapter >= this.visibleStartChapter + this.visibleChapterCount) {
        // If outside visible range, recenter the view
        this.visibleStartChapter = Math.max(0, chapter - Math.floor(this.visibleChapterCount / 2));
        this.calculateVisibleChapters();
      }
    }
  }

  getChapterNameLabel(): string {
    return `Chapter ${this.state.chapter + 1}${this.getCurrentChapterName() ? ' -' : ''}`;
  }

  getCurrentChapterName(): string {
    if (!this.storyline || this.state.chapter >= this.storyline.chapters.length) {
      return '';
    }
    return this.storyline.chapters[this.state.chapter].name || '';
  }

  updateChapterName(newName: string) {
    this.onChapterNameChange.emit({ chapter: this.state.chapter, name: newName });
  }

  onDragStarted(item: StorylineItem, event: any) {
    // Store the item being dragged and its starting position
    this.currentDrag = {
      item: item,
      startChapter: item.chapter,
      startX: (item.chapter - this.visibleStartChapter) * this.chapterWidth
    };
    console.log('Drag started:', {
      startChapter: item.chapter,
      startX: this.currentDrag.startX,
      chapterWidth: this.chapterWidth
    });
  }

  onDragMoved(event: CdkDragMove) {
    if (!this.currentDrag) return;

    // Get the drag distance from the CDK
    const dragTransform = event.source.getFreeDragPosition();
    
    // Calculate chapter offset based on horizontal drag distance
    const chaptersMoved = Math.round(dragTransform.x / this.chapterWidth);
    const newChapter = Math.max(0, Math.min(this.currentDrag.startChapter + chaptersMoved, this.availableChapters.length - 1));

    console.log('Drag moved:', {
      dragTransformX: dragTransform.x,
      chaptersMoved: chaptersMoved,
      startChapter: this.currentDrag.startChapter,
      newChapter: newChapter,
      currentItemChapter: this.currentDrag.item.chapter
    });

    // Auto-scroll when dragging near viewport edges
    const containerRect = this.storylineContainer.nativeElement.getBoundingClientRect();
    const mouseX = event.pointerPosition.x - containerRect.left;
    const storylineX = mouseX - this.labelsColumnWidth;
    const edgeThreshold = 50;

    if (storylineX < edgeThreshold && this.visibleStartChapter > 0) {
      this.visibleStartChapter = Math.max(0, this.visibleStartChapter - 1);
      this.calculateVisibleChapters();
    } else if (storylineX > (this.visibleChapterCount * this.chapterWidth - edgeThreshold) &&
               this.visibleStartChapter + this.visibleChapterCount < this.availableChapters.length) {
      this.visibleStartChapter++;
      this.calculateVisibleChapters();
    }

    // Store the pending chapter change without updating the visual position
    // Let CDK drag handle the visual movement to avoid double movement
    if (newChapter !== (this.currentDrag.pendingChapter ?? this.currentDrag.startChapter)) {
      console.log('Setting pending chapter move:', {
        fromChapter: this.currentDrag.startChapter,
        toChapter: newChapter
      });
      
      // Only store the pending change, don't update item.chapter during drag
      this.currentDrag.pendingChapter = newChapter;
    }
  }

  onDragEnded(event: any) {
    if (this.currentDrag) {
      console.log('Drag ended:', {
        finalChapter: this.currentDrag.item.chapter,
        startChapter: this.currentDrag.startChapter,
        pendingChapter: this.currentDrag.pendingChapter
      });
      
      // Now emit the final model update after drag is complete
      if (this.currentDrag.pendingChapter !== undefined && 
          this.currentDrag.pendingChapter !== this.currentDrag.startChapter) {
        // Update the item's chapter to the final position
        this.currentDrag.item.chapter = this.currentDrag.pendingChapter;
        
        this.ngZone.run(() => {
          this.onChangeMove.emit({
            change: this.currentDrag!.item.change,
            fromChapter: this.currentDrag!.startChapter,
            toChapter: this.currentDrag!.pendingChapter!
          });
        });
      }
      
      // Reset the CDK drag transform to snap back to grid position
      event.source.reset();
      this.currentDrag = undefined;
    }
  }

  isCurrentChapter(chapter: number): boolean {
    return chapter === this.state.chapter;
  }

  getChapterPosition(chapter: number): string {
    // Adjust for the current viewport
    const adjustedPosition = (chapter - this.visibleStartChapter) * this.chapterWidth;
    return `${adjustedPosition}px`;
  }

  // Check if an item is within the visible range
  isItemVisible(item: StorylineItem): boolean {
    return item.chapter >= this.visibleStartChapter &&
           item.chapter < this.visibleStartChapter + this.visibleChapterCount;
  }

  // Navigate viewport
  navigateLeft() {
    if (this.visibleStartChapter > 0) {
      this.visibleStartChapter = Math.max(0, this.visibleStartChapter - Math.floor(this.visibleChapterCount / 2));
      this.calculateVisibleChapters();
    }
  }

  navigateRight() {
    const maxStart = Math.max(0, this.availableChapters.length - this.visibleChapterCount);
    if (this.visibleStartChapter < maxStart) {
      this.visibleStartChapter = Math.min(maxStart,
                                         this.visibleStartChapter + Math.floor(this.visibleChapterCount / 2));
      this.calculateVisibleChapters();
    }
  }

}
