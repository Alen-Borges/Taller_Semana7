import api from './api'

export const aseguradoService = {
  listar: (params = {}) =>
    api.get('/asegurados', { params }),

  obtener: (id) =>
    api.get(`/asegurados/${id}`),

  buscarPorIdentificacion: (identificacion) =>
    api.get('/asegurados', { params: { identificacion } }),

  crear: (data) =>
    api.post('/asegurados', data),

  actualizar: (id, data) =>
    api.put(`/asegurados/${id}`, data),
}
