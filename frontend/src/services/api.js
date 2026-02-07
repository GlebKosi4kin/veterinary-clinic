import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const ownersAPI = {
  getAll: () => api.get('/owners'),
  getById: (id) => api.get(`/owners/${id}`),
  create: (data) => api.post('/owners', data),
  update: (id, data) => api.put(`/owners/${id}`, data),
  delete: (id) => api.delete(`/owners/${id}`),
};


export const petsAPI = {
  getAll: (ownerId) => api.get('/pets', { params: { ownerId } }),
  getById: (id) => api.get(`/pets/${id}`),
  create: (data) => api.post('/pets', data),
  update: (id, data) => api.put(`/pets/${id}`, data),
  delete: (id) => api.delete(`/pets/${id}`),
};

export const vetsAPI = {
  getAll: () => api.get('/vets'),
  getById: (id) => api.get(`/vets/${id}`),
  create: (data) => api.post('/vets', data),
  update: (id, data) => api.put(`/vets/${id}`, data),
  delete: (id) => api.delete(`/vets/${id}`),
};

export const visitsAPI = {
  getAll: (petId) => api.get('/visits', { params: { petId } }),
  getById: (id) => api.get(`/visits/${id}`),
  create: (data) => api.post('/visits', data),
  update: (id, data) => api.put(`/visits/${id}`, data),
  delete: (id) => api.delete(`/visits/${id}`),
};

export const patientCardsAPI = {
  getAll: () => api.get('/patient-cards'),
  getById: (id) => api.get(`/patient-cards/${id}`),
  create: (data) => api.post('/patient-cards', data),
  update: (id, data) => api.put(`/patient-cards/${id}`, data),
  delete: (id) => api.delete(`/patient-cards/${id}`),
};

export default api;
