import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { aseguradoService } from '../services/aseguradoService'
import { vehiculoService } from '../services/vehiculoService'
import { polizaService } from '../services/polizaService'
import { reclamoService } from '../services/reclamoService'
import Header from '../components/layout/Header'
import Badge from '../components/ui/Badge'
import Button from '../components/ui/Button'
import { formatCurrency, estadoReclamoColor } from '../utils/helpers'

function StatCard({ label, value }) {
  return (
    <div className="stat-card">
      <p className="text-xs font-medium text-slate-500 mb-1">{label}</p>
      <p className="text-2xl font-bold text-slate-900">{value}</p>
    </div>
  )
}

export default function DashboardPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const isGestor = user?.rol === 'GESTOR'

  const [stats, setStats] = useState({ asegurados: 0, vehiculos: 0, polizas: 0, escalados: 0 })
  const [escalados, setEscalados] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!isGestor) { setLoading(false); return }
    const fetchData = async () => {
      try {
        const [ra, rv, rp, re] = await Promise.allSettled([
          aseguradoService.listar(),
          vehiculoService.listar(),
          polizaService.listar(),
          reclamoService.listarEscalados(),
        ])
        setStats({
          asegurados: ra.status === 'fulfilled' ? (ra.value.data?.length ?? 0) : 0,
          vehiculos:  rv.status === 'fulfilled' ? (rv.value.data?.length ?? 0) : 0,
          polizas:    rp.status === 'fulfilled' ? (rp.value.data?.length ?? 0) : 0,
          escalados:  re.status === 'fulfilled' ? (re.value.data?.length ?? 0) : 0,
        })
        if (re.status === 'fulfilled') {
          setEscalados((re.value.data ?? []).slice(0, 5))
        }
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [isGestor])

  if (!isGestor) {
    return (
      <div>
        <Header title="Panel Principal" subtitle="Bienvenido" />
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div onClick={() => navigate('/nuevo-reclamo')} className="card p-6 cursor-pointer hover:shadow-md transition-shadow group">
            <div className="w-10 h-10 rounded-xl bg-primary-50 flex items-center justify-center mb-3 group-hover:bg-primary-100 transition-colors">
              <svg className="w-5 h-5 text-primary-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
            </div>
            <h3 className="font-semibold text-slate-900 mb-1">Registrar Reclamo</h3>
            <p className="text-sm text-slate-500">Inicie el proceso de evaluación de su siniestro.</p>
          </div>
          <div onClick={() => navigate('/estado-reclamo')} className="card p-6 cursor-pointer hover:shadow-md transition-shadow group">
            <div className="w-10 h-10 rounded-xl bg-emerald-50 flex items-center justify-center mb-3 group-hover:bg-emerald-100 transition-colors">
              <svg className="w-5 h-5 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
            </div>
            <h3 className="font-semibold text-slate-900 mb-1">Consultar Estado</h3>
            <p className="text-sm text-slate-500">Revise el estado de su reclamo con el número de seguimiento.</p>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div>
      <Header title="Panel Gestor" subtitle="Visión General" />

      {loading ? (
        <div className="flex items-center justify-center py-16">
          <div className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full animate-spin" />
        </div>
      ) : (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <StatCard label="Asegurados"   value={stats.asegurados} color="blue" />
            <StatCard label="Vehículos"    value={stats.vehiculos}  color="green" />
            <StatCard label="Pólizas"      value={stats.polizas}    color="amber" />
            <StatCard label="En Revisión"  value={stats.escalados}  color="red" />
          </div>

          <div className="card">
            <div className="flex items-center justify-between px-6 py-4 border-b border-slate-100">
              <h2 className="font-semibold text-slate-900">Reclamos en Revisión Manual</h2>
              <Button variant="ghost" size="sm" onClick={() => navigate('/reclamos')}>Ver todos →</Button>
            </div>
            {escalados.length === 0 ? (
              <div className="py-12 text-center text-sm text-slate-400">No hay reclamos en revisión manual.</div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-slate-100 bg-slate-50">
                      <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">N° Seguimiento</th>
                      <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Asegurado</th>
                      <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Monto Est.</th>
                      <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Banderas</th>
                      <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Estado</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {escalados.map((r) => (
                      <tr key={r.numeroSeguimiento} className="hover:bg-slate-50 transition-colors">
                        <td className="px-6 py-4 font-medium text-primary-600">{r.numeroSeguimiento}</td>
                        <td className="px-6 py-4 text-slate-700">{r.aseguradoNombreCompleto || `ID: ${r.aseguradoId}`}</td>
                        <td className="px-6 py-4 font-medium text-slate-800">{formatCurrency(r.montoEstimado)}</td>
                        <td className="px-6 py-4">
                          <div className="flex flex-wrap gap-1">
                            {r.banderas?.map((b, i) => (
                              <span key={i} className="text-[10px] bg-amber-100 text-amber-700 px-1.5 py-0.5 rounded font-medium">{b}</span>
                            ))}
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <Badge className={estadoReclamoColor(r.estado)}>{r.estado?.replace(/_/g, ' ')}</Badge>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  )
}
