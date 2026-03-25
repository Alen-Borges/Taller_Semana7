import { useEffect, useState, useCallback } from 'react'
import toast from 'react-hot-toast'
import { useForm } from 'react-hook-form'
import { polizaService } from '../../services/polizaService'
import { aseguradoService } from '../../services/aseguradoService'
import { vehiculoService } from '../../services/vehiculoService'
import { formatDate, formatCurrency, estadoPolizaColor, getErrorMessage } from '../../utils/helpers'
import Header from '../../components/layout/Header'
import Button from '../../components/ui/Button'
import Modal from '../../components/ui/Modal'
import EmptyState from '../../components/ui/EmptyState'
import Badge from '../../components/ui/Badge'

function PolizaForm({ onSubmit, loading, asegurados, vehiculos }) {
  const { register, handleSubmit, formState: { errors } } = useForm()
  const today = new Date().toISOString().split('T')[0]

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <label className="form-label">Número de Póliza *</label>
        <input className={`form-input ${errors.numero ? 'error' : ''}`} placeholder="POL-2026-001"
          {...register('numero', { required: 'Requerido', pattern: { value: /^POL-\d{4}-\d{3}$/, message: 'Formato: POL-YYYY-NNN' } })} />
        {errors.numero && <p className="form-error">{errors.numero.message}</p>}
        <p className="text-xs text-slate-400 mt-1">Formato: POL-YYYY-NNN</p>
      </div>
      <div>
        <label className="form-label">Asegurado *</label>
        <select className={`form-input ${errors.aseguradoId ? 'error' : ''}`}
          {...register('aseguradoId', { required: 'Requerido', valueAsNumber: true })}>
          <option value="">— Seleccionar asegurado —</option>
          {asegurados.map((a) => (
            <option key={a.id} value={a.id}>{a.nombre} {a.apellido} — {a.numeroIdentificacion}</option>
          ))}
        </select>
        {errors.aseguradoId && <p className="form-error">{errors.aseguradoId.message}</p>}
      </div>
      <div>
        <label className="form-label">Vehículo *</label>
        <select className={`form-input ${errors.vehiculoId ? 'error' : ''}`}
          {...register('vehiculoId', { required: 'Requerido', valueAsNumber: true })}>
          <option value="">— Seleccionar vehículo —</option>
          {vehiculos.map((v) => (
            <option key={v.id} value={v.id}>{v.marca} {v.modelo} — {v.placa}</option>
          ))}
        </select>
        {errors.vehiculoId && <p className="form-error">{errors.vehiculoId.message}</p>}
      </div>
      <div>
        <label className="form-label">Valor Asegurado (USD) *</label>
        <input type="number" step="0.01" className={`form-input ${errors.valorAsegurado ? 'error' : ''}`} placeholder="25000.00"
          {...register('valorAsegurado', { required: 'Requerido', min: { value: 1, message: 'Debe ser positivo' }, valueAsNumber: true })} />
        {errors.valorAsegurado && <p className="form-error">{errors.valorAsegurado.message}</p>}
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="form-label">Vigencia Inicio *</label>
          <input type="date" className={`form-input ${errors.vigenciaInicio ? 'error' : ''}`}
            {...register('vigenciaInicio', { required: 'Requerido' })} />
          {errors.vigenciaInicio && <p className="form-error">{errors.vigenciaInicio.message}</p>}
        </div>
        <div>
          <label className="form-label">Vigencia Fin *</label>
          <input type="date" className={`form-input ${errors.vigenciaFin ? 'error' : ''}`} min={today}
            {...register('vigenciaFin', { required: 'Requerido' })} />
          {errors.vigenciaFin && <p className="form-error">{errors.vigenciaFin.message}</p>}
        </div>
      </div>
      <div className="flex justify-end gap-3 pt-2">
        <Button type="submit" loading={loading}>Registrar Póliza</Button>
      </div>
    </form>
  )
}

