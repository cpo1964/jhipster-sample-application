<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="jhipsterSampleApplicationApp.textblock.home.createOrEditLabel"
          data-cy="TextblockCreateUpdateHeading"
          v-text="t$('jhipsterSampleApplicationApp.textblock.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="textblock.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="textblock.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('jhipsterSampleApplicationApp.textblock.describer')"
              for="textblock-describer"
            ></label>
            <input
              type="text"
              class="form-control"
              name="describer"
              id="textblock-describer"
              data-cy="describer"
              :class="{ valid: !v$.describer.$invalid, invalid: v$.describer.$invalid }"
              v-model="v$.describer.$model"
            />
            <div v-if="v$.describer.$anyDirty && v$.describer.$invalid">
              <small class="form-text text-danger" v-for="error of v$.describer.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('jhipsterSampleApplicationApp.textblock.nr')" for="textblock-nr"></label>
            <input
              type="text"
              class="form-control"
              name="nr"
              id="textblock-nr"
              data-cy="nr"
              :class="{ valid: !v$.nr.$invalid, invalid: v$.nr.$invalid }"
              v-model="v$.nr.$model"
            />
            <div v-if="v$.nr.$anyDirty && v$.nr.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nr.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('jhipsterSampleApplicationApp.textblock.text')" for="textblock-text"></label>
            <input
              type="text"
              class="form-control"
              name="text"
              id="textblock-text"
              data-cy="text"
              :class="{ valid: !v$.text.$invalid, invalid: v$.text.$invalid }"
              v-model="v$.text.$model"
            />
            <div v-if="v$.text.$anyDirty && v$.text.$invalid">
              <small class="form-text text-danger" v-for="error of v$.text.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('jhipsterSampleApplicationApp.textblock.invoice')" for="textblock-invoice"></label>
            <select class="form-control" id="textblock-invoice" data-cy="invoice" name="invoice" v-model="textblock.invoice">
              <option :value="null"></option>
              <option
                :value="textblock.invoice && invoiceOption.id === textblock.invoice.id ? textblock.invoice : invoiceOption"
                v-for="invoiceOption in invoices"
                :key="invoiceOption.id"
              >
                {{ invoiceOption.id }}
              </option>
            </select>
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
<script lang="ts" src="./textblock-update.component.ts"></script>
