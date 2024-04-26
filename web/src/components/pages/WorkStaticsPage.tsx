import React, { useState, useEffect } from "react";
import WorkStaticsGraph from "../atoms/WorkStaticsGraph";

interface DataItem {
  name: string;
  values: number[];
}
const WorkStaticsPage = () => {
  const initialData: DataItem[] = [
    { name: "5월 1주", values: [0, 0, 0, 0] },
    { name: "5월 2주", values: [0, 0, 0, 0] },
    { name: "5월 3주", values: [0, 0, 0, 0] },
    { name: "5월 4주", values: [0, 0, 0, 0] },
    { name: "6월 1주", values: [0, 0, 0, 0] },
  ];

  // 데이터 상태 및 업데이트 함수 설정
  const [data, setData] = useState<DataItem[]>(initialData);

  useEffect(() => {
    // 5초마다 데이터를 변경하는 함수
    const updateData = () => {
      const newData: DataItem[] = [
        { name: "7월 1주", values: getRandomValues() },
        { name: "7월 2주", values: getRandomValues() },
        { name: "7월 3주", values: getRandomValues() },
        { name: "7월 4주", values: getRandomValues() },
        { name: "8월 1주", values: getRandomValues() },
      ];
      setData(newData);
    };

    // 5초마다 데이터 업데이트 함수 실행
    const interval = setInterval(updateData, 5000);

    // 컴포넌트 언마운트 시 clearInterval을 사용하여 setInterval 정리
    return () => clearInterval(interval);
  }, []);
  const getRandomValues = () => {
    return Array.from({ length: 4 }, () => Math.floor(Math.random() * 7));
  };

  return (
    <>
      <div>[에그로그] 근무통계그래프 넣어볼게!</div>
      <WorkStaticsGraph data={data} />
    </>
  );
};

export default WorkStaticsPage;
