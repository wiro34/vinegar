import { Project } from "./project.model";

export class CreateProject {
  constructor(public project: Project) {}
}

export class UpdateProject {
  constructor(public project: Project) {}
}

export type ProjectMessages = CreateProject | UpdateProject;
