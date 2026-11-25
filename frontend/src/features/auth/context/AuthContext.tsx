
import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from 'react'

import authService from '../../../services/auth/authService'

import type {
  CurrentUser,
  LoginRequest,
  RegisterRequest,
  RegisterResponse,
} from '../types/auth.types'

interface AuthContextType {
  user: CurrentUser | null
  isAuthenticated: boolean
  isInitializing: boolean

  login: (data: LoginRequest) => Promise<void>
  register: (data: RegisterRequest) => Promise<RegisterResponse>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

interface AuthProviderProps {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<CurrentUser | null>(null)
  const [isInitializing, setIsInitializing] = useState(true)

  // Khôi phục phiên đăng nhập khi tải lại trang.
  useEffect(() => {
    const restoreSession = async () => {
      const token = sessionStorage.getItem('accessToken')

      if (!token) {
        setIsInitializing(false)
        return
      }

      try {
        const currentUser = await authService.getCurrentUser()
        setUser(currentUser)
      } catch {
        sessionStorage.removeItem('accessToken')
        setUser(null)
      } finally {
        setIsInitializing(false)
      }
    }

    void restoreSession()
  }, [])

  // Đăng nhập và lấy thông tin tài khoản.
  const login = useCallback(async (data: LoginRequest) => {
    await authService.login(data)

    try {
      const currentUser = await authService.getCurrentUser()
      setUser(currentUser)
    } catch (error) {
      sessionStorage.removeItem('accessToken')
      setUser(null)
      throw error
    }
  }, [])

  // Đăng ký tài khoản thân nhân.
  const register = useCallback(async (data: RegisterRequest) => {
    return authService.register(data)
  }, [])

  // Đăng xuất.
  const logout = useCallback(async () => {
    try {
      await authService.logout()
    } finally {
      sessionStorage.removeItem('accessToken')
      setUser(null)
    }
  }, [])

  const value: AuthContextType = {
    user,
    isAuthenticated: user !== null,
    isInitializing,
    login,
    register,
    logout,
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('useAuth phải được sử dụng bên trong AuthProvider')
  }

  return context
}
