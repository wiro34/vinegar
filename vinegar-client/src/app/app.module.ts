import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { IndexComponent } from "./index/index.component";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { ProjectComponent } from "./project/project.component";
import { ProjectListComponent } from "./project/project-list/project-list.component";
import { MessageDispatcher, ProjectRepositoryService, ProjectService } from "./shared";

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    DashboardComponent,
    ProjectComponent,
    ProjectListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
  ],
  providers: [
    ProjectRepositoryService,
    ProjectService,
    MessageDispatcher
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
