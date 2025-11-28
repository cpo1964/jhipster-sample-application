import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TextblockService from './textblock.service';
import { type ITextblock } from '@/shared/model/textblock.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TextblockDetails',
  setup() {
    const textblockService = inject('textblockService', () => new TextblockService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const textblock: Ref<ITextblock> = ref({});

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

    return {
      alertService,
      textblock,

      previousState,
      t$: useI18n().t,
    };
  },
});
