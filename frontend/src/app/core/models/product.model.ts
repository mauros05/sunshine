export interface Product {
  id: string,
  restaurantId: string;
  name: string;
  price: number;
  category: string;
  active: boolean;
  createdAt: string;
}

export interface CreateProductRequest {
  name: string;
  price: number;
  category: string;
}
