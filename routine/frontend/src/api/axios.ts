import axios from 'axios';

const instance = axios.create({
    baseURL: '/api', // 필요에 따라 조절
    // withCredentials: true, ← 세션 기반일 때만 필요. JWT는 필요 없음.
});

instance.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers.Authorization = token.startsWith("Bearer ") ? token : `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

instance.interceptors.response.use(
    res => res,
    err => {
        const msg = err.response?.data?.message ?? "알 수 없는 오류입니다.";
        window.dispatchEvent(new CustomEvent("global-error", { detail: msg }));
        return Promise.reject(err);
    }
);

export default instance;
