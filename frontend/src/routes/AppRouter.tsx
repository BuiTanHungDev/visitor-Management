
import { Navigate, Route, Routes } from 'react-router-dom'

import AuthLayout from '../app/layouts/AuthLayout'
import MainLayout from '../app/layouts/MainLayout'

import LoginPage from '../features/auth/pages/LoginPage'
import RegisterPage from '../features/auth/pages/RegisterPage'
import DashboardPage from '../features/dashboard/pages/DashboardPage'

function AppRouter() {
  return (
    <Routes>
      {/* Authentication */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Route>

      {/* Main application */}
      <Route element={<MainLayout />}>
        <Route path="/dashboard" element={<DashboardPage />} />
      </Route>

      {/* Default route */}
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Not found */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}

export default AppRouter
