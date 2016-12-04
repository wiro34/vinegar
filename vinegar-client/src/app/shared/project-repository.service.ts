import {Injectable} from '@angular/core';
import {Project} from "./project.model";

@Injectable()
export class ProjectRepositoryService {

  constructor() {
  }

  public all(): Project[] {
    return initialProjects;
  }

  public update(project: Project): boolean {
    return true;
  }

}

const initialProjects: Project[] = [
  new Project(1, 'Endeavour OLSU'),
];
