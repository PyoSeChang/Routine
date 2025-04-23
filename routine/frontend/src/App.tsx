import { BrowserRouter, Routes, Route } from 'react-router-dom';
import  RoutinePage  from './pages/RoutinePage';

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<RoutinePage />} />
        </Routes>
      </BrowserRouter>
  );
}

export default App;