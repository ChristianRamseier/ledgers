
export interface StoryDto {
  id: string,
  name: string,
  architecture: Architecture,
  storyline: Storyline
}

export interface Architecture {
  organizations: Organization[]
  ledgers: Ledger[]
  assets: Asset[]
  links: Link[]
}

export interface Storyline {
  chapters: Chapter[]
}

export interface Chapter {
  name: string,
  changes: StageChange[],
  scenario: Scenario
}

export interface StageChange {
  componentReference: ComponentReference
  type: StageChangeType
}

export interface ComponentReference {
  type: ComponentType,
  id: string,
  version: number
}

export enum ComponentType {
  Ledger = 'Ledger',
  Organization = 'Organization',
  Asset = 'Asset',
  Link = 'Link'
}

export enum StageChangeType {
  Add = 'Add', Change = 'Change', Remove = 'Remove'
}


export interface Scenario {

}

export interface NewOrganization {
  name: string
}

export interface Organization extends NewOrganization {
  id: string,
  version: number
}

export interface NewAsset {
  name: string,
  assetType: string
}

export interface Asset extends NewAsset {
  id: string,
  version: number
}

export interface NewLedger {
  name: string,
  ownerId: string
}

export interface Ledger extends NewLedger {
  id: string,
  version: number
}

export interface Asset {
  id: string,
  version: number,
  name: string,
  assetType: string
}

export interface Link {
  id: string,
  version: number,
  from: string,
  to: string
}


