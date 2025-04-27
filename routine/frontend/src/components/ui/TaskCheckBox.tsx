import React from 'react';

interface TaskCheckBoxProps {
    label: string;
    checked: boolean;
    disabled?: boolean;
    onChange: (checked: boolean) => void;
}

const TaskCheckBox: React.FC<TaskCheckBoxProps> = ({ label, checked, disabled, onChange }) => {
    if (!label.trim()) return null;

    // 체크됐고 disabled인 경우 -> 강제로 disabled 무시
    const isActuallyDisabled = !checked && disabled;

    return (
        <label className="flex items-center w-full h-[30px] pl-2 relative cursor-pointer">
            {/* 기본 체크박스 숨기기 */}
            <input
                type="checkbox"
                className="absolute opacity-0 w-0 h-0"
                checked={checked}
                disabled={isActuallyDisabled}
                onChange={(e) => onChange(e.target.checked)}
            />

            {/* 커스텀 체크박스 */}
            <span
                className="w-5 h-5 border-2 rounded-md flex items-center justify-center border-gray-400 overflow-visible"
            >
  {checked && (
      <img
          src="/icons/check.png"
          alt="checked"
          className="w-6 h-6 transform translate-x-[2px] -translate-y-[10px]"
      />
  )}
</span>


            {/* 라벨 */}
            <span
                className={`ml-2 relative overflow-hidden ${checked ? 'after:w-full' : 'after:w-0'}`}
                style={{
                    display: 'inline-block',
                    transition: 'color 0.3s ease',
                }}
            >
  {label}
                <span
                    className="absolute left-0 bottom-1/2 h-px bg-black transition-all duration-300"
                    style={{
                        transform: 'translateY(50%)',
                        width: checked ? '100%' : '0%',
                    }}
                />
</span>


        </label>
    );
};

export default TaskCheckBox;
