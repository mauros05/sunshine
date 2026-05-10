import { StringLiteral } from "typescript";

export interface SalesReport {
  restaurantId: string;
  paidOrders: number;
  totalSales: number;
}

export interface SalesSummary {
  restaurantId: string;
  from: string;
  to: string;
  paidOrders: number;
  totalSales: number;
  averageTicket: number;
}

export interface TopProduct {
  productId: string;
  productName: string;
  quantitySold: number;
  totalSales: number;
}
