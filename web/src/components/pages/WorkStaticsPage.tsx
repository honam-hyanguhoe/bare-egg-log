import React, { useState, useEffect } from "react";
import WorkStaticsGraph from "../atoms/WorkStaticsGraph";
import { idText } from "typescript";

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

  // 데이터 상태 및 업데이트 함수 설정
  const [data, setData] = useState<DataItem[]>(initialData);

  useEffect(() => {
    // 5초마다 데이터를 변경하는 함수
    const updateData = () => {
      const newData: DataItem[] = normalizeData(monthlyData);
      console.log(`${JSON.stringify(newData)}`);
      // const newData: DataItem[] = initialData;
      setData(newData);
    };

    // 5초마다 데이터 업데이트 함수 실행
    const interval = setInterval(updateData, 5000);

    // 컴포넌트 언마운트 시 clearInterval을 사용하여 setInterval 정리
    return () => clearInterval(interval);
  }, []);

  return (
    <>
      <div>[에그로그] 근무통계그래프 넣어볼게!</div>
      <WorkStaticsGraph data={data} />
    </>
  );
};

export default WorkStaticsPage;
