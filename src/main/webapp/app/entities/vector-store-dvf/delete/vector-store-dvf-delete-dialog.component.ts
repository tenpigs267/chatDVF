import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVectorStoreDvf } from '../vector-store-dvf.model';
import { VectorStoreDvfService } from '../service/vector-store-dvf.service';

@Component({
  standalone: true,
  templateUrl: './vector-store-dvf-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VectorStoreDvfDeleteDialogComponent {
  vectorStore?: IVectorStoreDvf;

  protected vectorStoreService = inject(VectorStoreDvfService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.vectorStoreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
