import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 25336,
  login: 'tq@F4Uo\\bBkA',
};

export const sampleWithPartialData: IUser = {
  id: 28541,
  login: '1QIy9a',
};

export const sampleWithFullData: IUser = {
  id: 11844,
  login: 'QXI@VG',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
