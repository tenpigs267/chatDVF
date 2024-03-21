import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVectorStoreDvf } from '../vector-store-dvf.model';
import { VectorStoreDvfService } from '../service/vector-store-dvf.service';

const vectorStoreResolve = (route: ActivatedRouteSnapshot): Observable<null | IVectorStoreDvf> => {
  const id = route.params['id'];
  if (id) {
    return inject(VectorStoreDvfService)
      .find(id)
      .pipe(
        mergeMap((vectorStore: HttpResponse<IVectorStoreDvf>) => {
          if (vectorStore.body) {
            return of(vectorStore.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default vectorStoreResolve;
