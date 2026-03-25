import { useState } from 'react'
import { Navigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import toast from 'react-hot-toast'
import { useAuth } from '../context/AuthContext'
import { authService } from '../services/authService'
import { getErrorMessage } from '../utils/helpers'
import Button from '../components/ui/Button'


export default function LoginPage() {
  const { login, isAuthenticated } = useAuth()
  const [loading, setLoading] = useState(false)
  const [apiError, setApiError] = useState(null)

  const { register, handleSubmit, formState: { errors } } = useForm()

  if (isAuthenticated) return <Navigate to="/dashboard" replace />

  const onSubmit = async ({ username, password }) => {
    setLoading(true)
    setApiError(null)
    try {
      const res = await authService.login(username, password)
      const data = res.data || res
      login(data.token, { username: data.username, rol: data.rol })
      toast.success(`Bienvenido, ${data.username}`)
    } catch (err) {
      setApiError(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 via-blue-50 to-slate-100 flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-white shadow-sm border border-slate-200 mb-3">
            <svg viewBox="0 0 24 24" fill="none" className="w-8 h-8" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L4 6v6c0 5.25 3.5 10.15 8 11.5C16.5 22.15 20 17.25 20 12V6L12 2z" fill="#2563eb" opacity="0.15"/>
              <path d="M12 2L4 6v6c0 5.25 3.5 10.15 8 11.5C16.5 22.15 20 17.25 20 12V6L12 2z" stroke="#2563eb" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
              <path d="M9 12l2 2 4-4" stroke="#2563eb" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </div>
          <h1 className="text-xl font-bold text-slate-900">Evaluador Automatizado de Siniestros</h1>
          <p className="text-xs text-slate-400 font-medium tracking-widest mt-0.5">INSURTECH</p>
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-7">
          <h2 className="text-lg font-bold text-slate-900 mb-1">Bienvenido de nuevo</h2>
          <p className="text-sm text-slate-500 mb-5">Ingrese sus credenciales para acceder al sistema.</p>

          {apiError && (
            <div className="flex gap-2 bg-red-50 border border-red-200 text-red-700 rounded-lg p-3 mb-4 text-sm">
              <svg className="w-4 h-4 shrink-0 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z" clipRule="evenodd" />
              </svg>
              <div>
                <p className="font-semibold">Acceso denegado</p>
                <p>{apiError}</p>
              </div>
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="form-label">Usuario</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-slate-400">
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
                  </svg>
                </div>
                <input
                  type="text"
                  placeholder="ejemplo@evaluador.com"
                  className={`form-input pl-9 ${errors.username ? 'error' : ''}`}
                  {...register('username', { required: 'El usuario es obligatorio' })}
                />
              </div>
              {errors.username && <p className="form-error">{errors.username.message}</p>}
            </div>

            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="form-label mb-0">Password</label>
                <span className="text-xs text-primary-600 cursor-pointer hover:underline">¿Olvidó su contraseña?</span>
              </div>
              <div className="relative">
                <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-slate-400">
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z" />
                  </svg>
                </div>
                <input
                  type="password"
                  placeholder="••••••••"
                  className={`form-input pl-9 ${errors.password ? 'error' : ''}`}
                  {...register('password', { required: 'La contraseña es obligatoria' })}
                />
              </div>
              {errors.password && <p className="form-error">{errors.password.message}</p>}
            </div>

            <Button type="submit" loading={loading} className="w-full mt-2" size="lg">
              Iniciar Sesión →
            </Button>
          </form>

          <p className="text-center text-xs text-slate-500 mt-4">
            ¿No tiene una cuenta?{' '}
            <span className="text-primary-600 cursor-pointer hover:underline font-medium">Contacte con Soporte</span>
          </p>
        </div>

        <div className="flex items-center justify-center gap-4 mt-5 text-xs text-slate-400">
          <span>Términos de Servicio</span>
          <span>Privacidad</span>
          <span>Ayuda</span>
        </div>
        <div className="flex items-center justify-center gap-1.5 mt-3">
        </div>
      </div>
    </div>
  )
}
