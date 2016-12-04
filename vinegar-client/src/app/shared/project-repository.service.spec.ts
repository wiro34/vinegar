/* tslint:disable:no-unused-variable */

import {TestBed, async, inject} from '@angular/core/testing';
import {ProjectRepositoryService} from './project-repository.service';
import {Project} from "./project.model";

describe('Service: ProjectRepository', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProjectRepositoryService]
    });
  });

  describe('#all', () => {
    it('should return all projects', inject([ProjectRepositoryService], (service: ProjectRepositoryService) => {
      expect(service.all()).toEqual([
        new Project(1, 'Endeavour OLSU')
      ]);
    }));
  });

  describe('#update', () => {
    it('should update project', inject([ProjectRepositoryService], (service: ProjectRepositoryService) => {
      const project = new Project(1, 'test');
      project.name = 'hoge';
      expect(service.update(project)).toEqual(true);
    }));
  });
});
