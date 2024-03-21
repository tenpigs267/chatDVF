import dayjs from 'dayjs/esm';

import { IVectorStoreDvf, NewVectorStoreDvf } from './vector-store-dvf.model';

export const sampleWithRequiredData: IVectorStoreDvf = {
  id: 'b14a2825-fbff-4571-b64b-9bb10b6ad5e2',
};

export const sampleWithPartialData: IVectorStoreDvf = {
  id: '82d1e441-86f4-4bf7-ba7c-1588370a69cd',
  content: 'longtemps triathlète contre',
  codePostal: 21423,
  superficieCarrez: 10333,
  nbPieces: 12768,
  dateVente: dayjs('2024-03-21'),
};

export const sampleWithFullData: IVectorStoreDvf = {
  id: 'c3be5e2f-2ddd-4227-b4f2-7f2ea8d09f12',
  content: 'malade parmi collègue',
  metadata: '../fake-data/blob/hipster.txt',
  commune: 'jusqu’à ce que',
  codePostal: 25779,
  departement: 'collègue alors que oups',
  codeDepartement: 25473,
  typeLocal: 'personnel professionnel',
  superficieCarrez: 31807,
  superficieTerrain: 9812,
  nbPieces: 21559,
  dateVente: dayjs('2024-03-21'),
  valeur: 929.63,
};

export const sampleWithNewData: NewVectorStoreDvf = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
