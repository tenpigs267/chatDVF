import { Component, CUSTOM_ELEMENTS_SCHEMA, ViewChild } from '@angular/core';
import 'deep-chat';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { DeepChat } from 'deep-chat';
import { CSRFService } from '../../core/auth/csrf.service';

@Component({
  selector: 'jhi-chat',
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss',
})
export class ChatComponent {
  constructor(
    protected applicationConfigService: ApplicationConfigService,
    protected csrfService: CSRFService,
  ) {}

  @ViewChild('deepchat') deepChat: DeepChat | undefined;

  getCSRF() {
    return this.csrfService.getCSRF();
  }
}
