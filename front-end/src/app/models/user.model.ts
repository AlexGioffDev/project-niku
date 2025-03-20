export interface Users {
  Users: User[];
}

export interface User {
  id: number;
  username: string;
  password: string;
  role: string;
}
