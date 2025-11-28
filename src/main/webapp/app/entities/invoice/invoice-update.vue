<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="jhipsterSampleApplicationApp.invoice.home.createOrEditLabel"
          data-cy="InvoiceCreateUpdateHeading"
          v-text="t$('jhipsterSampleApplicationApp.invoice.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="invoice.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="invoice.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('jhipsterSampleApplicationApp.invoice.invoiceNr')" for="invoice-invoiceNr"></label>
            <input
              type="text"
              class="form-control"
              name="invoiceNr"
              id="invoice-invoiceNr"
              data-cy="invoiceNr"
              :class="{ valid: !v$.invoiceNr.$invalid, invalid: v$.invoiceNr.$invalid }"
              v-model="v$.invoiceNr.$model"
              required
            />
            <div v-if="v$.invoiceNr.$anyDirty && v$.invoiceNr.$invalid">
              <small class="form-text text-danger" v-for="error of v$.invoiceNr.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('jhipsterSampleApplicationApp.invoice.invoiceDate')"
              for="invoice-invoiceDate"
            ></label>
            <div class="d-flex">
              <input
                id="invoice-invoiceDate"
                data-cy="invoiceDate"
                type="datetime-local"
                class="form-control"
                name="invoiceDate"
                :class="{ valid: !v$.invoiceDate.$invalid, invalid: v$.invoiceDate.$invalid }"
                :value="convertDateTimeFromServer(v$.invoiceDate.$model)"
                @change="updateInstantField('invoiceDate', $event)"
              />
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./invoice-update.component.ts"></script>
