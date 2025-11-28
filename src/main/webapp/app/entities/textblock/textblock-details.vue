<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="textblock">
        <h2 class="jh-entity-heading" data-cy="textblockDetailsHeading">
          <span v-text="t$('jhipsterSampleApplicationApp.textblock.detail.title')"></span> {{ textblock.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('jhipsterSampleApplicationApp.textblock.describer')"></span>
          </dt>
          <dd>
            <span>{{ textblock.describer }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterSampleApplicationApp.textblock.nr')"></span>
          </dt>
          <dd>
            <span>{{ textblock.nr }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterSampleApplicationApp.textblock.text')"></span>
          </dt>
          <dd>
            <span>{{ textblock.text }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterSampleApplicationApp.textblock.invoice')"></span>
          </dt>
          <dd>
            <div v-if="textblock.invoice">
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: textblock.invoice.id } }">{{
                textblock.invoice.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="textblock.id"
          :to="{ name: 'TextblockEdit', params: { textblockId: textblock.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./textblock-details.component.ts"></script>
