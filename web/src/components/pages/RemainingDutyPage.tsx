import React, { useRef, useEffect, useState, useMemo } from "react";
import RemainingDutyGraph from "../atoms/RemainingDutyGraph";

interface TreeNode {
  name: string;
  remain?: number;
  value?: number;
  children?: TreeNode[];
  colname?: string;
  color?: string;
}

<<<<<<< Updated upstream
// const remainingDuty = [
//   { name: "Day", value: 0, color: "#18C5B5" },
//   { name: "Eve", value: 0, color: "#F4D567" },
//   { name: "Night", value: 0, color: "#485E88" },
//   { name: "Off", value: 0, color: "#9B8AFB" },
//   { name: "Vacation", value: 0, color: "#FDA29B" },
//   { name: "Edu", value: 0, color: "#98A2B3" },
// ];
=======
const remainingDuty = [
  { name: "Day", value: 0, color: "#18C5B5" },
  { name: "Eve", value: 0, color: "#F4D567" },
  { name: "Night", value: 0, color: "#485E88" },
  { name: "Off", value: 0, color: "#9B8AFB" },
  { name: "Vacation", value: 0, color: "#FDA29B" },
  { name: "Edu", value: 0, color: "#98A2B3" },
];
>>>>>>> Stashed changes

const RemainingDutyPage = () => {
  const initialData: TreeNode = {
    name: "duty",
    children: [
      { name: "Day", value: 0, color: "#18C5B5" },
      { name: "Eve", value: 0, color: "#F4D567" },
      { name: "Night", value: 0, color: "#485E88" },
      { name: "Off", value: 0, color: "#9B8AFB" },
      { name: "휴가/연가", value: 0, color: "#FDA29B" },
      { name: "교육", value: 0, color: "#98A2B3" },
    ],
    colname: "duty-column",
    color: "white",
    remain: 0,
  };

  const [data, setData] = useState<TreeNode>(initialData);

  useEffect(() => {
    const updateData = () => {
      const newData: TreeNode = {
        name: "duty",
        children: [
          { name: "Day", value: getRandomValues(), color: "#18C5B5" },
          { name: "Eve", value: getRandomValues(), color: "#F4D567" },
          { name: "Night", value: getRandomValues(), color: "#485E88" },
          { name: "Off", value: getRandomValues(), color: "#9B8AFB" },
          { name: "휴가", value: getRandomValues(), color: "#FDA29B" },
          { name: "Edu", value: getRandomValues(), color: "#98A2B3" },
        ],
        colname: "duty-column",
        color: "white",
        remain: 0,
      };
      setData(newData);
    };

    // 5초마다 데이터 업데이트 함수 실행
    const interval = setInterval(updateData, 5000);

    // 컴포넌트 언마운트 시 clearInterval을 사용하여 setInterval 정리
    return () => clearInterval(interval);
  }, []);
  const getRandomValues = () => {
    return Math.floor(Math.random() * 8);
  };

  return (
    <>
      <div>[에그로그] 잔여근무그래프 넣어볼게</div>
      <RemainingDutyGraph data={data} />
    </>
  );
};

export default RemainingDutyPage;
