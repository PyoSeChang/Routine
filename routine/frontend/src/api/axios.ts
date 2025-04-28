import axios from 'axios';

const instance = axios.create({
    baseURL: '/api', // Spring Boot 서버 기준
    withCredentials: true, // 세션 쿠키 유지
});

export default instance;
