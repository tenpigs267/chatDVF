import dayjs from 'dayjs/esm';

export interface IVectorStoreDvf {
  id: string;
  content?: string | null;
  metadata?: string | null;
  commune?: string | null;
  codePostal?: number | null;
  departement?: string | null;
  codeDepartement?: number | null;
  typeLocal?: string | null;
  superficieCarrez?: number | null;
  superficieTerrain?: number | null;
  nbPieces?: number | null;
  dateVente?: dayjs.Dayjs | null;
  valeur?: number | null;
}

export type NewVectorStoreDvf = Omit<IVectorStoreDvf, 'id'> & { id: null };
