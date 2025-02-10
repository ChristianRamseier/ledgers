import {FormControl} from '@angular/forms';
import {NewAsset, Asset} from '../story/story-dto';

export class AssetFormControl extends FormControl {

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

  static createDefault(): AssetFormControl {
    return new AssetFormControl(
      undefined,
      undefined,
      new FormControl<string>('', {nonNullable: true}),
      new FormControl<string>('', {nonNullable: true}),
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
