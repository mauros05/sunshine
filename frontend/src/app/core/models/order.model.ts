export interface OrderItem {
  id: string;
  productId: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Order {
  id: string;
  restaurantId: string;
  status: 'OPEN' | 'PAID' | 'CANCELLED';
  total: number;
  createdAt: string;
  items: OrderItem[];
}

export interface AddOrderItemRequest {
  productId: string;
  quantity: number;
}

export interface PayOrderRequest {
  method: 'CASH' | 'CARD' | 'TRANSFER';
  amount: number;
}

export interface TicketItem {
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Ticket {
  orderId: string;
  folio: number;
  restaurantName: string;
  restaurantAddress: string;
  createdAt: string;
  paidAt: string;
  paymentMethod: 'CASH' | 'CARD' | 'TRANSFER';
  total: number;
  items: TicketItem[];
}
