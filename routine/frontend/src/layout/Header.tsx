import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
    sub: string;
    role: string;
    id: number;
    exp: number;
}

const Header: React.FC = () => {
    const navigate = useNavigate();

    const token = localStorage.getItem('accessToken');
    let isLoggedIn = false;
    let role = '';
    let loginId = '';

    if (token) {
        try {
            const decoded = jwtDecode<DecodedToken>(token);
            console.log('decoded:', decoded);

            isLoggedIn = true;
            role = decoded.role;       // "ADMIN" 또는 "USER"
            loginId = decoded.sub;     // 로그인 ID
        } catch (err) {
            console.error('Invalid token:', err);
        }
    }

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        navigate('/login');
    };

    return (
        <header
            className="w-full text-white py-3 px-6 shadow-md"
            style={{
                backgroundColor: '#724019',
                backgroundImage: 'url("https://www.transparenttextures.com/patterns/dark-wood.png")',
                backgroundRepeat: 'repeat',
            }}
        >
            <div className="max-w-6xl mx-auto flex items-center justify-between">
                <h1 className="text-[32px] font-bold">
                    <Link to="/">Routine</Link>
                </h1>
                <nav className="space-x-4">
                    <span>|</span>
                    <a
                        href="http://localhost:8080/product/list"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="hover:underline"
                    >
                        Shop
                    </a>
                    <span>|</span>
                    <Link to="/boards" className="hover:underline">Community</Link>
                    <span>|</span>
                    <Link to="/circles" className="hover:underline">Circle</Link>
                    <span>|</span>
                    <Link to="/routine" className="hover:underline">My Routines</Link>
                    <span>|</span>

                    {isLoggedIn ? (
                        <>
                            <Link
                                to={role === 'ADMIN' ? '/admin' : `/mypage/${loginId}`}
                                className="hover:underline"
                            >
                                {role === 'ADMIN' ? 'Admin' : 'My Page'}
                            </Link>
                            <span>|</span>
                            <button onClick={handleLogout} className="hover:underline">Logout</button>
                            <span>|</span>
                        </>
                    ) : (
                        <>
                            <Link to="/login" className="hover:underline">Login</Link>
                            <span>|</span>
                        </>
                    )}
                </nav>
            </div>
        </header>
    );
};

export default Header;
