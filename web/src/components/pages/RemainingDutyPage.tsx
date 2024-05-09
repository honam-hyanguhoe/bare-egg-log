import React, { useRef, useEffect, useState, useMemo } from "react";
import styled from "styled-components";
import RemainingDutyGraph from "../atoms/RemainingDutyGraph";

interface TreeNode {
  name: string;
  remain?: number;
  value?: number;
  children?: TreeNode[];
  colname?: string;
  color?: string;
}

const remainingDuty = [
  { name: "Day", value: 3, color: "#18C5B5" },
  { name: "Eve", value: 2, color: "#F4D567" },
  { name: "Night", value: 1, color: "#485E88" },
  { name: "Off", value: 4, color: "#9B8AFB" },
  // { name: "Vacation", value: 0, color: "#FDA29B" },
  // { name: "Edu", value: 0, color: "#98A2B3" },
];

const RemainingDutyPage = () => {
  const initialData: TreeNode = {
    name: "duty",
    children: remainingDuty,
    colname: "duty-column",
    color: "white",
    remain: 0,
  };

  const [data, setData] = useState<TreeNode>(initialData);

  const getRandomValues = () => {
    return Math.floor(Math.random() * 8);
  };

  useEffect(() => {
    // 컴포넌트가 마운트될 때 함수를 window 객체에 할당합니다.
    window.receiveDataFromApp = (data) => {
      console.log("Data received: " + data);
      return `윈도우로 변경 test : ${data}`;
    };

    // 컴포넌트가 언마운트될 때 window 객체에서 함수를 제거합니다.
    return () => {
      delete window.receiveDataFromApp;
    };
  }, []);

  return (
    <GraphContainer>
      {/* <GraphTitle>제 근무는 언제 끝나죠?</GraphTitle> */}
      <RemainingDutyGraph data={data} />
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
