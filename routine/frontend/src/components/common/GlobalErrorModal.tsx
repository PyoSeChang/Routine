// 2. GlobalErrorModal.tsx
import { useEffect, useState } from 'react';

const GlobalErrorModal = () => {
    const [message, setMessage] = useState<string | null>(null);

    useEffect(() => {
        const handler = (e: CustomEvent<string>) => {
            setMessage(e.detail);
            setTimeout(() => setMessage(null), 4000); // 4초 후 자동 닫힘
        };
        window.addEventListener("global-error", handler as EventListener);

        return () => window.removeEventListener("global-error", handler as EventListener);
    }, []);

    if (!message) return null;

    return (
        <div className="fixed bottom-10 right-10 bg-yellow-200 border border-yellow-500 rounded-lg px-4 py-3 shadow-lg text-sm z-50">
            <strong className="font-ui text-red-700">⚠️ 오류:</strong> {message}
        </div>
    );
};

export default GlobalErrorModal;
