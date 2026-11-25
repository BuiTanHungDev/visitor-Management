
import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../features/auth/context/AuthContext'

function ProtectedRoute() {
  const { isAuthenticated, isInitializing } = useAuth()
  const location = useLocation()

  if (isInitializing) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="text-sm text-slate-500">
          Đang kiểm tra phiên đăng nhập...
        </div>
      </div>
    )
  }

  if (!isAuthenticated) {
    return (
      <Navigate
        to="/login"
        replace
        state={{ from: location }}
      />
    )
  }

  return <Outlet />
}

export default ProtectedRoute
