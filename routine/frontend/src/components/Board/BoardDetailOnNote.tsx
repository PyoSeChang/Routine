import Line from '../ui/note/Line';
import TagOnNote from '../ui/note/TagOnNote';
import { format } from 'date-fns';
import { BoardDTO } from '../../types/board';
import NoneLine from '../ui/note/NoneLine';

interface DisplayDate {
    label: string;
    date: string;
}

interface Props {
    board: BoardDTO;
    displayDate: DisplayDate;
    canModify: boolean;
    onEdit: () => void;
    onDelete: () => void;
}

const BoardDetailOnNote: React.FC<Props> = ({
                                                board,
                                                displayDate,
                                                canModify,
                                                onEdit,
                                                onDelete,
                                            }) => {
    const contentLines = board.content.split('\n');
    const paddedContent = [...contentLines];

    while (paddedContent.length < 10) {
        paddedContent.push(''); // 빈 줄 추가
    }

    return (
        <div className="shadow-lg ">

            {/* 오른쪽으로 전체 내용 밀기 */}
            <div>
                <div className="bg-blue-700 h-6 w-full rounded-t-md shadow-md" />
                <NoneLine />

                <Line>
                    <h2 className="text-xl font-bold text-center">{board.title}</h2>
                </Line>

                <Line>
                    <div className="text-sm text-gray-600">
                        작성자: {board.writerNickname} &nbsp;|&nbsp;
                        종류: {board.boardType} &nbsp;|&nbsp;
                        카테고리: {board.category} / {board.detailCategory} &nbsp;|&nbsp;
                        작성일: {format(new Date(displayDate.date), 'yyyy-MM-dd HH:mm')} &nbsp;|&nbsp;
                        조회수: {board.viewCount ?? '-'}
                    </div>
                </Line>

                {paddedContent.map((line: string, idx: number) => (
                    <Line key={idx}>
                        <p className="whitespace-pre-wrap break-words font-user">{line || <>&nbsp;</>}</p>
                    </Line>
                ))}

                {board.tags && (
                    <>
                        <Line>
                            <div className="mt-2 mb-2 text-sm font-semibold">태그</div>
                        </Line>
                        <Line>
                            <div className="flex flex-wrap gap-2">
                                {board.tags.split(',').map((tag: string) => (
                                    <TagOnNote key={tag} tag={tag.trim()} />
                                ))}
                            </div>
                        </Line>
                    </>
                )}

                <NoneLine />
                <NoneLine />

                {canModify && (
                    <NoneLine>
                        <div className="w-full flex justify-center mb-4">
                            <div className="flex gap-8 text-sm font-semibold">
                                <button onClick={onEdit} className="text-blue-800 hover:underline">
                                    수정
                                </button>
                                <button onClick={onDelete} className="text-red-700 hover:underline">
                                    삭제
                                </button>
                            </div>
                        </div>
                    </NoneLine>
                )}
            </div>
        </div>
    );
};

export default BoardDetailOnNote;
