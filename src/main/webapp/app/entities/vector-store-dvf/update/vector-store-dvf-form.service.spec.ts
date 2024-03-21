import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vector-store-dvf.test-samples';

import { VectorStoreDvfFormService } from './vector-store-dvf-form.service';

describe('VectorStoreDvf Form Service', () => {
  let service: VectorStoreDvfFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VectorStoreDvfFormService);
  });

  describe('Service methods', () => {
    describe('createVectorStoreDvfFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVectorStoreDvfFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            metadata: expect.any(Object),
            commune: expect.any(Object),
            codePostal: expect.any(Object),
            departement: expect.any(Object),
            codeDepartement: expect.any(Object),
            typeLocal: expect.any(Object),
            superficieCarrez: expect.any(Object),
            superficieTerrain: expect.any(Object),
            nbPieces: expect.any(Object),
            dateVente: expect.any(Object),
            valeur: expect.any(Object),
          }),
        );
      });

      it('passing IVectorStoreDvf should create a new form with FormGroup', () => {
        const formGroup = service.createVectorStoreDvfFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            metadata: expect.any(Object),
            commune: expect.any(Object),
            codePostal: expect.any(Object),
            departement: expect.any(Object),
            codeDepartement: expect.any(Object),
            typeLocal: expect.any(Object),
            superficieCarrez: expect.any(Object),
            superficieTerrain: expect.any(Object),
            nbPieces: expect.any(Object),
            dateVente: expect.any(Object),
            valeur: expect.any(Object),
          }),
        );
      });
    });

    describe('getVectorStoreDvf', () => {
      it('should return NewVectorStoreDvf for default VectorStoreDvf initial value', () => {
        const formGroup = service.createVectorStoreDvfFormGroup(sampleWithNewData);

        const vectorStore = service.getVectorStoreDvf(formGroup) as any;

        expect(vectorStore).toMatchObject(sampleWithNewData);
      });

      it('should return NewVectorStoreDvf for empty VectorStoreDvf initial value', () => {
        const formGroup = service.createVectorStoreDvfFormGroup();

        const vectorStore = service.getVectorStoreDvf(formGroup) as any;

        expect(vectorStore).toMatchObject({});
      });

      it('should return IVectorStoreDvf', () => {
        const formGroup = service.createVectorStoreDvfFormGroup(sampleWithRequiredData);

        const vectorStore = service.getVectorStoreDvf(formGroup) as any;

        expect(vectorStore).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVectorStoreDvf should not enable id FormControl', () => {
        const formGroup = service.createVectorStoreDvfFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVectorStoreDvf should disable id FormControl', () => {
        const formGroup = service.createVectorStoreDvfFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
