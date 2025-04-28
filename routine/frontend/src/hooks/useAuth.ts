// src/hooks/useAuth.ts
import { useState, useEffect } from 'react';
import axios from '../api/axios';

export interface CurrentUser {
    memberId: number;
    nickname: string;
    // …필요한 필드
}

export function useCurrentUser() {
    const [user, setUser] = useState<CurrentUser | null>(null);
    useEffect(() => {
        axios
            .get<CurrentUser>('/members/me')
            .then(res => setUser(res.data))
            .catch(() => setUser(null));
    }, []);
    return user;
}
