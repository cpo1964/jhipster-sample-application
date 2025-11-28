<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="invoice">
        <h2 class="jh-entity-heading" data-cy="invoiceDetailsHeading">
          <span v-text="t$('jhipsterSampleApplicationApp.invoice.detail.title')"></span> {{ invoice.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('jhipsterSampleApplicationApp.invoice.invoiceNr')"></span>
          </dt>
          <dd>
            <span>{{ invoice.invoiceNr }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterSampleApplicationApp.invoice.invoiceDate')"></span>
          </dt>
          <dd>
            <span v-if="invoice.invoiceDate">{{ formatDateLong(invoice.invoiceDate) }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="invoice.id" :to="{ name: 'InvoiceEdit', params: { invoiceId: invoice.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./invoice-details.component.ts"></script>
