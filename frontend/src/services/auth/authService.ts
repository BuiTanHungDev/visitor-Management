
import axiosClient from '../api/axiosClient'
import API_ENDPOINTS from '../api/apiEndpoints'

import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  CurrentUser,
  LogoutResponse,
} from '../../features/auth/types/auth.types'

const authService = {
  async login(data: LoginRequest): Promise<LoginResponse> {
    const response = await axiosClient.post<LoginResponse>(
      API_ENDPOINTS.AUTH.LOGIN,
      data,
    )

    // Lưu Access Token sau khi đăng nhập thành công.
    sessionStorage.setItem('accessToken', response.data.accessToken)

    return response.data
  },

  async register(data: RegisterRequest): Promise<RegisterResponse> {
    const response = await axiosClient.post<RegisterResponse>(
      API_ENDPOINTS.AUTH.REGISTER,
      data,
    )

    return response.data
  },

  async getCurrentUser(): Promise<CurrentUser> {
    const response = await axiosClient.get<CurrentUser>(
      API_ENDPOINTS.AUTH.ME,
    )

    return response.data
  },

  async logout(): Promise<LogoutResponse> {
    try {
      const response = await axiosClient.post<LogoutResponse>(
        API_ENDPOINTS.AUTH.LOGOUT,
      )

      return response.data
    } finally {
      // Xóa token kể cả khi request logout gặp lỗi.
      sessionStorage.removeItem('accessToken')
    }
  },
}

export default authService
