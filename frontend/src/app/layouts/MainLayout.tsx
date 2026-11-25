
import { Outlet, useNavigate } from 'react-router-dom'
import { LogOut } from 'lucide-react'
import Sidebar from '../../components/common/Sidebar'
import { useAuth } from '../../features/auth/context/AuthContext'

function MainLayout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = async () => {
    try {
      await logout()
    } finally {
      navigate('/login', { replace: true })
    }
  }

  return (
    <div className="flex min-h-screen bg-slate-50">
      <Sidebar />

      <div className="flex min-w-0 flex-1 flex-col">
        <header className="flex h-16 items-center justify-between border-b border-slate-200 bg-white px-6">
          <h2 className="font-semibold text-slate-800">
            Hệ thống quản lý thân nhân thăm khách
          </h2>

          <div className="flex items-center gap-4">
            <div className="text-right">
              <p className="text-sm font-semibold text-slate-800">
                {user?.fullName}
              </p>
              <p className="text-xs text-slate-500">
                {user?.roles.join(', ')}
              </p>
            </div>

            <button
              onClick={handleLogout}
              className="flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-slate-600 hover:bg-red-50 hover:text-red-600"
            >
              <LogOut size={17} />
              Đăng xuất
            </button>
          </div>
        </header>

        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}

export default MainLayout
