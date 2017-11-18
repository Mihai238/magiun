import { RouterModule } from '@angular/router';

import { DataComponent } from './components/data/data.component';
import { AboutComponent } from './components/about/about.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { WorkflowsComponent } from "./components/workflows/workflows.component";

export const routing = RouterModule.forRoot([
  {path: '', component: DataComponent},
  {path: 'data', component: DataComponent},
  {path: 'workflows', component: WorkflowsComponent},
  {path: 'about', component: AboutComponent},
  {path: '**', component: PageNotFoundComponent},
]);
