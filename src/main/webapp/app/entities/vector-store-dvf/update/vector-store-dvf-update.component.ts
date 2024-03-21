import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { VectorStoreDvfService } from '../service/vector-store-dvf.service';
import { IVectorStoreDvf } from '../vector-store-dvf.model';
import { VectorStoreDvfFormService, VectorStoreDvfFormGroup } from './vector-store-dvf-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vector-store-dvf-update',
  templateUrl: './vector-store-dvf-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VectorStoreDvfUpdateComponent implements OnInit {
  isSaving = false;
  vectorStore: IVectorStoreDvf | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected vectorStoreService = inject(VectorStoreDvfService);
  protected vectorStoreFormService = inject(VectorStoreDvfFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VectorStoreDvfFormGroup = this.vectorStoreFormService.createVectorStoreDvfFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vectorStore }) => {
      this.vectorStore = vectorStore;
      if (vectorStore) {
        this.updateForm(vectorStore);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('chatDvfApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vectorStore = this.vectorStoreFormService.getVectorStoreDvf(this.editForm);
    if (vectorStore.id !== null) {
      this.subscribeToSaveResponse(this.vectorStoreService.update(vectorStore));
    } else {
      this.subscribeToSaveResponse(this.vectorStoreService.create(vectorStore));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVectorStoreDvf>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vectorStore: IVectorStoreDvf): void {
    this.vectorStore = vectorStore;
    this.vectorStoreFormService.resetForm(this.editForm, vectorStore);
  }
}
