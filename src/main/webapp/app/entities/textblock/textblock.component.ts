import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import TextblockService from './textblock.service';
import { type ITextblock } from '@/shared/model/textblock.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Textblock',
  setup() {
    const { t: t$ } = useI18n();
    const textblockService = inject('textblockService', () => new TextblockService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const textblocks: Ref<ITextblock[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveTextblocks = async () => {
      isFetching.value = true;
      try {
        const res = await textblockService().retrieve();
        textblocks.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveTextblocks();
    };

    onMounted(async () => {
      await retrieveTextblocks();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ITextblock) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeTextblock = async () => {
      try {
        await textblockService().delete(removeId.value);
        const message = t$('jhipsterSampleApplicationApp.textblock.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveTextblocks();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      textblocks,
      handleSyncList,
      isFetching,
      retrieveTextblocks,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeTextblock,
      t$,
    };
  },
});
