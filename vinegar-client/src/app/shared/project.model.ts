export class Project {
  get id(): number {
    return this._id;
  }

  constructor(private _id: number,
              public name: string) {
  }
}
