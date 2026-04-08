import { useEffect, useState, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { useForm } from 'react-hook-form'
import { aseguradoService } from '../../services/aseguradoService'
import { getErrorMessage } from '../../utils/helpers'
import Header from '../../components/layout/Header'
import Button from '../../components/ui/Button'
import Modal from '../../components/ui/Modal'
import EmptyState from '../../components/ui/EmptyState'

function AseguradoForm({ onSubmit, loading, defaultValues }) {
  const { register, handleSubmit, formState: { errors } } = useForm({ defaultValues })

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="form-label">Nombre *</label>
          <input className={`form-input ${errors.nombre ? 'error' : ''}`} placeholder="Juan"
            {...register('nombre', { required: 'Requerido', maxLength: { value: 100, message: 'Máx. 100 chars' } })} />
          {errors.nombre && <p className="form-error">{errors.nombre.message}</p>}
        </div>
        <div>
          <label className="form-label">Apellido *</label>
          <input className={`form-input ${errors.apellido ? 'error' : ''}`} placeholder="Pérez"
            {...register('apellido', { required: 'Requerido', maxLength: { value: 100, message: 'Máx. 100 chars' } })} />
          {errors.apellido && <p className="form-error">{errors.apellido.message}</p>}
        </div>
      </div>
      <div>
        <label className="form-label">Número de Identificación *</label>
        <input className={`form-input ${errors.numeroIdentificacion ? 'error' : ''}`} placeholder="1712345678"
          {...register('numeroIdentificacion', { required: 'Requerido' })} />
        {errors.numeroIdentificacion && <p className="form-error">{errors.numeroIdentificacion.message}</p>}
      </div>
      <div>
        <label className="form-label">Correo Electrónico *</label>
        <input type="email" className={`form-input ${errors.correoElectronico ? 'error' : ''}`} placeholder="juan@email.com"
          {...register('correoElectronico', { required: 'Requerido', pattern: { value: /\S+@\S+\.\S+/, message: 'Email inválido' } })} />
        {errors.correoElectronico && <p className="form-error">{errors.correoElectronico.message}</p>}
      </div>
      <div>
        <label className="form-label">Teléfono *</label>
        <input className={`form-input ${errors.telefono ? 'error' : ''}`} placeholder="0991234567"
          {...register('telefono', { required: 'Requerido', pattern: { value: /^\d+$/, message: 'Solo números' } })} />
        {errors.telefono && <p className="form-error">{errors.telefono.message}</p>}
      </div>
      <div>
        <label className="form-label">Dirección *</label>
        <input className={`form-input ${errors.direccion ? 'error' : ''}`} placeholder="Av. Amazonas N36-152"
          {...register('direccion', { required: 'Requerido' })} />
        {errors.direccion && <p className="form-error">{errors.direccion.message}</p>}
      </div>
      <div className="flex justify-end gap-3 pt-2">
        <Button type="submit" loading={loading}>
          {defaultValues?.id ? 'Actualizar' : 'Registrar Asegurado'}
        </Button>
      </div>
    </form>
  )
}

export default function AseguradosPage() {
  const navigate = useNavigate()
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
      const res = await aseguradoService.listar()
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
      setFiltered(lista.filter(a =>
        a.numeroIdentificacion?.toLowerCase().includes(q) ||
        a.nombre?.toLowerCase().includes(q) ||
        a.apellido?.toLowerCase().includes(q)
      ))
    }
  }, [search, lista])

  const handleSubmit = async (formData) => {
    setSaving(true)
    try {
      if (editing?.id) {
        await aseguradoService.actualizar(editing.id, formData)
        toast.success('Asegurado actualizado correctamente')
      } else {
        await aseguradoService.crear(formData)
        toast.success('Asegurado registrado correctamente')
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

  const openEdit = (a) => { setEditing(a); setShowModal(true) }
  const openNew = () => { setEditing(null); setShowModal(true) }

  return (
    <div>
      <Header
        title="Asegurados"
        subtitle="Gestión de clientes"
        action={
          <Button onClick={openNew}>
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
            </svg>
            Nuevo asegurado
          </Button>
        }
      />

      <div className="card mb-5 p-4">
        <div className="relative max-w-sm">
          <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-slate-400">
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <input
            className="form-input pl-9"
            placeholder="Buscar por nombre o identificación..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      <div className="card overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-16">
            <div className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : filtered.length === 0 ? (
          <EmptyState
            icon={
              <svg className="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            }
            title={search ? 'Sin resultados' : 'No hay asegurados registrados'}
            description={search ? `No se encontró "${search}"` : 'Comienza registrando a tu primer cliente.'}
            action={!search ? openNew : undefined}
            actionLabel="+ Añadir ahora"
          />
        ) : (
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-slate-100 bg-slate-50">
                <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Nombre</th>
                <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Identificación</th>
                <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Correo</th>
                <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Teléfono</th>
                <th className="text-left px-6 py-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filtered.map((a) => (
                <tr key={a.id} className="hover:bg-slate-50 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 rounded-full bg-primary-100 text-primary-700 font-bold text-xs flex items-center justify-center shrink-0">
                        {a.nombre?.[0]}{a.apellido?.[0]}
                      </div>
                      <span className="font-medium text-slate-900">{a.nombre} {a.apellido}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-slate-600">{a.numeroIdentificacion}</td>
                  <td className="px-6 py-4 text-slate-600">{a.correoElectronico}</td>
                  <td className="px-6 py-4 text-slate-600">{a.telefono}</td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <Button variant="ghost" size="sm" onClick={() => navigate(`/asegurados/${a.id}`)}>Ver</Button>
                      <Button variant="secondary" size="sm" onClick={() => openEdit(a)}>Editar</Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        {!loading && filtered.length > 0 && (
          <div className="px-6 py-3 border-t border-slate-100 text-xs text-slate-400">
            Mostrando {filtered.length} de {lista.length} asegurados
          </div>
        )}
      </div>

      <Modal
        isOpen={showModal}
        onClose={() => { setShowModal(false); setEditing(null) }}
        title={editing?.id ? 'Editar Asegurado' : 'Registrar Asegurado'}
        size="lg"
      >
        <AseguradoForm onSubmit={handleSubmit} loading={saving} defaultValues={editing} />
      </Modal>
    </div>
  )
}
