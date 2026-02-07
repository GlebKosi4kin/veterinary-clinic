import { useState, useEffect } from 'react';
import { ownersAPI } from '../services/api';

function OwnersList() {
  const [owners, setOwners] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    address: '',
    city: '',
    telephone: ''
  });

  useEffect(() => {
    fetchOwners();
  }, []);

  const fetchOwners = async () => {
    try {
      const response = await ownersAPI.getAll();
      setOwners(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Ошибка при выборке данных::', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await ownersAPI.update(editingId, formData);
      } else {
        await ownersAPI.create(formData);
      }
      setFormData({ firstName: '', lastName: '', address: '', city: '', telephone: '' });
      setShowForm(false);
      setEditingId(null);
      fetchOwners();
    } catch (error) {
      console.error(`Ошибка ${editingId ? 'обновлении' : 'удалении'} owner:`, error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить этого владельца?')) {
      try {
        await ownersAPI.delete(id);
        fetchOwners();
      } catch (error) {
        console.error('Ошибка при удалении данных::', error);
      }
    }
  };

  const handleEdit = (owner) => {
    setFormData({
      firstName: owner.firstName,
      lastName: owner.lastName,
      address: owner.address,
      city: owner.city,
      telephone: owner.telephone
    });
    setEditingId(owner.id);
    setShowForm(true);
  };

  const handleCancelEdit = () => {
    setFormData({ firstName: '', lastName: '', address: '', city: '', telephone: '' });
    setEditingId(null);
    setShowForm(false);
  };

  if (loading) return <div>Загрузка...</div>;

  return (
    <div className="container">
      <div className="header-section">
        <h2>Владельцы</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Отмена' : 'Добавить владельца'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="form">
          <h3>{editingId ? 'Редактировать владельца' : 'Добавить нового владельца'}</h3>
          <input
            type="text"
            placeholder="Имя"
            value={formData.firstName}
            onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
            required
          />
          <input
            type="text"
            placeholder="Фамилия"
            value={formData.lastName}
            onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
            required
          />
          <input
            type="text"
            placeholder="Адрес"
            value={formData.address}
            onChange={(e) => setFormData({ ...formData, address: e.target.value })}
          />
          <input
            type="text"
            placeholder="Город"
            value={formData.city}
            onChange={(e) => setFormData({ ...formData, city: e.target.value })}
          />
          <input
            type="text"
            placeholder="Телефон"
            value={formData.telephone}
            onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
          />
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
            <th>Имя</th>
            <th>Адрес</th>
            <th>Город</th>
            <th>Телефон</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {owners.map((owner) => (
            <tr key={owner.id}>
              <td>{owner.id}</td>
              <td>{owner.firstName} {owner.lastName}</td>
              <td>{owner.address}</td>
              <td>{owner.city}</td>
              <td>{owner.telephone}</td>
              <td>
                <button onClick={() => handleEdit(owner)} className="btn-primary" style={{ marginRight: '10px' }}>
                  Изменить
                </button>
                <button onClick={() => handleDelete(owner.id)} className="btn-danger">
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

export default OwnersList;
