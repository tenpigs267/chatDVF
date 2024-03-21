import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVectorStoreDvf, NewVectorStoreDvf } from '../vector-store-dvf.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVectorStoreDvf for edit and NewVectorStoreDvfFormGroupInput for create.
 */
type VectorStoreDvfFormGroupInput = IVectorStoreDvf | PartialWithRequiredKeyOf<NewVectorStoreDvf>;

type VectorStoreDvfFormDefaults = Pick<NewVectorStoreDvf, 'id'>;

type VectorStoreDvfFormGroupContent = {
  id: FormControl<IVectorStoreDvf['id'] | NewVectorStoreDvf['id']>;
  content: FormControl<IVectorStoreDvf['content']>;
  metadata: FormControl<IVectorStoreDvf['metadata']>;
  commune: FormControl<IVectorStoreDvf['commune']>;
  codePostal: FormControl<IVectorStoreDvf['codePostal']>;
  departement: FormControl<IVectorStoreDvf['departement']>;
  codeDepartement: FormControl<IVectorStoreDvf['codeDepartement']>;
  typeLocal: FormControl<IVectorStoreDvf['typeLocal']>;
  superficieCarrez: FormControl<IVectorStoreDvf['superficieCarrez']>;
  superficieTerrain: FormControl<IVectorStoreDvf['superficieTerrain']>;
  nbPieces: FormControl<IVectorStoreDvf['nbPieces']>;
  dateVente: FormControl<IVectorStoreDvf['dateVente']>;
  valeur: FormControl<IVectorStoreDvf['valeur']>;
};

export type VectorStoreDvfFormGroup = FormGroup<VectorStoreDvfFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VectorStoreDvfFormService {
  createVectorStoreDvfFormGroup(vectorStore: VectorStoreDvfFormGroupInput = { id: null }): VectorStoreDvfFormGroup {
    const vectorStoreRawValue = {
      ...this.getFormDefaults(),
      ...vectorStore,
    };
    return new FormGroup<VectorStoreDvfFormGroupContent>({
      id: new FormControl(
        { value: vectorStoreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(vectorStoreRawValue.content),
      metadata: new FormControl(vectorStoreRawValue.metadata),
      commune: new FormControl(vectorStoreRawValue.commune),
      codePostal: new FormControl(vectorStoreRawValue.codePostal),
      departement: new FormControl(vectorStoreRawValue.departement),
      codeDepartement: new FormControl(vectorStoreRawValue.codeDepartement),
      typeLocal: new FormControl(vectorStoreRawValue.typeLocal),
      superficieCarrez: new FormControl(vectorStoreRawValue.superficieCarrez),
      superficieTerrain: new FormControl(vectorStoreRawValue.superficieTerrain),
      nbPieces: new FormControl(vectorStoreRawValue.nbPieces),
      dateVente: new FormControl(vectorStoreRawValue.dateVente),
      valeur: new FormControl(vectorStoreRawValue.valeur),
    });
  }

  getVectorStoreDvf(form: VectorStoreDvfFormGroup): IVectorStoreDvf | NewVectorStoreDvf {
    return form.getRawValue() as IVectorStoreDvf | NewVectorStoreDvf;
  }

  resetForm(form: VectorStoreDvfFormGroup, vectorStore: VectorStoreDvfFormGroupInput): void {
    const vectorStoreRawValue = { ...this.getFormDefaults(), ...vectorStore };
    form.reset(
      {
        ...vectorStoreRawValue,
        id: { value: vectorStoreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VectorStoreDvfFormDefaults {
    return {
      id: null,
    };
  }
}
