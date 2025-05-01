import React from 'react';
import Header from "./Header";
import Footer from "./Footer";

interface Props {
    children: React.ReactNode;
}

const AppLayout: React.FC<Props> = ({ children }) => {
    return (
        <div className="min-h-screen flex flex-col">
            <Header />
            <main className="flex justify-center mt-[100px] bg-mainYellow flex-1">
                <div className="flex w-full max-w-[1600px] bg-mainYellow px-4">
                    {children}
                </div>
            </main>
            <Footer />
        </div>
    );
};

export default AppLayout;
