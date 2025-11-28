<template>
  <div>
    <h2 id="page-heading" data-cy="InvoiceHeading">
      <span v-text="t$('jhipsterSampleApplicationApp.invoice.home.title')" id="invoice-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('jhipsterSampleApplicationApp.invoice.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'InvoiceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-invoice"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('jhipsterSampleApplicationApp.invoice.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && invoices && invoices.length === 0">
      <span v-text="t$('jhipsterSampleApplicationApp.invoice.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="invoices && invoices.length > 0">
      <table class="table table-striped" aria-describedby="invoices">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('jhipsterSampleApplicationApp.invoice.invoiceNr')"></span></th>
            <th scope="row"><span v-text="t$('jhipsterSampleApplicationApp.invoice.invoiceDate')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="invoice in invoices" :key="invoice.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoice.id } }">{{ invoice.id }}</router-link>
            </td>
            <td>{{ invoice.invoiceNr }}</td>
            <td>{{ formatDateShort(invoice.invoiceDate) || '' }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoice.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'InvoiceEdit', params: { invoiceId: invoice.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(invoice)"
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
          id="jhipsterSampleApplicationApp.invoice.delete.question"
          data-cy="invoiceDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-invoice-heading" v-text="t$('jhipsterSampleApplicationApp.invoice.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-invoice"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeInvoice()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./invoice.component.ts"></script>
