import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Textblock = () => import('@/entities/textblock/textblock.vue');
const TextblockUpdate = () => import('@/entities/textblock/textblock-update.vue');
const TextblockDetails = () => import('@/entities/textblock/textblock-details.vue');

const Invoice = () => import('@/entities/invoice/invoice.vue');
const InvoiceUpdate = () => import('@/entities/invoice/invoice-update.vue');
const InvoiceDetails = () => import('@/entities/invoice/invoice-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'textblock',
      name: 'Textblock',
      component: Textblock,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'textblock/new',
      name: 'TextblockCreate',
      component: TextblockUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'textblock/:textblockId/edit',
      name: 'TextblockEdit',
      component: TextblockUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'textblock/:textblockId/view',
      name: 'TextblockView',
      component: TextblockDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice',
      name: 'Invoice',
      component: Invoice,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/new',
      name: 'InvoiceCreate',
      component: InvoiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/:invoiceId/edit',
      name: 'InvoiceEdit',
      component: InvoiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/:invoiceId/view',
      name: 'InvoiceView',
      component: InvoiceDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
