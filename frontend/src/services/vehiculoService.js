import api from './api'

export const vehiculoService = {
  listar: (params = {}) =>
    api.get('/vehiculos', { params }),

  obtener: (id) =>
    api.get(`/vehiculos/${id}`),

  buscarPorPlaca: (placa) =>
    api.get('/vehiculos', { params: { placa } }),

  crear: (data) =>
    api.post('/vehiculos', data),

  actualizar: (id, data) =>
    api.put(`/vehiculos/${id}`, data),
}
