import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VectorStoreDvfService } from '../service/vector-store-dvf.service';
import { IVectorStoreDvf } from '../vector-store-dvf.model';
import { VectorStoreDvfFormService } from './vector-store-dvf-form.service';

import { VectorStoreDvfUpdateComponent } from './vector-store-dvf-update.component';

describe('VectorStoreDvf Management Update Component', () => {
  let comp: VectorStoreDvfUpdateComponent;
  let fixture: ComponentFixture<VectorStoreDvfUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vectorStoreFormService: VectorStoreDvfFormService;
  let vectorStoreService: VectorStoreDvfService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), VectorStoreDvfUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VectorStoreDvfUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VectorStoreDvfUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vectorStoreFormService = TestBed.inject(VectorStoreDvfFormService);
    vectorStoreService = TestBed.inject(VectorStoreDvfService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vectorStore: IVectorStoreDvf = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ vectorStore });
      comp.ngOnInit();

      expect(comp.vectorStore).toEqual(vectorStore);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVectorStoreDvf>>();
      const vectorStore = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(vectorStoreFormService, 'getVectorStoreDvf').mockReturnValue(vectorStore);
      jest.spyOn(vectorStoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vectorStore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vectorStore }));
      saveSubject.complete();

      // THEN
      expect(vectorStoreFormService.getVectorStoreDvf).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vectorStoreService.update).toHaveBeenCalledWith(expect.objectContaining(vectorStore));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVectorStoreDvf>>();
      const vectorStore = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(vectorStoreFormService, 'getVectorStoreDvf').mockReturnValue({ id: null });
      jest.spyOn(vectorStoreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vectorStore: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vectorStore }));
      saveSubject.complete();

      // THEN
      expect(vectorStoreFormService.getVectorStoreDvf).toHaveBeenCalled();
      expect(vectorStoreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVectorStoreDvf>>();
      const vectorStore = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(vectorStoreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vectorStore });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vectorStoreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
