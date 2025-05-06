import React, { useEffect, useState } from 'react';
import AppLayout from '../../layout/AppLayout';
import Line from '../../components/ui/note/Line';
import axios from '../../api/axios';
import { useParams } from 'react-router-dom';
import InputOnNote from '../../components/ui/note/InputOnNote';
import BlankLine from "../../components/ui/note/BlankLine";
import NoneLine from "../../components/ui/note/NoneLine";
import NoteBlock from "../../components/ui/note/NoteBlock";

interface PointLogDTO {
    reason: string;
    amount: number;
    createdAt: string;
}

const gridCols = "grid grid-cols-[50%_20%_30%] w-full text-sm";
const cellBase = "h-9 flex items-center px-2";
const center = "justify-center";
const right = "justify-end";

const MyPage: React.FC = () => {
    const { loginId } = useParams();
    const [data, setData] = useState<any | null>(null);
    const [pointLogs, setPointLogs] = useState<PointLogDTO[] | null>(null);
    const [isEditing, setIsEditing] = useState(false);
    const [password, setPassword] = useState('');
    const [form, setForm] = useState({ nickname: '', email: '', password: '', confirmPassword: '' });
    const [passwordMessage, setPasswordMessage] = useState('');
    const [nicknameMessage, setNicknameMessage] = useState('');

    useEffect(() => {
        axios.get(`/member/${loginId}`)
            .then(res => {
                setData(res.data);
                setForm(prev => ({ ...prev, nickname: res.data.nickname, email: res.data.email }));
            })
            .catch(err => console.error('Failed to fetch my page data', err));
    }, [loginId]);

    const handleFetchPoints = () => {
        if (!loginId) return;
        axios.get(`/member/${loginId}/points`)
            .then(res => setPointLogs(res.data))
            .catch(err => console.error('Failed to fetch point logs', err));
    };

    const handlePasswordConfirm = () => {
        if (!loginId) return;
        axios.post(`/member/${loginId}/check-password`, { password })
            .then(() => setIsEditing(true))
            .catch(() => alert('비밀번호가 일치하지 않습니다'));
    };

    const checkNickname = async () => {
        try {
            const res = await axios.get('/member/check-nickname', {
                params: { nickname: form.nickname }
            });
            setNicknameMessage(res.data ? '이미 사용 중인 닉네임입니다.' : '사용 가능한 닉네임입니다.');
        } catch {
            setNicknameMessage('닉네임 확인 중 오류 발생');
        }
    };

    const handleChange = (field: string, value: string) => {
        setForm(prev => ({ ...prev, [field]: value }));

        if (field === 'password' || field === 'confirmPassword') {
            const pw = field === 'password' ? value : form.password;
            const confirm = field === 'confirmPassword' ? value : form.confirmPassword;
            setPasswordMessage(
                pw === confirm ? '비밀번호가 일치합니다.' : '비밀번호가 다릅니다.'
            );
        }
    };

    const handleSubmit = async () => {
        if (!loginId) return;
        try {
            await axios.post(`/member/${loginId}/update`, form);
            alert('회원 정보가 저장되었습니다.');
            setIsEditing(false);
        } catch (err) {
            console.error('회원 정보 저장 실패', err);
            alert('저장에 실패했습니다.');
        }
    };

    if (!data) return <div>Loading...</div>;

    return (
        <AppLayout>
            <div className="w-full max-w-[700px] mx-auto">
                <div className="bg-blue-700 h-6 w-full rounded-t-md shadow-lg " />
                <NoteBlock className="flex justify-center" >
                    <h1 className="mt-5 mb-5 text-bold text-[30px] text-center">My Page</h1>
                </NoteBlock>
                <BlankLine/>
                <Line className="flex justify-between px-4 items-center">
                    <p className="text-xl font-bold">
                        내 포인트: <span className="text-red-500">{data.myPoints}P</span>
                    </p>

                </Line>
                <Line>
                    <button
                        onClick={handleFetchPoints}
                        className="text-sm text-blue-600 hover:underline whitespace-nowrap"
                    >
                        [적립 내역 조회하기]
                    </button>
                </Line>

                {pointLogs && (
                    <>
                        <Line>
                            <div className={`${gridCols} text-gray-700 font-semibold`}>
                                <span className={cellBase}>사유</span>
                                <span className={`${cellBase} ${center}`}>포인트</span>
                                <span className={`${cellBase} ${right}`}>지급일</span>
                            </div>
                        </Line>
                        {pointLogs.map((log, idx) => (
                            <Line key={idx}>
                                <div className={`${gridCols} text-gray-800`}>
                                    <span className={cellBase}>{log.reason}</span>
                                    <span className={`${cellBase} ${center}`}>+{log.amount}P</span>
                                    <span className={`${cellBase} ${right} text-gray-500 text-xs`}>{log.createdAt}</span>
                                </div>
                            </Line>
                        ))}
                    </>
                )}
                <BlankLine/>
                <Line className="px-4">
                    <p className="text-xl font-bold">회원정보:</p>
                </Line>

                {!isEditing ? (
                    <>
                        <InputOnNote label="닉네임: " value={form.nickname} onChange={() => {}} disabled />
                        <InputOnNote label="이메일: " value={form.email} onChange={() => {}} disabled />

                        <Line indent={false} className="gap-2">
                            <div className="flex-1">
                                <InputOnNote
                                    label="비밀번호 입력: "
                                    value={password}
                                    onChange={setPassword}
                                    type="password"
                                />
                            </div>
                            <button
                                onClick={handlePasswordConfirm}
                                className="text-sm text-blue-600 hover:underline shrink-0"
                            >
                                [수정하러 가기]
                            </button>
                        </Line>

                    </>
                ) : (
                    <>
                        <InputOnNote label="닉네임: " value={form.nickname} onChange={v => handleChange('nickname', v)} onBlur={checkNickname} />
                        <Line>
                            <p className={`text-sm ml-2 ${nicknameMessage.includes("가능") ? "text-blue-600" : "text-red-500"}`}>
                                {nicknameMessage}
                            </p>
                        </Line>
                        <InputOnNote label="이메일: " value={form.email} onChange={v => handleChange('email', v)} />
                        <InputOnNote label="비밀번호: " type="password" value={form.password} onChange={v => handleChange('password', v)} />
                        <InputOnNote label="비밀번호 확인: " type="password" value={form.confirmPassword} onChange={v => handleChange('confirmPassword', v)} />
                        <Line>
                            <p className={`text-sm ml-2 ${passwordMessage.includes("일치") ? "text-blue-600" : "text-red-500"}`}>
                                {passwordMessage}
                            </p>
                        </Line>
                        <Line>
                            <button
                                onClick={handleSubmit}
                                className="text-sm text-blue-600 hover:underline"
                            >
                                [저장하기]
                            </button>
                        </Line>
                    </>
                )}
                <BlankLine/>
                <NoteBlock>
                    <div></div>
                </NoteBlock>
            </div>
        </AppLayout>
    );
};

export default MyPage;