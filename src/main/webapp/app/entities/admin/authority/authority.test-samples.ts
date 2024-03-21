import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'ff9bac3e-615a-447c-825c-5d25ad017591',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b07b0a1b-3b2f-4bc9-9892-880884f39127',
};

export const sampleWithFullData: IAuthority = {
  name: '160c9688-d4e3-490c-a023-3a4f86be686b',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
