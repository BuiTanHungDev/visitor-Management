
import { useState, type FormEvent } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'

import { useAuth } from '../context/AuthContext'

function LoginPage() {
  const [identifier, setIdentifier] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    setErrorMessage('')
    setIsLoading(true)

    try {
      await login({ identifier: identifier.trim(), password })

      const from = location.state?.from?.pathname || '/dashboard'
      navigate(from, { replace: true })
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErrorMessage(
          error.response?.data?.message ||
          'Thông tin đăng nhập không chính xác hoặc có lỗi xảy ra.',
        )
      } else {
        setErrorMessage('Không thể đăng nhập. Vui lòng thử lại.')
      }
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div>
      <div className="mb-8">
        <p className="mb-2 text-sm font-semibold text-blue-700">
          CHÀO MỪNG BẠN
        </p>

        <h1 className="text-3xl font-bold tracking-tight text-slate-900">
          Đăng nhập
        </h1>

        <p className="mt-3 text-sm leading-6 text-slate-500">
          Đăng nhập để sử dụng các chức năng của hệ thống.
        </p>
      </div>

      <form className="space-y-5" onSubmit={handleSubmit}>
        <div>
          <label htmlFor="identifier" className="mb-2 block text-sm font-semibold text-slate-700">
            Email hoặc số điện thoại
          </label>

          <input
            id="identifier"
            type="text"
            autoComplete="username"
            placeholder="Nhập email hoặc số điện thoại"
            value={identifier}
            onChange={(event) => setIdentifier(event.target.value)}
            required
            className="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-600 focus:ring-4 focus:ring-blue-600/10"
          />
        </div>

        <div>
          <label htmlFor="password" className="mb-2 block text-sm font-semibold text-slate-700">
            Mật khẩu
          </label>

          <div className="relative">
            <input
              id="password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="current-password"
              placeholder="Nhập mật khẩu"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
              className="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 pr-20 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-600 focus:ring-4 focus:ring-blue-600/10"
            />

            <button
              type="button"
              onClick={() => setShowPassword((value) => !value)}
              className="absolute right-4 top-1/2 -translate-y-1/2 text-xs font-semibold text-blue-700"
            >
              {showPassword ? 'Ẩn' : 'Hiện'}
            </button>
          </div>
        </div>

        {errorMessage && (
          <div role="alert" className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {errorMessage}
          </div>
        )}

        <button
          type="submit"
          disabled={isLoading}
          className="w-full rounded-xl bg-[#17365D] px-4 py-3.5 text-sm font-semibold text-white shadow-sm transition hover:bg-[#102747] disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isLoading ? 'Đang đăng nhập...' : 'Đăng nhập'}
        </button>
      </form>

      <div className="my-7 flex items-center gap-4">
        <div className="h-px flex-1 bg-slate-200" />
        <span className="text-xs text-slate-400">HOẶC</span>
        <div className="h-px flex-1 bg-slate-200" />
      </div>

      <p className="text-center text-sm text-slate-600">
        Chưa có tài khoản?{' '}
        <Link to="/register" className="font-semibold text-blue-700 hover:text-blue-900">
          Đăng ký ngay
        </Link>
      </p>
    </div>
  )
}

export default LoginPage
