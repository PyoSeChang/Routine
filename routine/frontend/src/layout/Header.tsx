// src/layout/Header.tsx
import React from 'react';
import { Link } from 'react-router-dom';

const Header: React.FC = () => {
    return (
        <header
            className="w-full text-white py-3 px-6 shadow-md"
            style={{
                backgroundColor: '#724019',
                backgroundImage: 'url("https://www.transparenttextures.com/patterns/dark-wood.png")',
                backgroundRepeat: 'repeat'
            }}
        >
            <div className="max-w-6xl mx-auto flex items-center justify-between">
                <h1 className="text-xl font-bold">
                    <Link to="/">Routine</Link>
                </h1>
                <nav className="space-x-4">
                    <span>|</span>
                    <Link to="/circles" className="hover:underline">Shop</Link>
                    <span>|</span>
                    <Link to="/boards" className="hover:underline">Community</Link>
                    <span>|</span>
                    <Link to="/circles" className="hover:underline">Circle</Link>
                    <span>|</span>
                    <Link to="/routine" className="hover:underline">My Routines</Link>
                    <span>|</span>
                    <Link to="/circles" className="hover:underline">My Page</Link>
                    <span>|</span>
                </nav>
            </div>
        </header>
    );
};

export default Header;