export interface LoginMembership {
  restaurantId: string;
  restaurantName: string;
  role: 'OWNER' | 'MANAGER' | 'CASHIER';
}

export interface LoginResponse {
  userId: string;
  fullName: string;
  email: string;
  memberships: LoginMembership[];
}
