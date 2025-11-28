import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TextblockUpdate from './textblock-update.vue';
import TextblockService from './textblock.service';
import AlertService from '@/shared/alert/alert.service';

import InvoiceService from '@/entities/invoice/invoice.service';

type TextblockUpdateComponentType = InstanceType<typeof TextblockUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const textblockSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TextblockUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Textblock Management Update Component', () => {
    let comp: TextblockUpdateComponentType;
    let textblockServiceStub: SinonStubbedInstance<TextblockService>;

    beforeEach(() => {
      route = {};
      textblockServiceStub = sinon.createStubInstance<TextblockService>(TextblockService);
      textblockServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          textblockService: () => textblockServiceStub,
          invoiceService: () =>
            sinon.createStubInstance<InvoiceService>(InvoiceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TextblockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.textblock = textblockSample;
        textblockServiceStub.update.resolves(textblockSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(textblockServiceStub.update.calledWith(textblockSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        textblockServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TextblockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.textblock = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(textblockServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        textblockServiceStub.find.resolves(textblockSample);
        textblockServiceStub.retrieve.resolves([textblockSample]);

        // WHEN
        route = {
          params: {
            textblockId: `${textblockSample.id}`,
          },
        };
        const wrapper = shallowMount(TextblockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.textblock).toMatchObject(textblockSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        textblockServiceStub.find.resolves(textblockSample);
        const wrapper = shallowMount(TextblockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
