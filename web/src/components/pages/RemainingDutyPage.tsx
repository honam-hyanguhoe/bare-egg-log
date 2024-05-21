import React, { useRef, useEffect, useState, useMemo } from "react";
import styled from "styled-components";
import RemainingDutyGraph from "../atoms/RemainingDutyGraph";

declare global {
  interface Window {
    receiveDataFromApp: (data: string) => void;
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
    { name: "Day", value: 0, color: "#18C5B5" },
    { name: "Eve", value: 0, color: "#F4D567" },
    { name: "Night", value: 0, color: "#485E88" },
    { name: "Off", value: 0, color: "#9B8AFB" },
  ],
};

const RemainingDutyPage = () => {
  const [duty, setDuty] = useState<TreeNode>(initialData);

  window.receiveDataFromApp = (data: string) => {
    delete (window as any).receiveDataFromApp;
    console.log("remain Data received: " + data);

    setDuty((prevData) => ({
      ...prevData,
      children: JSON.parse(data),
    }));

    return `remainData in web ${JSON.stringify(data)}`;
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
  max-height: 100vh;
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
