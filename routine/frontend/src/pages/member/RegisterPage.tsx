import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import InputOnNote from "../../components/ui/note/InputOnNote";
import TagInputOnNote from "../../components/ui/note/TagInputOnNote";
import axios from "../../api/axios";
import NoteBlock from "../../components/ui/note/NoteBlock";
import Line from "../../components/ui/note/Line";

const RegisterPage: React.FC = () => {
    const [form, setForm] = useState({
        loginId: "",
        password: "",
        confirmPassword: "",
        nickname: "",
        phone: "",
        email: "",
    });

    const [idMessage, setIdMessage] = useState("");
    const [nicknameMessage, setNicknameMessage] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");

    const handleChange = (field: keyof typeof form, value: string | string[]) => {
        setForm(prev => ({ ...prev, [field]: value }));

        if (field === "password" || field === "confirmPassword") {
            const pw = field === "password" ? value : form.password;
            const confirm = field === "confirmPassword" ? value : form.confirmPassword;
            setPasswordMessage(
                pw === confirm ? "비밀번호가 일치합니다." : "비밀번호가 다릅니다."
            );
        }
    };

    const checkLoginId = async () => {
        try {
            const res = await axios.get("/member/check-id", {
                params: { loginId: form.loginId }
            });
            setIdMessage(res.data ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
        } catch {
            setIdMessage("아이디 확인 중 오류 발생");
        }
    };

    const checkNickname = async () => {
        try {
            const res = await axios.get("/member/check-nickname", {
                params: { nickname: form.nickname }
            });
            setNicknameMessage(res.data ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.");
        } catch {
            setNicknameMessage("닉네임 확인 중 오류 발생");
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            await axios.post("/member/register", {
                ...form,
            });
            alert("회원가입이 완료되었습니다!");
        } catch (err) {
            console.error("회원가입 실패", err);
        }
    };

    return (
        <AppLayout>
            <div className="flex flex-col items-center justify-center min-h-screen p-4">
                <form onSubmit={handleSubmit} className="w-full max-w-md ">
                    <div className="bg-blue-700 h-6 w-full rounded-t-md shadow-lg " />

                    <NoteBlock className="justify-center pt-6">
                        <h1 className="text-2xl font-bold mb-6 ">Routine 가입 신청서</h1>
                    </NoteBlock>

                    <InputOnNote
                        type="text"
                        label="아이디: "
                        value={form.loginId}
                        onChange={v => handleChange("loginId", v)}
                        onBlur={checkLoginId}
                    />
                    <Line>
                        <p className={`text-sm ml-2 ${idMessage.includes("가능") ? "text-blue-600" : "text-red-500"}`}>
                            {idMessage}
                        </p>
                    </Line>

                    <InputOnNote
                        type="password"
                        label="비밀번호: "
                        value={form.password}
                        onChange={v => handleChange("password", v)}
                    />

                    <InputOnNote
                        type="password"
                        label="비밀번호 확인: "
                        value={form.confirmPassword}
                        onChange={v => handleChange("confirmPassword", v)}
                    />
                    <Line>
                        <p className={`text-sm ml-2 ${passwordMessage.includes("일치") ? "text-blue-600" : "text-red-500"}`}>
                            {passwordMessage}
                        </p>
                    </Line>

                    <InputOnNote
                        type="text"
                        label="닉네임: "
                        value={form.nickname}
                        onChange={v => handleChange("nickname", v)}
                        onBlur={checkNickname}
                    />
                    <Line>
                        <p className={`text-sm ml-2 ${nicknameMessage.includes("가능") ? "text-blue-600" : "text-red-500"}`}>
                            {nicknameMessage}
                        </p>
                    </Line>

                    <InputOnNote
                        type="text"
                        label="전화번호: "
                        value={form.phone}
                        onChange={v => handleChange("phone", v)}
                    />

                    <InputOnNote
                        type="email"
                        label="이메일: "
                        value={form.email}
                        onChange={v => handleChange("email", v)}
                    />


                    <NoteBlock className="pt-8 pb-2">
                        <button
                            type="submit"
                            className="w-full  text-black p-2 rounded hover:underline"
                        >
                            가입하기
                        </button>
                    </NoteBlock>
                </form>
            </div>
        </AppLayout>
    );
};

export default RegisterPage;
