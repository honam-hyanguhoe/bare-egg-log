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
  { name: "Day", value: 0, color: "#18C5B5" },
  { name: "Eve", value: 0, color: "#F4D567" },
  { name: "Night", value: 0, color: "#485E88" },
  { name: "Off", value: 0, color: "#9B8AFB" },
  { name: "Vacation", value: 0, color: "#FDA29B" },
  { name: "Edu", value: 0, color: "#98A2B3" },
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

  // useEffect(() => {
  //   const updateData = () => {
  //     const newData: TreeNode = {
  //       name: "duty",
  //       children: [
  //         { name: "Day", value: getRandomValues(), color: "#18C5B5" },
  //         { name: "Eve", value: getRandomValues(), color: "#F4D567" },
  //         { name: "Night", value: getRandomValues(), color: "#485E88" },
  //         { name: "Off", value: getRandomValues(), color: "#9B8AFB" },
  //         { name: "휴가", value: getRandomValues(), color: "#FDA29B" },
  //         { name: "Edu", value: getRandomValues(), color: "#98A2B3" },
  //       ],
  //       colname: "duty-column",
  //       color: "white",
  //       remain: 0,
  //     };
  //     setData(newData);
  //   };

  //   // const interval = setInterval(updateData, 2000);

  //   // return () => clearInterval(interval);
  // }, []);
  const getRandomValues = () => {
    return Math.floor(Math.random() * 8);
  };

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
