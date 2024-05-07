import React, { useState, useEffect } from "react";
import WorkStaticsGraph from "../atoms/WorkStaticsGraph";
import Label from "../atoms/Label";
import styled from "styled-components";

interface DataItem {
  name: string;
  values: number[];
}

const getRandomValues = () => {
  return Array.from({ length: 4 }, () => Math.floor(Math.random() * 7));
};
const monthlyData = [
  {
    month: "2024-05",
    weeks: [
      {
        week: "1",
        data: {
          DAY: 7,
          EVE: 6,
          NIGHT: 5,
          OFF: 4,
        },
      },
      {
        week: "2",
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
      },
      {
        week: "3",
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
      },
      {
        week: "4",
        data: {
          DAY: 0,
          EVE: 0,
          NIGHT: 0,
          OFF: 0,
        },
      },
    ],
  },
  {
    month: "2024-06",
    weeks: [
      {
        week: "1",
        data: {
          DAY: 3,
          EVE: 4,
          NIGHT: 2,
          OFF: 1,
        },
      },
    ],
  },
];

const WorkStaticsPage = () => {
  const initialData: DataItem[] = [
    { name: "5월 1주", values: [1, 3, 4, 5] },
    { name: "5월 2주", values: [4, 3, 2, 1] },
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

  const [data, setData] = useState<DataItem[]>(initialData);

  useEffect(() => {
    const updateData = () => {
      const newData: DataItem[] = normalizeData(monthlyData);
      console.log(`${JSON.stringify(newData)}`);
      // const newData: DataItem[] = initialData;
      setData(newData);
    };

    const interval = setInterval(updateData, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <GraphContainer>
      <GraphTitle>내 근무 통계</GraphTitle>
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
      <WorkStaticsGraph data={data} />
    </GraphContainer>
  );
};

export default WorkStaticsPage;

const GraphContainer = styled.div`
  padding: 20px;
`;
const GraphTitle = styled.p`
  font-size: 25px;
  font-family: "Line-Seed-Sans-App";
  font-weight: 700;
  color: #333;
  margin-bottom: 20px;
`;
const TypeListContainer = styled.div`
  display: flex;
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
