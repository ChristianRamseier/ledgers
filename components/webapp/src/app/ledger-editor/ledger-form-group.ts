import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Ledger, NewLedger, Organization} from '../story/story-dto';
import {org} from '../../../public';
import LedgerId = org.ledgers.domain.architecture.LedgerId;

export class LedgerFormGroup extends FormGroup {

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

  static createDefault(): LedgerFormGroup {
    return new LedgerFormGroup(
      undefined,
      undefined,
      new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      new FormControl<string>('', {nonNullable: true, validators: Validators.required})
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

  setData(ledger: Ledger | NewLedger) {
    this.id = (ledger as Ledger).id
    this.version = (ledger as Ledger).version
    this.nameControl.setValue(ledger.name)
    this.ownerIdControl.setValue(ledger.ownerId)
  }

}
