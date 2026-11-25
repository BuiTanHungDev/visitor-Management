
import { Link, Outlet } from 'react-router-dom'

function MainLayout() {
  return (
    <div className="min-h-screen bg-slate-50">
      <header className="h-16 bg-white border-b border-slate-200">
        <div className="max-w-7xl h-full mx-auto px-6 flex items-center justify-between">
          <Link to="/dashboard" className="font-bold text-lg text-blue-700">
            Quản lý thân nhân thăm khách
          </Link>

          <span className="text-sm text-slate-500">
            Hệ thống quản lý
          </span>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        <Outlet />
      </main>
    </div>
  )
}

export default MainLayout
