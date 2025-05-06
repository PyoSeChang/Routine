// src/pages/LoginPage.tsx
import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import ChalkButton from "../../components/ui/chalk/ChalkButton";
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";


const LoginPage: React.FC = () => {
    const [loginId, setLoginId] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await axios.post("/login", { loginId, password });
            const token = response.headers["authorization"];

            console.log("🎟️ 받은 토큰:", token);


            if (token) {
                localStorage.setItem("accessToken", token);
                console.log("🧊 저장된 토큰:", localStorage.getItem("accessToken"));
                navigate("/routine");
            }
        } catch (err) {
            window.dispatchEvent(new CustomEvent("global-error", {
                detail: "로그인에 실패했습니다."
            }));
        }
    };

    return (
        <AppLayout>
            <div className="flex items-center justify-center min-h-screen">
                <form onSubmit={handleSubmit} className="w-full max-w-[700px] p-8">
                    <h1 className="text-[52px] mb-6 font-chalk text-center text-black">Login</h1>

                    {/* 필드 + 버튼 수평 배치 */}
                    <div className="flex items-end gap-4">
                        {/* ID + PASSWORD 필드 수직 배치 */}
                        <div className="flex-1 space-y-4">
                            <div className="flex flex-col">
                                <label className="font-chalk text-white text-[40px]">ID</label>
                                <input
                                    type="text"
                                    className="border p-3 rounded text-lg"
                                    placeholder="아이디 입력"
                                    value={loginId}
                                    onChange={(e) => setLoginId(e.target.value)}
                                />
                            </div>
                            <div className="flex flex-col">
                                <label className="font-chalk text-white text-[40px]">PASSWORD</label>
                                <input
                                    type="password"
                                    className="border p-3 rounded text-lg"
                                    placeholder="비밀번호 입력"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </div>
                        </div>

                        {/* 로그인 버튼 */}
                        <div className="self-center">
                            <ChalkButton type="submit" className="h-full px-6">
                                Go !
                            </ChalkButton>
                        </div>
                    </div>
                    <Link to="/member/register" className="font-chalk text-white text-[24px] block mt-2">
                        Register
                    </Link>
                </form>

            </div>



        </AppLayout>
    );
};

export default LoginPage;
