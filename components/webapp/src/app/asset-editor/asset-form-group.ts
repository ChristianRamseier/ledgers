import {FormControl, FormGroup, Validators} from '@angular/forms';
import {NewAsset, Asset} from '../story/story-dto';

export class AssetFormGroup extends FormGroup {

  constructor(
    public id: string | undefined,
    public version: number | undefined,
    public readonly nameControl: FormControl<string>,
    public readonly assetTypeControl: FormControl<string>
  ) {
    super({
      name: nameControl,
      assetType: assetTypeControl
    });
  }

  static createDefault(): AssetFormGroup {
    return new AssetFormGroup(
      undefined,
      undefined,
      new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
    )
  }

  toAsset(): NewAsset | Asset {
    return {
      id: this.id,
      version: this.version,
      name: this.nameControl.value,
      assetType: this.assetTypeControl.value
    }
  }

  setData(asset: Asset | NewAsset) {
    this.id = (asset as Asset).id
    this.version = (asset as Asset).version
    this.nameControl.setValue(asset.name)
    this.assetTypeControl.setValue(asset.assetType)
  }

}
