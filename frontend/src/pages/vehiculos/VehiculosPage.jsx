import { useEffect, useState, useCallback } from 'react'
import toast from 'react-hot-toast'
import { useForm } from 'react-hook-form'
import { vehiculoService } from '../../services/vehiculoService'
import { getErrorMessage } from '../../utils/helpers'
import Header from '../../components/layout/Header'
import Button from '../../components/ui/Button'
import Modal from '../../components/ui/Modal'
import EmptyState from '../../components/ui/EmptyState'
import Badge from '../../components/ui/Badge'

function VehiculoForm({ onSubmit, loading, defaultValues }) {
  const { register, handleSubmit, formState: { errors } } = useForm({ defaultValues })

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="form-label">Marca *</label>
          <input className={`form-input ${errors.marca ? 'error' : ''}`} placeholder="Chevrolet"
            {...register('marca', { required: 'Requerido' })} />
          {errors.marca && <p className="form-error">{errors.marca.message}</p>}
        </div>
        <div>
          <label className="form-label">Modelo *</label>
          <input className={`form-input ${errors.modelo ? 'error' : ''}`} placeholder="Aveo"
            {...register('modelo', { required: 'Requerido' })} />
          {errors.modelo && <p className="form-error">{errors.modelo.message}</p>}
        </div>
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="form-label">Año *</label>
          <input type="number" className={`form-input ${errors.anio ? 'error' : ''}`} placeholder="2022"
            {...register('anio', { required: 'Requerido', min: { value: 1900, message: 'Mín. 1900' }, valueAsNumber: true })} />
          {errors.anio && <p className="form-error">{errors.anio.message}</p>}
        </div>
        <div>
          <label className="form-label">Placa *</label>
          <input
            className={`form-input uppercase ${errors.placa ? 'error' : ''}`}
            placeholder="PBA-1234"
            {...register('placa', {
              required: 'Requerido',
              pattern: { value: /^[A-Z]{3}-\d{3,4}$/, message: 'Formato: AAA-123 o AAA-1234' },
              setValueAs: v => v.toUpperCase()
            })}
            onInput={(e) => e.target.value = e.target.value.toUpperCase()}
          />
          {errors.placa && <p className="form-error">{errors.placa.message}</p>}
        </div>
      </div>
      <div className="flex justify-end gap-3 pt-2">
        <Button type="submit" loading={loading}>
          {defaultValues?.id ? 'Actualizar' : 'Registrar Vehículo'}
        </Button>
      </div>
    </form>
  )
}

export default function VehiculosPage() {
  const [lista, setLista] = useState([])
  const [filtered, setFiltered] = useState([])
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [showModal, setShowModal] = useState(false)
  const [editing, setEditing] = useState(null)
  const [search, setSearch] = useState('')

  const fetchData = useCallback(async () => {
    setLoading(true)
    try {
      const res = await vehiculoService.listar()
      const data = Array.isArray(res.data) ? res.data : []
      setLista(data)
      setFiltered(data)
    } catch (err) {
      toast.error(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchData() }, [fetchData])

  useEffect(() => {
    if (!search.trim()) {
      setFiltered(lista)
    } else {
      const q = search.toLowerCase()
      setFiltered(lista.filter(v =>
        v.placa?.toLowerCase().includes(q) ||
        v.marca?.toLowerCase().includes(q) ||
        v.modelo?.toLowerCase().includes(q)
      ))
    }
  }, [search, lista])

  const handleSubmit = async (formData) => {
    setSaving(true)
    try {
      if (editing?.id) {
        await vehiculoService.actualizar(editing.id, formData)
        toast.success('Vehículo actualizado correctamente')
      } else {
        await vehiculoService.crear(formData)
        toast.success('Vehículo registrado correctamente')
      }
      setShowModal(false)
      setEditing(null)
      fetchData()
    } catch (err) {
      toast.error(getErrorMessage(err))
    } finally {
      setSaving(false)
    }
  }

  const openEdit = (v) => { setEditing(v); setShowModal(true) }
  const openNew = () => { setEditing(null); setShowModal(true) }

  return (
    <div>
      <Header
        title="Vehículos"
        subtitle="Gestión de activos vehiculares"
        action={
          <Button onClick={openNew}>
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
            </svg>
            Nuevo vehículo
          </Button>
        }
      />

      <div className="grid grid-cols-2 gap-4 mb-5">
        <div className="stat-card flex items-center gap-4">
          <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center">
            <svg className="w-5 h-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" /><path strokeLinecap="round" strokeLinejoin="round" d="M13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10" />
            </svg>
          </div>
          <div>
            <p className="text-xs text-slate-500">Total Flota</p>
            <p className="text-xl font-bold text-slate-900">{lista.length}</p>
          </div>
        </div>
        <div className="stat-card flex items-center gap-4">
          <div className="w-10 h-10 rounded-xl bg-emerald-50 flex items-center justify-center">
            <svg className="w-5 h-5 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div>
            <p className="text-xs text-slate-500">Registrados</p>
            <p className="text-xl font-bold text-slate-900">{lista.length}</p>
          </div>
        </div>
      </div>

      <div className="card mb-5 p-4">
        <div className="relative max-w-sm">
          <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-slate-400">
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <input
            className="form-input pl-9"
            placeholder="Buscar por placa, marca o modelo..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      <div className="card overflow-hidden">
        <div className="px-6 py-3 border-b border-slate-100">
          <h3 className="text-sm font-semibold text-slate-700">Listado Maestro</h3>
        </div>
        {loading ? (
          <div className="flex items-center justify-center py-16">
            <div className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : filtered.length === 0 ? (
          <EmptyState
            icon={<svg className="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" /></svg>}
            title={search ? 'Sin resultados' : 'No hay vehículos registrados'}
            description={search ? `No se encontró "${search}"` : 'Registra el primer vehículo de la flota.'}
            action={!search ? openNew : undefined}
            actionLabel="+ Registrar vehículo"
          />
        ) : (
          <>
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50">
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Vehículo</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Año</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Placa</th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map((v) => (
                  <tr key={v.id} className="hover:bg-slate-50 transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center text-slate-400">
                          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
                          </svg>
                        </div>
                        <div>
                          <p className="font-medium text-slate-900">{v.marca} {v.modelo}</p>
                          <p className="text-xs text-slate-400">ID: {v.id}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600">{v.anio}</td>
                    <td className="px-6 py-4">
                      <Badge className="bg-slate-100 text-slate-700 font-mono">{v.placa}</Badge>
                    </td>
                    <td className="px-6 py-4">
                      <Button variant="secondary" size="sm" onClick={() => openEdit(v)}>Editar</Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="px-6 py-3 border-t border-slate-100 text-xs text-slate-400">
              Mostrando {filtered.length} de {lista.length} vehículos
            </div>
          </>
        )}
      </div>

      <Modal isOpen={showModal} onClose={() => { setShowModal(false); setEditing(null) }} title={editing?.id ? 'Editar Vehículo' : 'Registrar Vehículo'}>
        <VehiculoForm onSubmit={handleSubmit} loading={saving} defaultValues={editing} />
      </Modal>
    </div>
  )
}
