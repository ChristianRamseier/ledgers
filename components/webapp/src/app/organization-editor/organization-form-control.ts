import {FormControl} from '@angular/forms';
import {NewOrganization, Organization} from '../story/story-dto';

export class OrganizationFormControl extends FormControl {

  constructor(
    public id: string | undefined,
    public version: number | undefined,
    public readonly nameControl: FormControl<string>
  ) {
    super({
      name: nameControl
    });
  }

  static createDefault(): OrganizationFormControl {
    return new OrganizationFormControl(
      undefined,
      undefined,
      new FormControl<string>('', {nonNullable: true})
    )
  }

  toOrganization(): Organization | NewOrganization {
    return {
      id: this.id,
      version: this.version,
      name: this.nameControl.value
    }
  }

  setData(organization: Organization | NewOrganization) {
    this.id = (organization as Organization).id
    this.version = (organization as Organization).version
    this.nameControl.setValue(organization.name)
  }
}
