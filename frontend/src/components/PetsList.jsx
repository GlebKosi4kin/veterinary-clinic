import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { petsAPI, ownersAPI } from '../services/api';

function PetsList() {
  const navigate = useNavigate();
  const [pets, setPets] = useState([]);
  const [owners, setOwners] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    birthDate: '',
    type: '',
    ownerId: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [petsResponse, ownersResponse] = await Promise.all([
        petsAPI.getAll(),
        ownersAPI.getAll()
      ]);
      setPets(petsResponse.data);
      setOwners(ownersResponse.data);
      setLoading(false);
    } catch (error) {
      console.error('Ошибка при выводе данных:', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await petsAPI.update(editingId, formData);
      } else {
        await petsAPI.create(formData);
      }
      setFormData({ name: '', birthDate: '', type: '', ownerId: '' });
      setShowForm(false);
      setEditingId(null);
      fetchData();
    } catch (error) {
      console.error(`Ошибка ${editingId ? 'обновления' : 'создания'} pet:`, error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить этого питомца?')) {
      try {
        await petsAPI.delete(id);
        fetchData();
      } catch (error) {
        console.error('Ошибка при удалении данных:', error);
      }
    }
  };

  const handleEdit = (pet) => {
    setFormData({
      name: pet.name,
      birthDate: pet.birthDate,
      type: pet.type,
      ownerId: pet.ownerId
    });
    setEditingId(pet.id);
    setShowForm(true);
  };

  const handleCancelEdit = () => {
    setFormData({ name: '', birthDate: '', type: '', ownerId: '' });
    setEditingId(null);
    setShowForm(false);
  };

  const getOwnerName = (ownerId) => {
    const owner = owners.find(o => o.id === ownerId);
    return owner ? `${owner.firstName} ${owner.lastName}` : 'Неизвестно';
  };

  if (loading) return <div>Загрузка...</div>;

  return (
    <div className="container">
      <div className="header-section">
        <h2>Питомцы</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Отмена' : 'Добавить питомца'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="form">
          <h3>{editingId ? 'Редактировать питомца' : 'Добавить нового питомца'}</h3>
          <input
            type="text"
            placeholder="Кличка питомца"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            required
          />
          <input
            type="date"
            placeholder="Дата рождения"
            value={formData.birthDate}
            onChange={(e) => setFormData({ ...formData, birthDate: e.target.value })}
            required
          />
          <select
            value={formData.type}
            onChange={(e) => setFormData({ ...formData, type: e.target.value })}
            required
          >
            <option value="">Выберите тип</option>
            <option value="Собака">Собака</option>
            <option value="Кошка">Кошка</option>
            <option value="Птица">Птица</option>
            <option value="Кролик">Кролик</option>
            <option value="Другое">Другое</option>
          </select>
          <select
            value={formData.ownerId}
            onChange={(e) => setFormData({ ...formData, ownerId: e.target.value })}
            required
          >
            <option value="">Выберите владельца</option>
            {owners.map(owner => (
              <option key={owner.id} value={owner.id}>
                {owner.firstName} {owner.lastName}
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
            <th>Кличка</th>
            <th>Дата рождения</th>
            <th>Тип</th>
            <th>Владелец</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {pets.map((pet) => (
            <tr key={pet.id}>
              <td>{pet.id}</td>
              <td>{pet.name}</td>
              <td>{pet.birthDate}</td>
              <td>{pet.type}</td>
              <td>{getOwnerName(pet.ownerId)}</td>
              <td>
                <button
                  onClick={() => navigate(`/patient-card/${pet.id}`)}
                  className="btn-secondary"
                  style={{ marginRight: '10px' }}
                >
                  Карточка
                </button>
                <button onClick={() => handleEdit(pet)} className="btn-primary" style={{ marginRight: '10px' }}>
                  Изменить
                </button>
                <button onClick={() => handleDelete(pet.id)} className="btn-danger">
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

export default PetsList;
