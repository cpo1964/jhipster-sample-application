import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import InvoiceService from './invoice.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IInvoice, Invoice } from '@/shared/model/invoice.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InvoiceUpdate',
  setup() {
    const invoiceService = inject('invoiceService', () => new InvoiceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const invoice: Ref<IInvoice> = ref(new Invoice());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'de'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveInvoice = async invoiceId => {
      try {
        const res = await invoiceService().find(invoiceId);
        res.invoiceDate = new Date(res.invoiceDate);
        invoice.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.invoiceId) {
      retrieveInvoice(route.params.invoiceId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      invoiceNr: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      invoiceDate: {},
    };
    const v$ = useVuelidate(validationRules, invoice as any);
    v$.value.$validate();

    return {
      invoiceService,
      alertService,
      invoice,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: invoice }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.invoice.id) {
        this.invoiceService()
          .update(this.invoice)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('jhipsterSampleApplicationApp.invoice.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.invoiceService()
          .create(this.invoice)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('jhipsterSampleApplicationApp.invoice.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
