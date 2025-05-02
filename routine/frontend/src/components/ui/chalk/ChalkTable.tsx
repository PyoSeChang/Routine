// ChalkTable.tsx
import React from 'react';

interface Column {
    key: string;
    label: string;
    render?: (item: any) => React.ReactNode;
}

interface Props {
    columns: Column[];
    data: any[];
    className?: string;
}

const ChalkTable: React.FC<Props> = ({ columns, data, className = '' }) => {
    return (
        <table className={`w-full table-auto border-collapse text-white ${className}`}>
            <thead>
            <tr>
                {columns.map((col) => (
                    <th
                        key={col.key}
                        className="border-4 border-white px-4 py-2 text-left font-chalk text-white"
                        style={{
                            boxShadow: '0 0 2px white, 0 0 4px white',
                            filter: 'blur(0.3px)',
                        }}
                    >
                        {col.label}
                    </th>
                ))}
            </tr>
            </thead>
            <tbody>
            {data.map((item, idx) => (
                <tr key={idx} className="hover:bg-white/10">
                    {columns.map((col) => (
                        <td
                            key={col.key}
                            className="border-4 border-white px-4 py-2 font-chalk text-white"
                            style={{
                                boxShadow: '0 0 2px white, 0 0 4px white',
                                filter: 'blur(0.3px)',
                            }}
                        >
                            {col.render ? col.render(item) : item[col.key]}
                        </td>
                    ))}
                </tr>
            ))}
            </tbody>
        </table>
    );
};

export default ChalkTable;
