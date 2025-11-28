import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Textblock from './textblock.vue';
import TextblockService from './textblock.service';
import AlertService from '@/shared/alert/alert.service';

type TextblockComponentType = InstanceType<typeof Textblock>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Textblock Management Component', () => {
    let textblockServiceStub: SinonStubbedInstance<TextblockService>;
    let mountOptions: MountingOptions<TextblockComponentType>['global'];

    beforeEach(() => {
      textblockServiceStub = sinon.createStubInstance<TextblockService>(TextblockService);
      textblockServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          textblockService: () => textblockServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        textblockServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Textblock, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(textblockServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.textblocks[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: TextblockComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Textblock, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        textblockServiceStub.retrieve.reset();
        textblockServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        textblockServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTextblock();
        await comp.$nextTick(); // clear components

        // THEN
        expect(textblockServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(textblockServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
