import { useState, useEffect } from 'react';
import { vetsAPI } from '../services/api';

function VetsList() {
  const [vets, setVets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    specialty: ''
  });

  useEffect(() => {
    fetchVets();
  }, []);

  const fetchVets = async () => {
    try {
      const response = await vetsAPI.getAll();
      setVets(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Ошибка при загрузке ветеринаров:', error);
      alert('Не удалось загрузить список ветеринаров. Пожалуйста, попробуйте позже.');
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await vetsAPI.update(editingId, formData);
        alert('Данные ветеринара успешно обновлены!');
      } else {
        await vetsAPI.create(formData);
        alert('Ветеринар успешно добавлен!');
      }
      setFormData({ firstName: '', lastName: '', specialty: '' });
      setShowForm(false);
      setEditingId(null);
      fetchVets();
    } catch (error) {
      console.error(`Ошибка при ${editingId ? 'обновлении' : 'создании'} ветеринара:`, error);
      alert(`Не удалось ${editingId ? 'обновить' : 'создать'} ветеринара. Пожалуйста, проверьте данные и попробуйте снова.`);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить этого ветеринара?')) {
      try {
        await vetsAPI.delete(id);
        alert('Ветеринар успешно удален!');
        fetchVets();
      } catch (error) {
        console.error('Ошибка при удалении ветеринара:', error);
        alert('Не удалось удалить ветеринара. Пожалуйста, попробуйте позже.');
      }
    }
  };

  const handleEdit = (vet) => {
    setFormData({
      firstName: vet.firstName,
      lastName: vet.lastName,
      specialty: vet.specialty
    });
    setEditingId(vet.id);
    setShowForm(true);
  };

  const handleCancelEdit = () => {
    setFormData({ firstName: '', lastName: '', specialty: '' });
    setEditingId(null);
    setShowForm(false);
  };

  if (loading) return <div>Загрузка...</div>;

  return (
    <div className="container">
      <div className="header-section">
        <h2>Ветеринары</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Отмена' : 'Добавить ветеринара'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="form">
          <h3>{editingId ? 'Редактировать ветеринара' : 'Добавить нового ветеринара'}</h3>
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
            placeholder="Специализация"
            value={formData.specialty}
            onChange={(e) => setFormData({ ...formData, specialty: e.target.value })}
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
            <th>Специализация</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {vets.map((vet) => (
            <tr key={vet.id}>
              <td>{vet.id}</td>
              <td>{vet.firstName} {vet.lastName}</td>
              <td>{vet.specialty}</td>
              <td>
                <button onClick={() => handleEdit(vet)} className="btn-primary" style={{ marginRight: '10px' }}>
                  Изменить
                </button>
                <button onClick={() => handleDelete(vet.id)} className="btn-danger">
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

export default VetsList;