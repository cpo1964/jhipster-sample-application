import { defineComponent, provide } from 'vue';

import TextblockService from './textblock/textblock.service';
import InvoiceService from './invoice/invoice.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('textblockService', () => new TextblockService());
    provide('invoiceService', () => new InvoiceService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
