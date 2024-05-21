import React, { useState, useEffect } from "react";
import WorkStaticsGraph from "../atoms/WorkStaticsGraph";
import Label from "../atoms/Label";
import styled from "styled-components";

interface DataItem {
  name: string;
  values: number[];
}

const monthlyData = [
  {
    month: "2024-05",
    weeks: [
      {
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
        week: "1",
      },
      {
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
        week: "2",
      },
      {
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
        week: "3",
      },
      {
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
        week: "4",
      },
    ],
  },
];

declare global {
  interface Window {
    receiveStatsFromApp: (data: string) => string;
  }
}
const WorkStaticsPage = () => {
  const initialData: DataItem[] = [
    { name: "5월 1주", values: [0, 0, 0, 0] },
    { name: "5월 2주", values: [0, 0, 0, 0] },
    { name: "5월 3주", values: [0, 0, 0, 0] },
    { name: "5월 4주", values: [0, 0, 0, 0] },
    { name: "6월 1주", values: [0, 0, 0, 0] },
  ];

  const normalizeData = (tempData: any) => {
    return tempData.flatMap((item: any) => {
      const month = item.month.split("-")[1]; // 05
      return item.weeks.map((weekData: any) => ({
        name: `${parseInt(month)}월 ${weekData.week}주`,
        values: [
          weekData.data.DAY,
          weekData.data.EVE,
          weekData.data.NIGHT,
          weekData.data.OFF,
        ],
      }));
    });
  };

  const newData1: DataItem[] = normalizeData(monthlyData);
  console.log(`${JSON.stringify(newData1)}`);

  const [staticsData, setStaticsData] = useState<DataItem[]>(newData1);

  window.receiveStatsFromApp = (data: string) => {
    delete (window as any).receiveStatsFromApp;
    console.log("statics data received: " + data);

    console.log("statics data 옴" + JSON.parse(data));
    const emptyList = [];
    // JSON.parse(data);
    // setStaticsData();
    // setStaticsData([JSON.parse(data)]);
    emptyList.push(JSON.parse(data));

    const normalData = normalizeData(emptyList);
    console.log("normalData" + normalData);
    setStaticsData(normalData);
    return `staticsData in web${JSON.stringify(data)}`;
  };

  return (
    <GraphContainer>
      <TypeListContainer>
        <TypeContainer>
          <Label borderColor="#18C5B5" bgColor="#18C5B5" />
          <TypeText>DAY</TypeText>
        </TypeContainer>
        <TypeContainer>
          <Label borderColor="#ffe68a" bgColor="#ffe68a" />
          <TypeText>EVE</TypeText>
        </TypeContainer>
        <TypeContainer>
          <Label borderColor="#7c90b6" bgColor="#7c90b6" />
          <TypeText>NIGHT</TypeText>
        </TypeContainer>
        <TypeContainer>
          <Label borderColor="#b4aaeb" bgColor="#b4aaeb" />
          <TypeText>OFF</TypeText>
        </TypeContainer>
      </TypeListContainer>
      <WorkStaticsGraph data={staticsData} />
    </GraphContainer>
  );
};

export default WorkStaticsPage;

const GraphContainer = styled.div`
  background-color: #f2f4f7;
  max-height: 100vh;
  min-height: 100vh;
  padding-left: 10px;
`;

const TypeListContainer = styled.div`
  display: flex;
  padding-top: 30px;
  gap: 14px;
`;

const TypeContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
`;
const TypeText = styled.div`
  font-size: 14px;
  font-family: "Line-Seed-Sans-App";
  font-weight: 600;
  color: #767676;
`;

// const [data, setData] = useState<DataItem[]>(initialData);

// useEffect(() => {
//   const updateData = () => {
//     const newData: DataItem[] = normalizeData(monthlyData);
//     console.log(`${JSON.stringify(newData)}`);
//     // const newData: DataItem[] = initialData;
//     setData(newData);
//   };

//   const interval = setInterval(updateData, 5000);

//   return () => clearInterval(interval);
// }, []);

// webView (web -> App)
// const showAndroidToast = () => {
//   window.AndroidBridge.showToast("안녕 호남향우회");
// };
