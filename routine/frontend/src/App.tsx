import { BrowserRouter, Routes, Route } from 'react-router-dom';
import RoutinePage from './pages/RoutinePage';
import RoutineDetailPage from "./pages/RoutineDetailPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/routine" element={<RoutinePage />} />
                <Route path="/routine/:routineId" element={<RoutineDetailPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
