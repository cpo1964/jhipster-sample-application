export interface IInvoice {
  id?: number;
  invoiceNr?: string;
  invoiceDate?: Date | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNr?: string,
    public invoiceDate?: Date | null,
  ) {}
}
