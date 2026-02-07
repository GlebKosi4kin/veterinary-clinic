import { useState, useEffect } from 'react';
import { visitsAPI, petsAPI, vetsAPI } from '../services/api';

function VisitsList() {
  const [visits, setVisits] = useState([]);
  const [pets, setPets] = useState([]);
  const [vets, setVets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    visitDate: '',
    description: '',
    petId: '',
    vetId: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [visitsResponse, petsResponse, vetsResponse] = await Promise.all([
        visitsAPI.getAll(),
        petsAPI.getAll(),
        vetsAPI.getAll()
      ]);
      setVisits(visitsResponse.data);
      setPets(petsResponse.data);
      setVets(vetsResponse.data);
      setLoading(false);
    } catch (error) {
      console.error('Ошибка при выборке данных: ', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await visitsAPI.update(editingId, formData);
      } else {
        await visitsAPI.create(formData);
      }
      setFormData({ visitDate: '', description: '', petId: '', vetId: '' });
      setShowForm(false);
      setEditingId(null);
      fetchData();
    } catch (error) {
      console.error(`Ошибка ${editingId ? 'обновление' : 'создание'} visit:`, error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить этот визит?')) {
      try {
        await visitsAPI.delete(id);
        fetchData();
      } catch (error) {
        console.error('Ошибка при удалении данных:', error);
      }
    }
  };

  const handleEdit = (visit) => {
    setFormData({
      visitDate: visit.visitDate,
      description: visit.description,
      petId: visit.petId,
      vetId: visit.vetId
    });
    setEditingId(visit.id);
    setShowForm(true);
  };

  const handleCancelEdit = () => {
    setFormData({ visitDate: '', description: '', petId: '', vetId: '' });
    setEditingId(null);
    setShowForm(false);
  };

  const getPetName = (petId) => {
    const pet = pets.find(p => p.id === petId);
    return pet ? pet.name : 'Неизвестно';
  };

  const getVetName = (vetId) => {
    const vet = vets.find(v => v.id === vetId);
    return vet ? `${vet.firstName} ${vet.lastName}` : 'Неизвестно';
  };

  if (loading) return <div>Загрузка...</div>;

  return (
    <div className="container">
      <div className="header-section">
        <h2>Визиты</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Отмена' : 'Добавить визит'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="form">
          <h3>{editingId ? 'Редактировать визит' : 'Добавить новый визит'}</h3>
          <input
            type="date"
            placeholder="Дата визита"
            value={formData.visitDate}
            onChange={(e) => setFormData({ ...formData, visitDate: e.target.value })}
            required
          />
          <textarea
            placeholder="Описание"
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            rows="3"
          />
          <select
            value={formData.petId}
            onChange={(e) => setFormData({ ...formData, petId: e.target.value })}
            required
          >
            <option value="">Выберите питомца</option>
            {pets.map(pet => (
              <option key={pet.id} value={pet.id}>
                {pet.name} ({pet.type})
              </option>
            ))}
          </select>
          <select
            value={formData.vetId}
            onChange={(e) => setFormData({ ...formData, vetId: e.target.value })}
            required
          >
            <option value="">Выберите ветеринара</option>
            {vets.map(vet => (
              <option key={vet.id} value={vet.id}>
                Др. {vet.firstName} {vet.lastName} - {vet.specialty}
              </option>
            ))}
          </select>
          <div style={{ display: 'flex', gap: '10px' }}>
            <button type="submit" className="btn-primary">
              {editingId ? 'Обновить' : 'Создать'}
            </button>
            <button type="button" onClick={handleCancelEdit} className="btn-secondary">
              Отмена
            </button>
          </div>
        </form>
      )}

      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Дата</th>
            <th>Питомец</th>
            <th>Ветеринар</th>
            <th>Описание</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {visits.map((visit) => (
            <tr key={visit.id}>
              <td>{visit.id}</td>
              <td>{visit.visitDate}</td>
              <td>{getPetName(visit.petId)}</td>
              <td>{getVetName(visit.vetId)}</td>
              <td>{visit.description}</td>
              <td>
                <button onClick={() => handleEdit(visit)} className="btn-primary" style={{ marginRight: '10px' }}>
                  Изменить
                </button>
                <button onClick={() => handleDelete(visit.id)} className="btn-danger">
                  Удалить
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default VisitsList;
