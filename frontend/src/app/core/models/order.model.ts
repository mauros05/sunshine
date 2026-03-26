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
