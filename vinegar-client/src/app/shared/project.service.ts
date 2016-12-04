import { Injectable } from "@angular/core";
import { Observable, BehaviorSubject } from "rxjs/Rx";
import * as _ from "lodash";
import { Project } from "./project.model";
import { ProjectRepositoryService } from "./project-repository.service";
import { ProjectMessages, CreateProject, UpdateProject } from "./project.messages";
import { MessageDispatcher } from "./message-dispatcher.service";

class ProjectState {
  constructor(public projects: Project[] = []) {
  }
}

@Injectable()
export class ProjectService {

  private stateSubject$: BehaviorSubject<ProjectState>;

  constructor(private projectRepository: ProjectRepositoryService,
              private dispatcher$: MessageDispatcher<ProjectMessages>) {

    this.stateSubject$ = new BehaviorSubject<ProjectState>(new ProjectState(projectRepository.all()));

    Observable.zip(
      ProjectService.dispatchMessage([], dispatcher$),
      (projects) => ({projects: projects} as ProjectState))
      .subscribe(appState => {
        console.log(appState);
        this.stateSubject$.next(appState);
      });
  }

  public create(project: Project) {
    this.dispatcher$.next(new CreateProject(project));
  }

  public update(project: Project) {
    this.dispatcher$.next(new UpdateProject(project));
  }

  private get returner$() {
    return this.stateSubject$.asObservable().map(projects => _.cloneDeep(projects));
  }

  get state$() {
    return this.returner$;
  }

  get projects$() {
    return this.returner$.map(_ => _.projects);
  }

  private static dispatchMessage(initHeroes: Project[], dispatcher$: Observable<ProjectMessages>): Observable<Project[]> {
    return dispatcher$.scan((projects: Project[], message: ProjectMessages) => {
      if (message instanceof UpdateProject) {
        projects = _.uniqBy([message.project, ...projects], 'id');
        // } else if (action instanceof AddHero) {
        //   const newHero = action.hero;
        //   heroes = lodash.uniqBy([newHero, ...heroes], 'id');
        // } else if (action instanceof DeleteHero) {
        //   const deleteId = action.id;
        //   heroes = lodash.reject(heroes, { id: deleteId });
      }
      return _.orderBy(projects, ['id'], ['asc']);
    }, []);
  }
}

