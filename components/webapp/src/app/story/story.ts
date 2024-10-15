export interface Story {
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

export interface Organization {
  id: string,
  version: number,
  name: string
}

export interface Ledger {
  id: string,
  version: number,
  name: string,
  ownerId: string
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


