
import { ClipboardPlus, UserRound } from 'lucide-react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../auth/context/AuthContext'

function DashboardPage() {
  const { user } = useAuth()

  return (
    <div className="mx-auto max-w-7xl space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">
          Xin chào, {user?.fullName}!
        </h1>
        <p className="mt-2 text-sm text-slate-500">
          Chào mừng bạn đến với hệ thống quản lý thân nhân thăm khách.
        </p>
      </div>

      <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
        {user?.permissions.includes('REGISTRATION_CREATE') && (
          <Link
            to="/registrations/create"
            className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-md"
          >
            <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-lg bg-blue-50 text-blue-700">
              <ClipboardPlus size={24} />
            </div>

            <h2 className="font-semibold text-slate-900">
              Đăng ký thăm khách
            </h2>

            <p className="mt-2 text-sm text-slate-500">
              Tạo đăng ký thăm khách mới và theo dõi quá trình xử lý.
            </p>
          </Link>
        )}

        <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
          <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-lg bg-slate-100 text-slate-700">
            <UserRound size={24} />
          </div>

          <h2 className="font-semibold text-slate-900">
            Thông tin tài khoản
          </h2>

          <p className="mt-2 text-sm text-slate-500">
            Họ tên: {user?.fullName}
          </p>

          <p className="mt-1 text-sm text-slate-500">
            Email: {user?.email || 'Chưa cập nhật'}
          </p>

          <p className="mt-1 text-sm text-slate-500">
            Trạng thái: {user?.status}
          </p>
        </div>
      </div>
    </div>
  )
}

export default DashboardPage
