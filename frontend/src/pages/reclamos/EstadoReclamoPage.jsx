import { useState } from 'react'
import toast from 'react-hot-toast'
import { reclamoService } from '../../services/reclamoService'
import { formatCurrency, formatDate, estadoReclamoColor, getErrorMessage } from '../../utils/helpers'
import Header from '../../components/layout/Header'
import Button from '../../components/ui/Button'
import Badge from '../../components/ui/Badge'

const estadoIconos = {
  APROBADO: (
    <div className="w-14 h-14 rounded-full bg-emerald-100 flex items-center justify-center">
      <svg className="w-7 h-7 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
        <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
      </svg>
    </div>
  ),
  DESCARTADO: (
    <div className="w-14 h-14 rounded-full bg-slate-100 flex items-center justify-center">
      <svg className="w-7 h-7 text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
        <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
      </svg>
    </div>
  ),
  EN_REVISION_MANUAL: (
    <div className="w-14 h-14 rounded-full bg-amber-100 flex items-center justify-center">
      <svg className="w-7 h-7 text-amber-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
        <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z" />
      </svg>
    </div>
  ),
  REGISTRADO: (
    <div className="w-14 h-14 rounded-full bg-blue-100 flex items-center justify-center">
      <svg className="w-7 h-7 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
        <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    </div>
  ),
  RECHAZADO: (
    <div className="w-14 h-14 rounded-full bg-red-100 flex items-center justify-center">
      <svg className="w-7 h-7 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
        <path strokeLinecap="round" strokeLinejoin="round" d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636" />
      </svg>
    </div>
  ),
}

export default function EstadoReclamoPage() {
  const [query, setQuery] = useState('')
  const [loading, setLoading] = useState(false)
  const [reclamo, setReclamo] = useState(null)
  const [error, setError] = useState(null)

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!query.trim()) return
    setLoading(true)
    setError(null)
    setReclamo(null)
    try {
      const res = await reclamoService.consultarEstado(query.trim())
      setReclamo(res.data)
    } catch (err) {
      if (err.response?.status === 404) {
        setError('No se encontró ningún reclamo con ese número de seguimiento.')
      } else {
        setError(getErrorMessage(err))
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <Header title="Consulta de Estado" subtitle="Reclamos" />

      <div className="card p-5 mb-6">
        <form onSubmit={handleSearch} className="flex gap-3">
          <div className="relative flex-1">
            <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-slate-400">
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
            <input
              className="form-input pl-9"
              placeholder="Número de seguimiento (ej: REC-2026-001)"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />
          </div>
          <Button type="submit" loading={loading} disabled={!query.trim()}>
            Consultar
          </Button>
        </form>
      </div>

      {error && (
        <div className="card p-5 flex items-center gap-3 bg-red-50 border-red-100">
          <div className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center shrink-0">
            <svg className="w-5 h-5 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
            </svg>
          </div>
          <p className="text-sm text-red-700">{error}</p>
        </div>
      )}

      {reclamo && (
        <div className="space-y-4">
          <div className="card p-6 flex items-center gap-5">
            {estadoIconos[reclamo.estado] || estadoIconos.REGISTRADO}
            <div className="flex-1">
              <p className="text-xs text-slate-400 font-medium uppercase tracking-wider mb-1">
                Reclamo #{reclamo.numeroSeguimiento}
              </p>
              <Badge className={`${estadoReclamoColor(reclamo.estado)} text-sm font-bold px-3 py-1`}>
                {reclamo.estado?.replace(/_/g, ' ')}
              </Badge>
              {reclamo.mensajeEstado && (
                <p className="text-sm text-slate-500 mt-2">{reclamo.mensajeEstado}</p>
              )}
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="card p-5">
              <h3 className="font-semibold text-slate-800 mb-4 flex items-center gap-2">
                <svg className="w-4 h-4 text-primary-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                Detalle Financiero
              </h3>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className="text-sm text-slate-500">Monto Estimado</span>
                  <span className="text-sm font-semibold text-slate-800">{formatCurrency(reclamo.montoEstimado)}</span>
                </div>
                {reclamo.deducibleCalculado != null && (
                  <div className="flex justify-between">
                    <span className="text-sm text-slate-500">Deducible Aplicado</span>
                    <span className="text-sm font-semibold text-red-600">- {formatCurrency(reclamo.deducibleCalculado)}</span>
                  </div>
                )}
                {reclamo.montoAprobado != null && (
                  <div className="border-t border-slate-100 pt-3 flex justify-between">
                    <span className="text-sm font-semibold text-slate-700">Monto Aprobado</span>
                    <span className="text-lg font-bold text-emerald-600">{formatCurrency(reclamo.montoAprobado)}</span>
                  </div>
                )}
              </div>
            </div>

            <div className="card p-5">
              <h3 className="font-semibold text-slate-800 mb-4 flex items-center gap-2">
                <svg className="w-4 h-4 text-primary-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5" />
                </svg>
                Información del Caso
              </h3>
              <div className="space-y-3">
                <div>
                  <p className="text-xs text-slate-400 mb-0.5">Fecha de Registro</p>
                  <p className="text-sm font-medium text-slate-800">{formatDate(reclamo.fechaCreacion)}</p>
                </div>
                {reclamo.fechaResolucion && (
                  <div>
                    <p className="text-xs text-slate-400 mb-0.5">Fecha de Resolución</p>
                    <p className="text-sm font-medium text-slate-800">{formatDate(reclamo.fechaResolucion)}</p>
                  </div>
                )}
              </div>
            </div>
          </div>

          <div className="flex gap-3">
            <Button onClick={() => { setReclamo(null); setQuery('') }}>
              Nueva consulta
            </Button>
            <Button variant="secondary" onClick={() => {
              navigator.clipboard.writeText(reclamo.numeroSeguimiento)
              toast.success('Número copiado')
            }}>
              Copiar N° seguimiento
            </Button>
          </div>
        </div>
      )}

      {!reclamo && !error && (
        <div className="card p-12 text-center">
          <div className="w-16 h-16 rounded-full bg-slate-100 flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <h3 className="font-semibold text-slate-700 mb-2">Consulte el estado de su reclamo</h3>
          <p className="text-sm text-slate-400">
            Ingrese el número de seguimiento que recibió al registrar su siniestro.
            <br />Formato: <span className="font-mono text-slate-600">REC-YYYY-NNN</span>
          </p>
        </div>
      )}
    </div>
  )
}
