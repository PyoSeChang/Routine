import { BrowserRouter, Routes, Route } from 'react-router-dom';
import RoutinePage from './pages/routine/RoutinePage';
import RoutineDetailPage from "./pages/routine/RoutineDetailPage";
import BoardListPage from './pages/board/BoardListPage';
import BoardFormPage from './pages/board/BoardFormPage';
import BoardDetailPage from './pages/board/BoardDetailPage';
import CirclePage from "./pages/circle/CirclePage";
import CircleFormPage from "./pages/circle/CircleFormPage";
import CircleDetailPage from "./pages/circle/CircleDetailPage";


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
                {/*/!* 서클 보기 *!/*/}
                <Route path="/circles" element={<CirclePage/>} />
                {/*/!* 서클 만들기 }*/}
                <Route path="/circles/create" element={<CircleFormPage/>} />
                {/*/!* 서클 상세 *!/*/}
                <Route path="/circles/:circleId" element={<CircleDetailPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
