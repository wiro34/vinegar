import { Component, OnInit } from "@angular/core";
import { ProjectService, Project } from "../../shared";

@Component({
  selector: 'app-project-list',
  template: `
    <div>
      <ul class="list-group">
        <li *ngFor="let project of projects | async" class="list-group-item">
          <button class="btn btn-outline-primary btn-sm" (click)="edit(project)">Edit</button>
          <!--<button class="btn btn-outline-warning btn-sm" (click)="deleteHero(project)">Delete</button>-->
          <span>id: {{project.id}} / name: {{project.name}}</span>
        </li>
      </ul>
    </div>`,
  styleUrls: ['./project-list.component.scss']
})
export class ProjectListComponent implements OnInit {

  constructor(public projectService: ProjectService) { }

  ngOnInit() {
  }

  public edit(project) {
    project.name = `[edited] ${project.name}`;
    this.projectService.update(project);
  }

  get projects() {
    return this.projectService.projects$;
  }

}
