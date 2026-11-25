
import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'

import { useAuth } from '../context/AuthContext'

type ContactMethod = 'email' | 'phone'

function RegisterPage() {
  const [fullName, setFullName] = useState('')
  const [contactMethod, setContactMethod] = useState<ContactMethod>('email')
  const [contact, setContact] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const { register } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    setErrorMessage('')
    setSuccessMessage('')

    if (password !== confirmPassword) {
      setErrorMessage('Mật khẩu xác nhận không khớp.')
      return
    }

    if (password.length < 8) {
      setErrorMessage('Mật khẩu phải có ít nhất 8 ký tự.')
      return
    }

    setIsLoading(true)

    try {
      const response = await register({
        fullName: fullName.trim(),
        email: contactMethod === 'email' ? contact.trim() : null,
        phone: contactMethod === 'phone' ? contact.trim() : null,
        password,
      })

      setSuccessMessage(
        response.message || 'Đăng ký tài khoản thành công.',
      )

      setTimeout(() => {
        navigate('/login')
      }, 1500)
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErrorMessage(
          error.response?.data?.message ||
          'Đăng ký không thành công. Vui lòng kiểm tra lại thông tin.',
        )
      } else {
        setErrorMessage('Không thể đăng ký. Vui lòng thử lại.')
      }
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div>
      <div className="mb-7">
        <p className="mb-2 text-sm font-semibold text-blue-700">
          TẠO TÀI KHOẢN MỚI
        </p>

        <h1 className="text-3xl font-bold tracking-tight text-slate-900">
          Đăng ký thân nhân
        </h1>

        <p className="mt-3 text-sm leading-6 text-slate-500">
          Tạo tài khoản để thực hiện đăng ký và theo dõi lịch thăm khách.
        </p>
      </div>

      <form className="space-y-4" onSubmit={handleSubmit}>
        <div>
          <label htmlFor="fullName" className="mb-2 block text-sm font-semibold text-slate-700">
            Họ và tên
          </label>

          <input
            id="fullName"
            type="text"
            autoComplete="name"
            placeholder="Nhập họ và tên"
            value={fullName}
            onChange={(event) => setFullName(event.target.value)}
            required
            maxLength={150}
            className="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-600 focus:ring-4 focus:ring-blue-600/10"
          />
        </div>

        <div>
          <label className="mb-2 block text-sm font-semibold text-slate-700">
            Phương thức đăng ký
          </label>

          <div className="grid grid-cols-2 gap-3">
            <button
              type="button"
              onClick={() => {
                setContactMethod('email')
                setContact('')
              }}
              aria-pressed={contactMethod === 'email'}
              className={`rounded-xl border px-4 py-3 text-sm font-semibold transition ${
                contactMethod === 'email'
                  ? 'border-blue-700 bg-blue-50 text-blue-800 ring-2 ring-blue-700/10'
                  : 'border-slate-300 bg-white text-slate-600 hover:bg-slate-50'
              }`}
            >
              Email
            </button>

            <button
              type="button"
              onClick={() => {
                setContactMethod('phone')
                setContact('')
              }}
              aria-pressed={contactMethod === 'phone'}
              className={`rounded-xl border px-4 py-3 text-sm font-semibold transition ${
                contactMethod === 'phone'
                  ? 'border-blue-700 bg-blue-50 text-blue-800 ring-2 ring-blue-700/10'
                  : 'border-slate-300 bg-white text-slate-600 hover:bg-slate-50'
              }`}
            >
              Số điện thoại
            </button>
          </div>
        </div>

        <div>
          <label htmlFor="contact" className="mb-2 block text-sm font-semibold text-slate-700">
            {contactMethod === 'email' ? 'Địa chỉ email' : 'Số điện thoại'}
          </label>

          <input
            key={contactMethod}
            id="contact"
            type={contactMethod === 'email' ? 'email' : 'tel'}
            autoComplete={contactMethod === 'email' ? 'email' : 'tel'}
            placeholder={contactMethod === 'email' ? 'example@email.com' : 'Nhập số điện thoại'}
            value={contact}
            onChange={(event) => setContact(event.target.value)}
            required
            maxLength={contactMethod === 'email' ? 150 : 16}
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
              autoComplete="new-password"
              placeholder="Nhập mật khẩu (ít nhất 8 ký tự)"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
              minLength={8}
              maxLength={100}
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

        <div>
          <label htmlFor="confirmPassword" className="mb-2 block text-sm font-semibold text-slate-700">
            Xác nhận mật khẩu
          </label>

          <input
            id="confirmPassword"
            type="password"
            autoComplete="new-password"
            placeholder="Nhập lại mật khẩu"
            value={confirmPassword}
            onChange={(event) => setConfirmPassword(event.target.value)}
            required
            className="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition placeholder:text-slate-400 focus:border-blue-600 focus:ring-4 focus:ring-blue-600/10"
          />
        </div>

        {errorMessage && (
          <div role="alert" className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {errorMessage}
          </div>
        )}

        {successMessage && (
          <div role="status" className="rounded-xl border border-green-200 bg-green-50 px-4 py-3 text-sm text-green-700">
            {successMessage}
          </div>
        )}

        <button
          type="submit"
          disabled={isLoading}
          className="w-full rounded-xl bg-[#17365D] px-4 py-3.5 text-sm font-semibold text-white shadow-sm transition hover:bg-[#102747] disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isLoading ? 'Đang tạo tài khoản...' : 'Tạo tài khoản'}
        </button>
      </form>

      <p className="mt-6 text-center text-sm text-slate-600">
        Đã có tài khoản?{' '}
        <Link to="/login" className="font-semibold text-blue-700 hover:text-blue-900">
          Đăng nhập
        </Link>
      </p>
    </div>
  )
}

export default RegisterPage
