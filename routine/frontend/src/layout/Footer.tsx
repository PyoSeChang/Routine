// src/layout/Footer.tsx
import React from 'react';

const Footer: React.FC = () => {
    return (
        <footer className="w-full bg-amber-900 text-center text-sm text-white py-4 mt-12">
            © {new Date().getFullYear()} Routine. All rights reserved.
        </footer>
    );
};

export default Footer;