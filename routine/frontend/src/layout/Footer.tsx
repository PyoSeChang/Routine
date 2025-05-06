// src/layout/Footer.tsx
import React from 'react';

const Footer: React.FC = () => {
    return (
        <footer className="w-full text-white py-3 px-6 shadow-md"
                style={{
                    backgroundColor: '#724019',
                    backgroundImage: 'url("https://www.transparenttextures.com/patterns/dark-wood.png")',
                    backgroundRepeat: 'repeat',
                }}>
            © {new Date().getFullYear()} Routine. All rights reserved.
        </footer>
    );
};

export default Footer;