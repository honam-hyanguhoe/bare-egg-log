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

declare global {
  interface Window {
    receiveDataFromApp: (data: string) => string;
  }
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
    window.receiveDataFromApp = (data: string) => {
      console.log("Data received: " + data);

      let updatedData = parseData(data);

      setData(updatedData);
      return `윈도우로 변경 test : ${updatedData}`;
    };

    return () => {
      delete (window as any).receiveDataFromApp;
    };
  }, []);

  const parseData = (data: string) => {
    let parsedData = JSON.parse(data);

    console.log(`parsedData ${parsedData}`);

    return parsedData;
  };

  return (
    <GraphContainer>
      <GraphTitle>호남 들어왔니 {JSON.stringify(data)}</GraphTitle>
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
