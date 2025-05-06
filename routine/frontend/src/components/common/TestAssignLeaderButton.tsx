import axios from "../../api/axios";

const TestAssignLeaderButton = () => {
    const testAssignLeader = async () => {
        try {
            await axios.put("/circles/1/members/2/assign-leader");
            alert("✅ 리더 위임 성공!");
        } catch (err: any) {
            // 이 부분은 굳이 없어도 되지만, 네가 확인 위해 로그만 출력
            console.log("❌ 서버에서 받은 에러 메시지:", err.response?.data?.message);

            // 인터셉터가 이미 global-error를 dispatch 하니까 여기서 따로 안 해도 됨
        }
    };

    return (
        <button
            onClick={testAssignLeader}
            className="bg-blue-500 text-white px-4 py-2 rounded shadow"
        >
            리더 위임 테스트
        </button>
    );
};

export default TestAssignLeaderButton;
