
import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  ClipboardPlus,
  Users,
} from 'lucide-react'
import { useAuth } from '../../features/auth/context/AuthContext'

const menuItems = [
  {
    label: 'Tổng quan',
    path: '/dashboard',
    icon: LayoutDashboard,
    permission: null,
  },
  {
    label: 'Đăng ký thăm khách',
    path: '/registrations/create',
    icon: ClipboardPlus,
    permission: 'REGISTRATION_CREATE',
  },
  {
    label: 'Thông tin cá nhân',
    path: '/profile',
    icon: Users,
    permission: null,
  },
]

function Sidebar() {
  const { user } = useAuth()

  const visibleItems = menuItems.filter(
    (item) =>
      item.permission === null ||
      user?.permissions.includes(item.permission),
  )

  return (
    <aside className="flex w-64 shrink-0 flex-col border-r border-slate-200 bg-white">
      <div className="border-b border-slate-200 px-6 py-5">
        <h1 className="text-lg font-bold text-slate-900">
          Quản lý thăm khách
        </h1>
        <p className="mt-1 text-xs text-slate-500">
          Hệ thống quản lý thân nhân
        </p>
      </div>

      <nav className="flex-1 space-y-1 px-3 py-5">
        {visibleItems.map((item) => {
          const Icon = item.icon

          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-lg px-3 py-3 text-sm font-medium transition ${
                  isActive
                    ? 'bg-blue-50 text-blue-700'
                    : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900'
                }`
              }
            >
              <Icon size={19} />
              {item.label}
            </NavLink>
          )
        })}
      </nav>

      <div className="border-t border-slate-200 p-4">
        <p className="text-xs text-slate-400">
          Phiên bản 1.0.0
        </p>
      </div>
    </aside>
  )
}

export default Sidebar
