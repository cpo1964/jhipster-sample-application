import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TextblockService from './textblock.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import InvoiceService from '@/entities/invoice/invoice.service';
import { type IInvoice } from '@/shared/model/invoice.model';
import { type ITextblock, Textblock } from '@/shared/model/textblock.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TextblockUpdate',
  setup() {
    const textblockService = inject('textblockService', () => new TextblockService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const textblock: Ref<ITextblock> = ref(new Textblock());

    const invoiceService = inject('invoiceService', () => new InvoiceService());

    const invoices: Ref<IInvoice[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'de'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTextblock = async textblockId => {
      try {
        const res = await textblockService().find(textblockId);
        textblock.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.textblockId) {
      retrieveTextblock(route.params.textblockId);
    }

    const initRelationships = () => {
      invoiceService()
        .retrieve()
        .then(res => {
          invoices.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      describer: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      nr: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      text: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      invoice: {},
    };
    const v$ = useVuelidate(validationRules, textblock as any);
    v$.value.$validate();

    return {
      textblockService,
      alertService,
      textblock,
      previousState,
      isSaving,
      currentLanguage,
      invoices,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.textblock.id) {
        this.textblockService()
          .update(this.textblock)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('jhipsterSampleApplicationApp.textblock.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.textblockService()
          .create(this.textblock)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('jhipsterSampleApplicationApp.textblock.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
