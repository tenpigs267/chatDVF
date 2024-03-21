import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VectorStoreDvfComponent } from './list/vector-store-dvf.component';
import { VectorStoreDvfDetailComponent } from './detail/vector-store-dvf-detail.component';
import { VectorStoreDvfUpdateComponent } from './update/vector-store-dvf-update.component';
import VectorStoreDvfResolve from './route/vector-store-dvf-routing-resolve.service';

const vectorStoreRoute: Routes = [
  {
    path: '',
    component: VectorStoreDvfComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VectorStoreDvfDetailComponent,
    resolve: {
      vectorStore: VectorStoreDvfResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VectorStoreDvfUpdateComponent,
    resolve: {
      vectorStore: VectorStoreDvfResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VectorStoreDvfUpdateComponent,
    resolve: {
      vectorStore: VectorStoreDvfResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vectorStoreRoute;
