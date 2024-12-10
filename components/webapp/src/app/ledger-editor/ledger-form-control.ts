import {FormControl} from '@angular/forms';
import {Ledger, NewLedger, Organization} from '../story/story-dto';

export class LedgerFormControl extends FormControl {

  constructor(
    public id: string | undefined,
    public version: number | undefined,
    public readonly nameControl: FormControl<string>,
    public readonly ownerIdControl: FormControl<string>
  ) {
    super({
      name: nameControl,
      ownerId: ownerIdControl
    });
  }

  static createDefault(): LedgerFormControl {
    return new LedgerFormControl(
      undefined,
      undefined,
      new FormControl<string>('', {nonNullable: true}),
      new FormControl<string>('', {nonNullable: true})
    )
  }

  toLedger(): Ledger | NewLedger {
    return {
      id: this.id,
      version: this.version,
      name: this.nameControl.value,
      ownerId: this.ownerIdControl.value
    }
  }

  setData(organization: Ledger | NewLedger) {
    this.id = (organization as Organization).id
    this.version = (organization as Organization).version
    this.nameControl.setValue(organization.name)
    this.ownerIdControl.setValue(organization.ownerId)
  }

}
