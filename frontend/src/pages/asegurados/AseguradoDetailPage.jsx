import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { aseguradoService } from '../../services/aseguradoService'
import { formatDate, getErrorMessage } from '../../utils/helpers'
import Button from '../../components/ui/Button'

function InfoRow({ label, value, icon }) {
  return (
    <div className="flex items-start gap-3">
      {icon && <span className="text-slate-400 mt-0.5">{icon}</span>}
      <div>
        <p className="text-xs text-slate-400 font-medium mb-0.5">{label}</p>
        <p className="text-sm text-slate-800 font-medium">{value || '—'}</p>
      </div>
    </div>
  )
}

export default function AseguradoDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [asegurado, setAsegurado] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetch = async () => {
      try {
        const res = await aseguradoService.obtener(id)
        setAsegurado(res.data?.data || res.data)
      } catch (err) {
        toast.error(getErrorMessage(err))
        navigate('/asegurados')
      } finally {
        setLoading(false)
      }
    }
    fetch()
  }, [id, navigate])

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full animate-spin" />
      </div>
    )
  }

  if (!asegurado) return null

  return (
    <div>
      <nav className="flex items-center gap-2 text-sm text-slate-400 mb-6">
        <button onClick={() => navigate('/asegurados')} className="hover:text-primary-600 transition-colors">
          Asegurados
        </button>
        <svg className="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
        </svg>
        <span className="text-slate-700 font-medium">Detalle del Perfil</span>
      </nav>

      <div className="flex items-start justify-between mb-6">
        <div className="flex items-center gap-4">
          <div className="w-14 h-14 rounded-2xl bg-primary-100 text-primary-700 font-bold text-xl flex items-center justify-center">
            {asegurado.nombre?.[0]}{asegurado.apellido?.[0]}
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-900">{asegurado.nombre} {asegurado.apellido}</h1>
            <span className="inline-flex items-center gap-1.5 text-xs text-emerald-600 font-medium mt-0.5">
              <div className="w-1.5 h-1.5 rounded-full bg-emerald-500" />
              Cliente Activo
            </span>
          </div>
        </div>
        <Button variant="secondary" onClick={() => navigate('/asegurados')}>
          ← Volver
        </Button>
      </div>

      <div className="card p-6">
        <h2 className="font-semibold text-slate-800 mb-4 flex items-center gap-2">
          <svg className="w-4 h-4 text-primary-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          Información Personal
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
          <InfoRow
            label="Número de Identificación"
            value={asegurado.numeroIdentificacion}
            icon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M15 9h3.75M15 12h3.75M15 15h3.75M4.5 19.5h15a2.25 2.25 0 002.25-2.25V6.75A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25v10.5A2.25 2.25 0 004.5 19.5zm6-10.125a1.875 1.875 0 11-3.75 0 1.875 1.875 0 013.75 0zm1.294 6.336a6.721 6.721 0 01-3.17.789 6.721 6.721 0 01-3.168-.789 3.376 3.376 0 016.338 0z" /></svg>}
          />
          <InfoRow
            label="Teléfono de Contacto"
            value={asegurado.telefono}
            icon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 002.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 01-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 00-1.091-.852H4.5A2.25 2.25 0 002.25 4.5v2.25z" /></svg>}
          />
          <InfoRow
            label="Correo Electrónico"
            value={asegurado.correoElectronico}
            icon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25h-15a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25m19.5 0v.243a2.25 2.25 0 01-1.07 1.916l-7.5 4.615a2.25 2.25 0 01-2.36 0L3.32 8.91a2.25 2.25 0 01-1.07-1.916V6.75" /></svg>}
          />
          <InfoRow
            label="Dirección de Residencia"
            value={asegurado.direccion}
            icon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" /><path strokeLinecap="round" strokeLinejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z" /></svg>}
          />
        </div>
      </div>
    </div>
  )
}
