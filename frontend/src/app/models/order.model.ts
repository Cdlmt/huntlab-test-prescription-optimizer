export interface Order {
  patientName: string;
  lensType: string;
  quantity: number;
  unitPrice: number;
  discountCode?: string;
}

export interface DiscountRule {
  code: string;
  percentage: number;
}

