import React from 'react';

interface CustomCheckboxProps {
    checked: boolean;
    onChange: (checked: boolean) => void;
    disabled?: boolean;
}

const CustomCheckbox: React.FC<CustomCheckboxProps> = ({ checked, onChange, disabled = false }) => {
    return (
        <label className="relative flex items-center cursor-pointer">
            {/* 숨겨진 진짜 input */}
            <input
                type="checkbox"
                checked={checked}
                disabled={disabled}
                onChange={(e) => onChange(e.target.checked)}
                className="peer hidden"
            />

            {/* 커스텀 체크박스 */}
            <div
                className={`w-6 h-6 rounded-md border flex items-center justify-center transition-all duration-150
          ${checked ? 'border-blue-500 bg-blue-500' : 'border-gray-300 bg-white'}
          ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
            >
                {/* 체크된 상태면 이미지 표시 */}
                {checked && (
                    <div
                        className="w-4 h-4 bg-center bg-no-repeat bg-contain"
                        style={{ backgroundImage: "url('/icons/check.png')" }}
                    />
                )}
            </div>
        </label>
    );
};

export default CustomCheckbox;