export default function PolizasPage() {
  const [lista, setLista] = useState([])
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [showModal, setShowModal] = useState(false)
  const [asegurados, setAsegurados] = useState([])
  const [vehiculos, setVehiculos] = useState([])

  const fetchData = useCallback(async () => {
    setLoading(true)
    try {
      const res = await polizaService.listar()
      setLista(Array.isArray(res.data) ? res.data : [])
    } catch (err) {
      toast.error(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchData() }, [fetchData])

  const openNew = async () => {
    try {
      const [ra, rv] = await Promise.all([
        aseguradoService.listar(),
        vehiculoService.listar(),
      ])
      setAsegurados(Array.isArray(ra.data) ? ra.data : [])
      setVehiculos(Array.isArray(rv.data) ? rv.data : [])
      setShowModal(true)
    } catch (err) {
      toast.error('Error al cargar asegurados/vehículos')
    }
  }

  const handleSubmit = async (formData) => {
    setSaving(true)
    try {
      await polizaService.crear(formData)
      toast.success('Póliza registrada correctamente')
      setShowModal(false)
      fetchData()
    } catch (err) {
      toast.error(getErrorMessage(err))
    } finally {
      setSaving(false)
    }
  }

  const activas   = lista.filter(p => p.estado === 'ACTIVA').length
  const inactivas = lista.filter(p => p.estado === 'INACTIVA').length
  const vencidas  = lista.filter(p => p.estado === 'VENCIDA').length

  return (
    <div>
      <Header
        title="Gestión de Pólizas"
        subtitle="Administración de contratos"
        action={
          <Button onClick={openNew}>
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
            </svg>
            Nueva póliza
          </Button>
        }
      />

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-5">
        <div className="stat-card"><p className="text-xs text-slate-500 mb-1">Total</p><p className="text-2xl font-bold text-slate-900">{lista.length}</p></div>
        <div className="stat-card"><p className="text-xs text-slate-500 mb-1">Activas</p><p className="text-2xl font-bold text-emerald-600">{activas}</p></div>
        <div className="stat-card"><p className="text-xs text-slate-500 mb-1">Inactivas</p><p className="text-2xl font-bold text-amber-600">{inactivas}</p></div>
        <div className="stat-card"><p className="text-xs text-slate-500 mb-1">Vencidas</p><p className="text-2xl font-bold text-red-600">{vencidas}</p></div>
      </div>

      <div className="card overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-16">
            <div className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : lista.length === 0 ? (
          <EmptyState
            icon={<svg className="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>}
            title="No hay pólizas registradas"
            description="Registra la primera póliza del sistema."
            action={openNew}
            actionLabel="+ Nueva póliza"
          />
        ) : (
          <>
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50">
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">N° Póliza</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Asegurado</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Vehículo</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Estado</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Valor Aseg.</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Inicio</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Vence</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {lista.map((p) => (
                  <tr key={p.id} className="hover:bg-slate-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-slate-800">{p.numero}</td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <div className="w-7 h-7 rounded-full bg-primary-100 text-primary-700 font-bold text-xs flex items-center justify-center shrink-0">
                          {p.aseguradoNombre?.[0] || '?'}
                        </div>
                        <span className="text-slate-700">{p.aseguradoNombre || `ID: ${p.aseguradoId}`}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-500 text-xs">{p.vehiculoMarcaModeloPlaca || `ID: ${p.vehiculoId}`}</td>
                    <td className="px-6 py-4">
                      <Badge className={estadoPolizaColor(p.estado)}>{p.estado}</Badge>
                    </td>
                    <td className="px-6 py-4 font-medium text-slate-800">{formatCurrency(p.valorAsegurado)}</td>
                    <td className="px-6 py-4 text-slate-500">{formatDate(p.vigenciaInicio)}</td>
                    <td className="px-6 py-4 text-slate-500">{formatDate(p.vigenciaFin)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="px-6 py-3 border-t border-slate-100 text-xs text-slate-400">
              {lista.length} póliza(s) registrada(s)
            </div>
          </>
        )}
      </div>

      <Modal isOpen={showModal} onClose={() => setShowModal(false)} title="Registrar Nueva Póliza" size="lg">
        <PolizaForm onSubmit={handleSubmit} loading={saving} asegurados={asegurados} vehiculos={vehiculos} />
      </Modal>
    </div>
  )
}
