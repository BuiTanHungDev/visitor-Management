
export interface LoginRequest {
  identifier: string
  password: string
}

export interface LoginResponse {
  userId: string
  username: string | null
  fullName: string
  email: string | null
  status: string
  lastLoginAt: string | null
  accessToken: string
  tokenType: string
  expiresIn: number
}

export interface RegisterRequest {
  fullName: string
  email: string | null
  phone: string | null
  password: string
}

export interface RegisterResponse {
  userId: string
  fullName: string
  email: string | null
  phone: string | null
  status: string
  message: string
  createdAt: string
}

export interface CurrentUser {
  userId: string
  username: string | null
  fullName: string
  email: string | null
  status: string
  roles: string[]
  permissions: string[]
}

export interface LogoutResponse {
  message: string
}
