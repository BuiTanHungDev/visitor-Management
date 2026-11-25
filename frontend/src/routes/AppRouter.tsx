
import { Navigate, Route, Routes } from 'react-router-dom'

import AuthLayout from '../app/layouts/AuthLayout'
import MainLayout from '../app/layouts/MainLayout'

import LoginPage from '../features/auth/pages/LoginPage'
import RegisterPage from '../features/auth/pages/RegisterPage'
import DashboardPage from '../features/dashboard/pages/DashboardPage'

import ProtectedRoute from './ProtectedRoute'
import ComingSoonPage from '../features/dashboard/pages/ComingSoonPage'
function AppRouter() {
    return (
        <Routes>
            {/* Trang đăng nhập và đăng ký */}
            <Route element={<AuthLayout />}>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
            </Route>

            {/* Các trang yêu cầu đăng nhập */}
            <Route element={<ProtectedRoute />}>
                <Route element={<MainLayout />}>
                    <Route path="/dashboard" element={<DashboardPage />} />
                </Route>
            </Route>

            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
            <Route path="/registrations/create" element={<ComingSoonPage />} />
            <Route path="/profile" element={<ComingSoonPage />} />
        </Routes>
    )
}

export default AppRouter
