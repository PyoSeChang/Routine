import { BrowserRouter, Routes, Route } from 'react-router-dom';
import RoutinePage from './pages/RoutinePage';
import RoutineDetailPage from "./pages/RoutineDetailPage";
import BoardListPage from './pages/BoardListPage';
import BoardFormPage from './pages/BoardFormPage';
import BoardDetailPage from './pages/BoardDetailPage';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/routine" element={<RoutinePage />} />
                <Route path="/routine/:routineId" element={<RoutineDetailPage />} />
                {/* 게시판 목록 */}
                <Route path="/boards" element={<BoardListPage />} />

                {/* 글쓰기 */}
                <Route path="/boards/write" element={<BoardFormPage mode="create" />} />

                {/* 글 수정 */}
                <Route path="/boards/edit/:boardId" element={<BoardFormPage mode="edit" />} />

                {/* 게시글 상세 */}
                <Route path="/boards/:boardId" element={<BoardDetailPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
