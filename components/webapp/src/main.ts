import {createApplication} from '@angular/platform-browser';
import {createCustomElement} from "@angular/elements";
import {EditableComponent} from './app/editable/editable.component';

(async () => {
  const app = await createApplication({
    providers: []
  });

  const elements = [
    {name: 'editable-element', component: EditableComponent}
  ]

  elements.forEach(element => {
    const editableElement = createCustomElement(element.component, {
      injector: app.injector,
    });
    customElements.define(element.name, editableElement);
  })

  console.log('Components registered: '+elements.map(e => e.name).join(', '))
})();
