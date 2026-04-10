export interface User {
  id: string;
  fullName: string;
  email:string;
  active: boolean;
  createdAt: string;
}

export interface CreateUserRequest {
  fullName: string;
  email: string;
}

export interface RestaurantMembership {
  id: string;
  userId: string;
  userName: string;
  userEmail: string;
  restaurantId: string;
  restaurantName: string;
  role: 'OWNER' | 'MANAGER' | 'CASHIER';
  createdAt: string;
}

export interface AddUserToRestaurantRequest {
  userId: string;
  role: 'OWNER' | 'MANAGER' | 'CASHIER';
}
