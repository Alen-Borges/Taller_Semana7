import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { useForm } from 'react-hook-form'
import { reclamoService } from '../../services/reclamoService'
import { polizaService } from '../../services/polizaService'
import { getErrorMessage } from '../../utils/helpers'
import Button from '../../components/ui/Button'
import Header from '../../components/layout/Header'

export default function NuevoReclamoPage() {
  const navigate = useNavigate()
  const [saving, setSaving] = useState(false)
  const [polizas, setPolizas] = useState([])
  const [loadingPolizas, setLoadingPolizas] = useState(true)
  const [previews, setPreviews] = useState([])
  const [files, setFiles] = useState([])
  const [success, setSuccess] = useState(null)

  const { register, handleSubmit, formState: { errors }, reset } = useForm()

  const today = new Date().toISOString().split('T')[0]

  useEffect(() => {
    const fetch = async () => {
      try {
        const res = await polizaService.listar()
        const all = Array.isArray(res.data) ? res.data : []
        setPolizas(all.filter(p => p.estado === 'ACTIVA'))
      } catch {
        toast.error('No se pudieron cargar las pólizas activas')
      } finally {
        setLoadingPolizas(false)
      }
    }
    fetch()
  }, [])

  const handleFiles = (e) => {
    const selected = Array.from(e.target.files)
    const valid = selected.filter((f) => f.type === 'image/png' || f.type === 'image/jpeg')
    if (valid.length !== selected.length) {
      toast.error('Solo se permiten imágenes PNG o JPG')
    }
    setFiles(valid)
    setPreviews(valid.map((f) => ({ url: URL.createObjectURL(f), name: f.name })))
  }

  const removeFile = (index) => {
    const newFiles = files.filter((_, i) => i !== index)
    const newPreviews = previews.filter((_, i) => i !== index)
    URL.revokeObjectURL(previews[index].url)
    setFiles(newFiles)
    setPreviews(newPreviews)
  }

  const onSubmit = async (data) => {
    if (files.length === 0) {
      toast.error('Debe adjuntar al menos una fotografía del siniestro')
      return
    }
    setSaving(true)
    try {
      const formData = new FormData()
      formData.append('polizaId', data.polizaId)
      formData.append('fechaIncidente', data.fechaIncidente)
      formData.append('descripcion', data.descripcion)
      formData.append('montoEstimado', data.montoEstimado)
      formData.append('ubicacion', data.ubicacion)
      files.forEach((f) => formData.append('fotografias', f))

      const res = await reclamoService.crear(formData)
      const reclamo = res.data?.data || res.data
      setSuccess({
        numeroSeguimiento: reclamo?.numeroSeguimiento || 'N/A',
        fechaIncidente: data.fechaIncidente,
      })
      reset()
      setFiles([])
      setPreviews([])
    } catch (err) {
      toast.error(getErrorMessage(err))
    } finally {
      setSaving(false)
    }
  }

  if (success) {
    return (
      <div className="flex items-center justify-center min-h-[70vh]">
        <div className="card max-w-sm w-full p-8 text-center">
          <div className="relative inline-flex mb-6">
            <div className="w-16 h-16 rounded-full bg-emerald-100 flex items-center justify-center">
              <svg className="w-8 h-8 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <div className="absolute -top-1 -right-1 w-5 h-5 bg-emerald-500 rounded-full flex items-center justify-center">
              <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
              </svg>
            </div>
          </div>
          <h2 className="text-xl font-bold text-slate-900 mb-2">Reclamo enviado con éxito</h2>
          <p className="text-sm text-slate-500 mb-6">
            Su solicitud ha sido procesada y se encuentra en etapa de evaluación técnica.
          </p>

          <div className="bg-slate-50 rounded-xl p-4 mb-6 text-left">
            <div className="flex items-center justify-between mb-3">
              <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Detalles del Trámite</p>
              <span className="text-xs bg-emerald-100 text-emerald-700 font-medium px-2 py-0.5 rounded-full">Recibido</span>
            </div>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-xs text-slate-400 mb-0.5">Tracking ID</p>
                <p className="font-bold text-slate-900 font-mono">{success.numeroSeguimiento}</p>
              </div>
              <button
                onClick={() => {
                  navigator.clipboard.writeText(success.numeroSeguimiento)
                  toast.success('Copiado al portapapeles')
                }}
                className="text-slate-400 hover:text-primary-600 transition-colors"
              >
                <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                </svg>
              </button>
            </div>
            <div className="mt-3 pt-3 border-t border-slate-200">
              <p className="text-xs text-slate-400 mb-0.5">Fecha</p>
              <p className="text-sm font-medium text-slate-700">{success.fechaIncidente}</p>
            </div>
          </div>

          <div className="space-y-2">
            <Button className="w-full" onClick={() => navigate('/dashboard')}>
              ← Volver al inicio
            </Button>
            <Button variant="ghost" className="w-full" onClick={() => navigate('/estado-reclamo')}>
              Consultar estado del reclamo
            </Button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div>
      <Header title="Registro de Siniestro" subtitle="Reclamos › Nuevo Registro" />
      <p className="text-sm text-slate-500 -mt-4 mb-6">Complete el formulario para iniciar la evaluación automatizada de su reclamo.</p>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            <div className="card p-5">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="form-label">Selección de Póliza *</label>
                  {loadingPolizas ? (
                    <div className="form-input flex items-center gap-2 text-slate-400">
                      <div className="w-3 h-3 border border-current border-t-transparent rounded-full animate-spin" />
                      Cargando pólizas...
                    </div>
                  ) : (
                    <select className={`form-input ${errors.polizaId ? 'error' : ''}`}
                      {...register('polizaId', { required: 'Seleccione una póliza activa' })}>
                      <option value="">Seleccione una póliza activa ▼</option>
                      {polizas.map((p) => (
                        <option key={p.id} value={p.id}>{p.numero}</option>
                      ))}
                    </select>
                  )}
                  {errors.polizaId && <p className="form-error">{errors.polizaId.message}</p>}
                  {!loadingPolizas && polizas.length === 0 && (
                    <p className="form-error">No tiene pólizas activas disponibles.</p>
                  )}
                </div>
                <div>
                  <label className="form-label">Fecha del Siniestro *</label>
                  <input type="date" max={today} className={`form-input ${errors.fechaIncidente ? 'error' : ''}`}
                    {...register('fechaIncidente', { required: 'Requerido' })} />
                  {errors.fechaIncidente && <p className="form-error">{errors.fechaIncidente.message}</p>}
                </div>
              </div>
            </div>

            <div className="card p-5">
              <div>
                <label className="form-label">Descripción de los Hechos *</label>
                <textarea
                  rows={4}
                  placeholder="Describa brevemente lo sucedido..."
                  className={`form-input resize-none ${errors.descripcion ? 'error' : ''}`}
                  {...register('descripcion', { required: 'La descripción es obligatoria', maxLength: { value: 1000, message: 'Máx. 1000 caracteres' } })}
                />
                {errors.descripcion && <p className="form-error">{errors.descripcion.message}</p>}
                <p className="text-xs text-slate-400 mt-1">Sea lo más detallado posible para agilizar el proceso de IA.</p>
              </div>
            </div>

            <div className="card p-5 space-y-4">
              <div>
                <label className="form-label">Monto Estimado (USD) *</label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-3 flex items-center text-slate-400 text-sm pointer-events-none">$</span>
                  <input
                    type="number"
                    step="0.01"
                    placeholder="0.00"
                    className={`form-input pl-7 ${errors.montoEstimado ? 'error' : ''}`}
                    {...register('montoEstimado', { required: 'Requerido', min: { value: 0.01, message: 'Debe ser mayor a $0' }, valueAsNumber: true })}
                  />
                </div>
                {errors.montoEstimado && (
                  <p className="form-error flex items-center gap-1">
                    <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                    </svg>
                    {errors.montoEstimado.message}
                  </p>
                )}
              </div>
              <div>
                <label className="form-label">Ubicación del Incidente *</label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-slate-400">
                    <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                      <path strokeLinecap="round" strokeLinejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" /><path strokeLinecap="round" strokeLinejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z" />
                    </svg>
                  </div>
                  <input className={`form-input pl-9 ${errors.ubicacion ? 'error' : ''}`} placeholder="Calle, Ciudad, País"
                    {...register('ubicacion', { required: 'Requerido' })} />
                </div>
                {errors.ubicacion && <p className="form-error">{errors.ubicacion.message}</p>}
              </div>
            </div>

            <Button type="submit" loading={saving} className="w-full" size="lg">
              Enviar Reclamo →
            </Button>
           
          </form>
        </div>

        <div className="space-y-4">
          <div className="card p-5">
            <h3 className="font-semibold text-slate-800 mb-1 flex items-center gap-2">
              <svg className="w-4 h-4 text-primary-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              Evidencia Fotográfica
            </h3>
            <p className="text-xs text-slate-400 mb-4">Adjunte imágenes o haga clic para subir las fotos del daño.</p>

            <label className="block">
              <input type="file" accept="image/png,image/jpeg" multiple className="hidden" onChange={handleFiles} />
              <div className={`border-2 border-dashed rounded-xl p-6 text-center cursor-pointer transition-colors ${
                files.length === 0
                  ? 'border-slate-200 hover:border-primary-300 hover:bg-primary-50'
                  : 'border-emerald-200 bg-emerald-50'
              }`}>
                {files.length === 0 ? (
                  <>
                    <svg className="w-8 h-8 text-slate-300 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                      <path strokeLinecap="round" strokeLinejoin="round" d="M3 16.5v2.25A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75V16.5m-13.5-9L12 3m0 0l4.5 4.5M12 3v13.5" />
                    </svg>
                    <p className="text-xs text-slate-400">Arrastra imágenes o haz clic</p>
                    <p className="text-xs text-red-500 font-medium mt-1">REQUERIDO: Al menos 3 fotos del daño</p>
                  </>
                ) : (
                  <p className="text-xs text-emerald-600 font-medium">{files.length} archivo(s) seleccionado(s) ✓</p>
                )}
              </div>
            </label>

            {previews.length > 0 && (
              <div className="grid grid-cols-2 gap-2 mt-3">
                {previews.map((p, i) => (
                  <div key={i} className="relative group">
                    <img src={p.url} alt={p.name} className="w-full h-20 object-cover rounded-lg" />
                    <button
                      type="button"
                      onClick={() => removeFile(i)}
                      className="absolute top-1 right-1 w-5 h-5 bg-red-500 text-white rounded-full text-xs opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center"
                    >
                      ×
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="card p-5">
            <h3 className="font-semibold text-slate-800 mb-3 text-sm">Protocolo de Revisión</h3>
            <div className="space-y-2">
              {[
                'Recepción y validación de formulario',
                'Evaluación automática',
              ].map((step, i) => (
                <div key={i} className="flex items-start gap-2 text-xs text-slate-500">
                  <div className="w-4 h-4 rounded-full bg-primary-100 text-primary-600 font-bold flex items-center justify-center shrink-0 mt-0.5 text-[10px]">
                    {i + 1}
                  </div>
                  {step}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
