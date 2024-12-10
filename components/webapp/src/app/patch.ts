import * as jsonpatch from 'fast-json-patch';

export class Patch {
  static apply<T>(existing: T, updated: T) : T {
    if (!existing || !updated) {
      return updated
    }
    const delta = jsonpatch.compare(existing, updated);
    jsonpatch.applyPatch(existing, delta);
    return existing
  }
}
