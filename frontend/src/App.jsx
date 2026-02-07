import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import OwnersList from './components/OwnersList';
import PetsList from './components/PetsList';
import VetsList from './components/VetsList';
import VisitsList from './components/VisitsList';
import PatientCard from './components/PatientCard';

function App() {
  return (
    <Router>
      <div className="app">
        <header className="header">
          <h1>Ветеринарная Клиника</h1>
          <nav className="nav">
            <Link to="/">Владельцы</Link>
            <Link to="/pets">Питомцы</Link>
            <Link to="/vets">Ветеринары</Link>
            <Link to="/visits">Визиты</Link>
          </nav>
        </header>

        <main className="main">
          <Routes>
            <Route path="/" element={<OwnersList />} />
            <Route path="/pets" element={<PetsList />} />
            <Route path="/vets" element={<VetsList />} />
            <Route path="/visits" element={<VisitsList />} />
            <Route path="/patient-card/:petId" element={<PatientCard />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
