export interface Restaurant {
  id: string;
  name: string;
  address: string;
  createdAt: string;
  }

export interface CreateRestaurantRequest {
  name: string;
  address: string;
  }
