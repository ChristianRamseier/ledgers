import {createApplication} from '@angular/platform-browser';
import {createCustomElement} from "@angular/elements";
import {EditableComponent} from './app/common/editable/editable.component';
import {PanelsComponent} from './app/common/panels/panels.component';
import {PanelComponent} from './app/common/panel/panel.component';
import {PanelEntryComponent} from './app/common/panel-entry/panel-entry.component';
import {StoryComponent} from './app/story/story.component';
import {provideHttpClient} from '@angular/common/http';

(async () => {

  const app = await createApplication({
    providers: [provideHttpClient()]
  });

  const elements = [
    {name: 'editable-element', component: EditableComponent},
    {name: 'panels-element', component: PanelsComponent},
    {name: 'panel-element', component: PanelComponent},
    {name: 'panel-entry-element', component: PanelEntryComponent},
    {name: 'story-element', component: StoryComponent}
  ]

  elements.forEach(element => {
    const editableElement = createCustomElement(element.component, {
      injector: app.injector,
    });
    customElements.define(element.name, editableElement);
  })

  console.log('Components registered: ' + elements.map(e => e.name).join(', '))

})();
