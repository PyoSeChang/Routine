import Line from './Line';

interface BlankLineProps {
    count?: number;
}

const BlankLine: React.FC<BlankLineProps> = ({ count = 1 }) => {
    return (
        <>
            {Array.from({ length: count }).map((_, i) => (
                <Line key={i} />
            ))}
        </>
    );
};

export default BlankLine;
