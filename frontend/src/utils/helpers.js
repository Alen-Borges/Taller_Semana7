export const formatCurrency = (amount) => {
  if (amount == null) return '—'
  return new Intl.NumberFormat('es-EC', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
  }).format(amount)
}

export const formatDate = (dateStr) => {
  if (!dateStr) return '—'
  const date = new Date(dateStr)
  return new Intl.DateTimeFormat('es-EC', {
    day: '2-digit', month: 'short', year: 'numeric',
  }).format(date)
}

export const estadoReclamoColor = (estado) => {
  const map = {
    REGISTRADO:        'bg-blue-100 text-blue-700',
    APROBADO:          'bg-emerald-100 text-emerald-700',
    DESCARTADO:        'bg-slate-100 text-slate-600',
    EN_REVISION_MANUAL:'bg-amber-100 text-amber-700',
    RECHAZADO:         'bg-red-100 text-red-700',
  }
  return map[estado] || 'bg-slate-100 text-slate-600'
}

export const estadoPolizaColor = (estado) => {
  const map = {
    ACTIVA:   'bg-emerald-100 text-emerald-700',
    INACTIVA: 'bg-slate-100 text-slate-600',
    VENCIDA:  'bg-red-100 text-red-600',
  }
  return map[estado] || 'bg-slate-100 text-slate-600'
}

export const getErrorMessage = (error) => {
  if (!error?.response) return 'Error de conexión. Verifica que el servidor esté activo.'
  const data = error.response.data
  if (data?.message) return data.message
  if (data?.errors?.length) return data.errors.join(', ')
  return `Error ${error.response.status}`
}
