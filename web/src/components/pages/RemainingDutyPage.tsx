import React, { useRef, useEffect, useState, useMemo } from "react";
import styled from "styled-components";
import RemainingDutyGraph from "../atoms/RemainingDutyGraph";

declare global {
  interface Window {
    receiveDataFromApp: (data: string) => string;
  }
}

interface TreeNode {
  name: string;
  remain?: number;
  value?: number;
  colname?: string;
  color?: string;
  children?: TreeNode[];
}

// TreeNode 데이터에 대한 초기 상태 설정
const initialData: TreeNode = {
  name: "duty",
  colname: "duty-column",
  color: "white",
  remain: 0,
  children: [
    { name: "Day", value: 3, color: "#18C5B5" },
    { name: "Eve", value: 2, color: "#F4D567" },
    { name: "Night", value: 1, color: "#485E88" },
    { name: "Off", value: 4, color: "#9B8AFB" },
  ],
};

const RemainingDutyPage = () => {
  const [duty, setDuty] = useState<TreeNode>(initialData);

  window.receiveDataFromApp = (data: string) => {
    console.log("Data received: " + data);

    setDuty((prevData) => ({
      ...prevData,
      children: JSON.parse(data),
    }));

    return `${duty}`;
  };

  // useEffect(() => {
  //   const receiveDataFromApp = (data: string) => {
  //     console.log("Data received: " + data);
  //     setDuty(JSON.parse(data));
  //     // try {
  //     // const tempData: TreeNode[] = JSON.parse(data);
  //     // updateData(tempData);
  //     // } catch (error) {
  //     //   console.error("Error parsing JSON data:", error);
  //     // }
  //     return `Window updated with: ${data}`;
  //   };

  //   window.receiveDataFromApp = receiveDataFromApp;

  //   return () => {
  //     delete (window as any).receiveDataFromApp;
  //   };
  // }, []);

  const updateData = (newChildren: TreeNode[]) => {
    setDuty((prevData) => ({
      ...prevData,
      children: newChildren,
    }));
  };

  return (
    <GraphContainer>
      {/* <GraphTitle id="title">{JSON.stringify(duty)}</GraphTitle> */}
      <RemainingDutyGraph data={duty} />
    </GraphContainer>
  );
};

export default RemainingDutyPage;

const GraphContainer = styled.div`
  min-height: 100vh;
  background-color: #f2f4f7;
`;
const GraphTitle = styled.p`
  font-size: 25px;
  font-family: "Line-Seed-Sans-App";
  font-weight: 700;
  color: #333;
  margin-bottom: 20px;
`;

// useEffect(() => {
//   const data = `[{
//       "name": "DAY",
//       "value": 1,
//       "color": "#18C5B5"
//     }, {
//       "name": "OFF",
//       "value": 3,
//       "color": "#9B8AFB"
//     }]`;
//   const tempData: TreeNode[] = JSON.parse(data);
//   updateData(tempData);
// }, []);
