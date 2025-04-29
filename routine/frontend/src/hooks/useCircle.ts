// src/hooks/useCircle.ts

import axios from '../api/axios';
import { CircleDTO } from '../types/circle';

export const createCircle = async (circle: CircleDTO) => {
    const res = await axios.post('/circles', circle);
    return res.data;
};

export const fetchMyCircles = async () => {
    const res = await axios.get('/circles/my');
    return res.data;
};

export const fetchCircleDetail = async (circleId: string) => {
    const res = await axios.get(`/circles/${circleId}`);
    return res.data;
};
