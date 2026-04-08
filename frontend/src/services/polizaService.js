import api from './api'

export const polizaService = {
  listar: (params = {}) =>
    api.get('/polizas', { params }),

  obtener: (id) =>
    api.get(`/polizas/${id}`),

  crear: (data) =>
    api.post('/polizas', data),
}
