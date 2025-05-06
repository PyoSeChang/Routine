import React from 'react';
import Header from './Header';
import Footer from './Footer';
import Routiney from '../components/Emoticon/Routiney';
import Sun from '../components/Emoticon/Sun';

interface Props {
    children: React.ReactNode;
}

const BoardLayout: React.FC<Props> = ({ children }) => {
    return (
        <div className="min-h-screen flex flex-col">
            <Header />

            <div className="absolute left-4 bottom-4 z-40">
                <Routiney />
            </div>

            <div className="absolute top-12 right-4 z-40">
                <Sun />
            </div>

            <main className="flex justify-center mt-[40px] flex-1">
                <div className="flex w-full justify-center max-w-[1200px] px-4">
                    {children}
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default BoardLayout;
