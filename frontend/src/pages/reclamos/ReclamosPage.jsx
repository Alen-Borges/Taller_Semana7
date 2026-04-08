import { useEffect, useState, useCallback } from 'react'
import toast from 'react-hot-toast'
import { reclamoService } from '../../services/reclamoService'
import { formatCurrency, formatDate, estadoReclamoColor, getErrorMessage } from '../../utils/helpers'
import Header from '../../components/layout/Header'
import Badge from '../../components/ui/Badge'
import EmptyState from '../../components/ui/EmptyState'

export default function ReclamosPage() {
  const [lista, setLista] = useState([])
  const [loading, setLoading] = useState(true)

  const fetchData = useCallback(async () => {
    setLoading(true)
    try {
      const res = await reclamoService.listarEscalados()
      setLista(Array.isArray(res.data) ? res.data : [])
    } catch (err) {
      toast.error(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchData() }, [fetchData])

  return (
    <div>
      <Header title="Reclamos Escalados" subtitle="Panel del gestor" />
      <p className="text-sm text-slate-500 -mt-4 mb-6">Reclamos en estado EN_REVISION_MANUAL pendientes de resolución.</p>

      <div className="card overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-16">
            <div className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : lista.length === 0 ? (
          <EmptyState
            icon={
              <svg className="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            }
            title="No hay reclamos pendientes"
            description="No se encontraron reclamos en revisión manual."
          />
        ) : (
          <>
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50">
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">N° Seguimiento</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Asegurado</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Póliza</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Monto Est.</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Fecha</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Banderas</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Estado</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {lista.map((r) => (
                  <tr key={r.numeroSeguimiento} className="hover:bg-slate-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-primary-600">{r.numeroSeguimiento}</td>
                    <td className="px-6 py-4">
                      <div>
                        <p className="font-medium text-slate-800">{r.aseguradoNombreCompleto || `ID: ${r.aseguradoId}`}</p>
                        <p className="text-xs text-slate-400">{r.aseguradoNumeroIdentificacion}</p>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600 text-xs">{r.polizaNumero}</td>
                    <td className="px-6 py-4 font-medium text-slate-800">{formatCurrency(r.montoEstimado)}</td>
                    <td className="px-6 py-4 text-slate-500">{formatDate(r.fechaCreacion)}</td>
                    <td className="px-6 py-4">
                      <div className="flex flex-wrap gap-1">
                        {r.banderas?.map((b, i) => (
                          <span key={i} className="text-[10px] bg-amber-100 text-amber-700 px-1.5 py-0.5 rounded font-medium">{b}</span>
                        )) || <span className="text-slate-400 text-xs">—</span>}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <Badge className={estadoReclamoColor(r.estado)}>{r.estado?.replace(/_/g, ' ')}</Badge>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="px-6 py-3 border-t border-slate-100 text-xs text-slate-400">
              {lista.length} reclamo(s) en revisión manual
            </div>
          </>
        )}
      </div>
    </div>
  )
}
