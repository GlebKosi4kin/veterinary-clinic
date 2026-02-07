import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { patientCardsAPI, petsAPI, visitsAPI, vetsAPI, ownersAPI } from '../services/api';

function PatientCard() {
  const { petId } = useParams();
  const navigate = useNavigate();
  const [pet, setPet] = useState(null);
  const [owner, setOwner] = useState(null);
  const [patientCard, setPatientCard] = useState(null);
  const [visits, setVisits] = useState([]);
  const [vets, setVets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [notes, setNotes] = useState('');

  useEffect(() => {
    fetchData();
  }, [petId]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [petsResponse, visitsResponse, vetsResponse] = await Promise.all([
        petsAPI.getAll(),
        visitsAPI.getAll(petId),
        vetsAPI.getAll()
      ]);

      const currentPet = petsResponse.data.find(p => p.id === parseInt(petId));
      if (!currentPet) {
        console.error('Питомец не найден');
        setLoading(false);
        return;
      }

      setPet(currentPet);
      setVisits(visitsResponse.data.sort((a, b) => new Date(b.visitDate) - new Date(a.visitDate)));
      setVets(vetsResponse.data);

      const ownersResponse = await ownersAPI.getAll();
      const petOwner = ownersResponse.data.find(o => o.id === currentPet.ownerId);
      setOwner(petOwner);

      try {
        const cardsResponse = await patientCardsAPI.getAll();
        const card = cardsResponse.data.find(c => c.petId === parseInt(petId));
        if (card) {
          setPatientCard(card);
          setNotes(card.notes || '');
        }
      } catch (error) {
        console.log('Карточка не найдена');
      }

      setLoading(false);
    } catch (error) {
      console.error('Ошибка при выборке данных:', error);
      setLoading(false);
    }
  };

  const handleSaveNotes = async () => {
    try {
      if (patientCard) {
        // Update existing card
        await patientCardsAPI.update(patientCard.id, { petId: parseInt(petId), notes });
      } else {
        // Create new card
        const created = await patientCardsAPI.create({ petId: parseInt(petId), notes });
        setPatientCard(created.data);
      }
      setEditing(false);
      fetchData();
    } catch (error) {
      console.error('Ошибка при сохранении данных:', error);
      alert('Ошибка при выборке данных:');
    }
  };

  const handleCancelEdit = () => {
    setNotes(patientCard?.notes || '');
    setEditing(false);
  };

  const getVetName = (vetId) => {
    const vet = vets.find(v => v.id === vetId);
    return vet ? `${vet.firstName} ${vet.lastName}` : 'Неизвестно';
  };

  if (loading) return <div className="container">Загрузка...</div>;

  if (!pet) {
    return (
      <div className="container">
        <h2>Пациент не найден</h2>
        <button onClick={() => navigate('/pets')} className="btn-primary">
          Назад к питомцам
        </button>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="header-section">
        <h2>Карточка пациента: {pet.name}</h2>
        <button onClick={() => navigate('/pets')} className="btn-secondary">
          Назад к питомцам
        </button>
      </div>

      {/* Pet Information */}
      <div style={{ backgroundColor: '#f9f9f9', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
        <h3 style={{ marginTop: 0 }}>Информация о пациенте</h3>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
          <div>
            <strong>Кличка:</strong> {pet.name}
          </div>
          <div>
            <strong>Тип:</strong> {pet.type}
          </div>
          <div>
            <strong>Дата рождения:</strong> {pet.birthDate}
          </div>
          <div>
            <strong>Владелец:</strong> {owner ? `${owner.firstName} ${owner.lastName}` : 'Неизвестно'}
          </div>
          {owner && (
            <>
              <div>
                <strong>Телефон владельца:</strong> {owner.telephone || 'Н/Д'}
              </div>
              <div>
                <strong>Адрес владельца:</strong> {owner.address ? `${owner.address}, ${owner.city}` : 'Н/Д'}
              </div>
            </>
          )}
        </div>
      </div>

      {/* Patient Notes */}
      <div style={{ backgroundColor: '#fff', padding: '20px', border: '1px solid #ddd', borderRadius: '8px', marginBottom: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' }}>
          <h3 style={{ margin: 0 }}>Медицинские заметки</h3>
          {!editing && (
            <button onClick={() => setEditing(true)} className="btn-primary">
              {patientCard ? 'Редактировать заметки' : 'Добавить заметки'}
            </button>
          )}
        </div>

        {editing ? (
          <div>
            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              rows="8"
              style={{ width: '100%', padding: '10px', resize: 'vertical', fontSize: '14px' }}
              placeholder="Введите медицинские заметки, аллергии, особые указания и т.д."
            />
            <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
              <button onClick={handleSaveNotes} className="btn-primary">
                Сохранить заметки
              </button>
              <button onClick={handleCancelEdit} className="btn-secondary">
                Отмена
              </button>
            </div>
          </div>
        ) : (
          <div style={{ whiteSpace: 'pre-wrap', lineHeight: '1.6' }}>
            {notes || <em style={{ color: '#999' }}>Медицинских заметок не записано.</em>}
          </div>
        )}
      </div>

      {/* Visit History */}
      <div style={{ backgroundColor: '#fff', padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
        <h3 style={{ marginTop: 0 }}>История визитов ({visits.length})</h3>
        {visits.length === 0 ? (
          <p style={{ color: '#666', fontStyle: 'italic' }}>У этого пациента не записано визитов.</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Дата</th>
                <th>Ветеринар</th>
                <th>Описание</th>
              </tr>
            </thead>
            <tbody>
              {visits.map(visit => (
                <tr key={visit.id}>
                  <td>{visit.visitDate}</td>
                  <td>{getVetName(visit.vetId)}</td>
                  <td>{visit.description || '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default PatientCard;
