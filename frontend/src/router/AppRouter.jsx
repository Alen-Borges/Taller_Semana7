import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import ProtectedRoute from './ProtectedRoute'
import LoginPage from '../pages/LoginPage'
import DashboardPage from '../pages/DashboardPage'
import NotFoundPage from '../pages/NotFoundPage'
import AseguradosPage from '../pages/asegurados/AseguradosPage'
import AseguradoDetailPage from '../pages/asegurados/AseguradoDetailPage'
import VehiculosPage from '../pages/vehiculos/VehiculosPage'
import PolizasPage from '../pages/polizas/PolizasPage'
import NuevoReclamoPage from '../pages/reclamos/NuevoReclamoPage'
import EstadoReclamoPage from '../pages/reclamos/EstadoReclamoPage'
import ReclamosPage from '../pages/reclamos/ReclamosPage'

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/nuevo-reclamo" element={<NuevoReclamoPage />} />
          <Route path="/estado-reclamo" element={<EstadoReclamoPage />} />
        </Route>

        <Route element={<ProtectedRoute roles={['GESTOR']} />}>
          <Route path="/asegurados" element={<AseguradosPage />} />
          <Route path="/asegurados/:id" element={<AseguradoDetailPage />} />
          <Route path="/vehiculos" element={<VehiculosPage />} />
          <Route path="/polizas" element={<PolizasPage />} />
          <Route path="/reclamos" element={<ReclamosPage />} />
        </Route>

        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  )
}
