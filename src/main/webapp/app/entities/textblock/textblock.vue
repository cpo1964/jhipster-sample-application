<template>
  <div>
    <h2 id="page-heading" data-cy="TextblockHeading">
      <span v-text="t$('jhipsterSampleApplicationApp.textblock.home.title')" id="textblock-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('jhipsterSampleApplicationApp.textblock.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'TextblockCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-textblock"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('jhipsterSampleApplicationApp.textblock.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && textblocks && textblocks.length === 0">
      <span v-text="t$('jhipsterSampleApplicationApp.textblock.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="textblocks && textblocks.length > 0">
      <table class="table table-striped" aria-describedby="textblocks">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('jhipsterSampleApplicationApp.textblock.describer')"></span></th>
            <th scope="row"><span v-text="t$('jhipsterSampleApplicationApp.textblock.nr')"></span></th>
            <th scope="row"><span v-text="t$('jhipsterSampleApplicationApp.textblock.text')"></span></th>
            <th scope="row"><span v-text="t$('jhipsterSampleApplicationApp.textblock.invoice')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="textblock in textblocks" :key="textblock.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TextblockView', params: { textblockId: textblock.id } }">{{ textblock.id }}</router-link>
            </td>
            <td>{{ textblock.describer }}</td>
            <td>{{ textblock.nr }}</td>
            <td>{{ textblock.text }}</td>
            <td>
              <div v-if="textblock.invoice">
                <router-link :to="{ name: 'InvoiceView', params: { invoiceId: textblock.invoice.id } }">{{
                  textblock.invoice.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TextblockView', params: { textblockId: textblock.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TextblockEdit', params: { textblockId: textblock.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(textblock)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="jhipsterSampleApplicationApp.textblock.delete.question"
          data-cy="textblockDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-textblock-heading" v-text="t$('jhipsterSampleApplicationApp.textblock.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-textblock"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeTextblock()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./textblock.component.ts"></script>
