import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TextblockDetails from './textblock-details.vue';
import TextblockService from './textblock.service';
import AlertService from '@/shared/alert/alert.service';

type TextblockDetailsComponentType = InstanceType<typeof TextblockDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const textblockSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Textblock Management Detail Component', () => {
    let textblockServiceStub: SinonStubbedInstance<TextblockService>;
    let mountOptions: MountingOptions<TextblockDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      textblockServiceStub = sinon.createStubInstance<TextblockService>(TextblockService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          textblockService: () => textblockServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        textblockServiceStub.find.resolves(textblockSample);
        route = {
          params: {
            textblockId: `${123}`,
          },
        };
        const wrapper = shallowMount(TextblockDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.textblock).toMatchObject(textblockSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        textblockServiceStub.find.resolves(textblockSample);
        const wrapper = shallowMount(TextblockDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
