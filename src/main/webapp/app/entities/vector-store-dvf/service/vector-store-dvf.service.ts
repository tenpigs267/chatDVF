import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVectorStoreDvf, NewVectorStoreDvf } from '../vector-store-dvf.model';

export type PartialUpdateVectorStoreDvf = Partial<IVectorStoreDvf> & Pick<IVectorStoreDvf, 'id'>;

type RestOf<T extends IVectorStoreDvf | NewVectorStoreDvf> = Omit<T, 'dateVente'> & {
  dateVente?: string | null;
};

export type RestVectorStoreDvf = RestOf<IVectorStoreDvf>;

export type NewRestVectorStoreDvf = RestOf<NewVectorStoreDvf>;

export type PartialUpdateRestVectorStoreDvf = RestOf<PartialUpdateVectorStoreDvf>;

export type EntityResponseType = HttpResponse<IVectorStoreDvf>;
export type EntityArrayResponseType = HttpResponse<IVectorStoreDvf[]>;

@Injectable({ providedIn: 'root' })
export class VectorStoreDvfService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vector-stores');

  create(vectorStore: NewVectorStoreDvf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vectorStore);
    return this.http
      .post<RestVectorStoreDvf>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vectorStore: IVectorStoreDvf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vectorStore);
    return this.http
      .put<RestVectorStoreDvf>(`${this.resourceUrl}/${this.getVectorStoreDvfIdentifier(vectorStore)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vectorStore: PartialUpdateVectorStoreDvf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vectorStore);
    return this.http
      .patch<RestVectorStoreDvf>(`${this.resourceUrl}/${this.getVectorStoreDvfIdentifier(vectorStore)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestVectorStoreDvf>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVectorStoreDvf[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVectorStoreDvfIdentifier(vectorStore: Pick<IVectorStoreDvf, 'id'>): string {
    return vectorStore.id;
  }

  compareVectorStoreDvf(o1: Pick<IVectorStoreDvf, 'id'> | null, o2: Pick<IVectorStoreDvf, 'id'> | null): boolean {
    return o1 && o2 ? this.getVectorStoreDvfIdentifier(o1) === this.getVectorStoreDvfIdentifier(o2) : o1 === o2;
  }

  addVectorStoreDvfToCollectionIfMissing<Type extends Pick<IVectorStoreDvf, 'id'>>(
    vectorStoreCollection: Type[],
    ...vectorStoresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vectorStores: Type[] = vectorStoresToCheck.filter(isPresent);
    if (vectorStores.length > 0) {
      const vectorStoreCollectionIdentifiers = vectorStoreCollection.map(vectorStoreItem =>
        this.getVectorStoreDvfIdentifier(vectorStoreItem),
      );
      const vectorStoresToAdd = vectorStores.filter(vectorStoreItem => {
        const vectorStoreIdentifier = this.getVectorStoreDvfIdentifier(vectorStoreItem);
        if (vectorStoreCollectionIdentifiers.includes(vectorStoreIdentifier)) {
          return false;
        }
        vectorStoreCollectionIdentifiers.push(vectorStoreIdentifier);
        return true;
      });
      return [...vectorStoresToAdd, ...vectorStoreCollection];
    }
    return vectorStoreCollection;
  }

  protected convertDateFromClient<T extends IVectorStoreDvf | NewVectorStoreDvf | PartialUpdateVectorStoreDvf>(vectorStore: T): RestOf<T> {
    return {
      ...vectorStore,
      dateVente: vectorStore.dateVente?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVectorStoreDvf: RestVectorStoreDvf): IVectorStoreDvf {
    return {
      ...restVectorStoreDvf,
      dateVente: restVectorStoreDvf.dateVente ? dayjs(restVectorStoreDvf.dateVente) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVectorStoreDvf>): HttpResponse<IVectorStoreDvf> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVectorStoreDvf[]>): HttpResponse<IVectorStoreDvf[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  addDVFForDept(codeDpt: number) {
    return this.http.get<RestVectorStoreDvf>(`${this.resourceUrl}/adddvf/${codeDpt}`, { observe: 'response' }).subscribe();
  }
}
