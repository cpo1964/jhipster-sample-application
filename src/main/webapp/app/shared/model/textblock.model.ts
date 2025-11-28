import { type IInvoice } from '@/shared/model/invoice.model';

export interface ITextblock {
  id?: number;
  describer?: string | null;
  nr?: string | null;
  text?: string | null;
  invoice?: IInvoice | null;
}

export class Textblock implements ITextblock {
  constructor(
    public id?: number,
    public describer?: string | null,
    public nr?: string | null,
    public text?: string | null,
    public invoice?: IInvoice | null,
  ) {}
}
