
import { useLocation } from 'react-router-dom'

function ComingSoonPage() {
  const location = useLocation()

  const title =
    location.pathname === '/registrations/create'
      ? 'Đăng ký thăm khách'
      : 'Thông tin cá nhân'

  return (
    <div className="rounded-xl border border-slate-200 bg-white p-8">
      <h1 className="text-xl font-bold text-slate-900">{title}</h1>
      <p className="mt-2 text-sm text-slate-500">
        Chức năng đang được xây dựng.
      </p>
    </div>
  )
}

export default ComingSoonPage
