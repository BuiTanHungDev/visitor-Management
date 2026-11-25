
import { Outlet } from 'react-router-dom'

function AuthLayout() {
  return (
    <div className="min-h-screen bg-slate-50 lg:grid lg:grid-cols-2">
      {/* Branding panel */}
      <aside className="relative hidden overflow-hidden bg-[#102747] px-12 py-14 text-white lg:flex lg:flex-col lg:justify-between xl:px-20">
        <div className="absolute -right-32 -top-32 h-96 w-96 rounded-full border border-white/10" />
        <div className="absolute -bottom-40 -left-32 h-[500px] w-[500px] rounded-full border border-white/10" />

        <div className="relative">
          <div className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-white/10 text-xl font-bold">
              V
            </div>
            <div>
              <p className="text-lg font-bold">Visitor Management</p>
              <p className="text-xs text-blue-200">Hệ thống quản lý thăm khách</p>
            </div>
          </div>
        </div>

        <div className="relative max-w-lg">
          <p className="mb-5 text-sm font-semibold uppercase tracking-[0.2em] text-blue-300">
            Kết nối và quản lý
          </p>

          <h1 className="text-4xl font-bold leading-tight xl:text-5xl">
            Quản lý thăm khách
            <span className="mt-2 block text-blue-300">
              nhanh chóng, thuận tiện và an toàn.
            </span>
          </h1>

          <p className="mt-6 max-w-md text-base leading-7 text-slate-300">
            Nền tảng hỗ trợ thân nhân đăng ký thăm khách và giúp cán bộ
            quản lý, theo dõi quy trình đăng ký trên một hệ thống thống nhất.
          </p>
        </div>

        <p className="relative text-xs text-slate-400">
          © {new Date().getFullYear()} Visitor Management System
        </p>
      </aside>

      {/* Form panel */}
      <main className="flex min-h-screen flex-col">
        <div className="flex items-center gap-3 px-6 py-6 lg:hidden">
          <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-[#102747] font-bold text-white">
            V
          </div>
          <div>
            <p className="font-bold text-slate-800">Visitor Management</p>
            <p className="text-xs text-slate-500">Hệ thống quản lý thăm khách</p>
          </div>
        </div>

        <div className="flex flex-1 items-center justify-center px-5 py-10 sm:px-10">
          <div className="w-full max-w-md">
            <Outlet />
          </div>
        </div>

        <footer className="px-6 py-5 text-center text-xs text-slate-400">
          Hệ thống quản lý thân nhân thăm khách
        </footer>
      </main>
    </div>
  )
}

export default AuthLayout
