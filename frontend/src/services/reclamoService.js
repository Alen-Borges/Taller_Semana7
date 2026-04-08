import api from './api'

export const reclamoService = {
  crear: (formData) =>
    api.post('/reclamos', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    }),

  consultarEstado: (numeroSeguimiento) =>
    api.get(`/reclamos/${numeroSeguimiento}/estado`),

  listarEscalados: () =>
    api.get('/reclamos/escalados'),
}
