import React, { useRef, useEffect, useState, useMemo } from "react";
import * as d3 from "d3";

interface DataItem {
  name: string;
  values: number[];
}

const WorkStaticsGraph = () => {
  const svgRef = useRef(null);
  const width = 500;
  const height = 400;

  const data: DataItem[] = useMemo(
    () => [
      { name: "5월 1주", values: [8, 6, 15, 3] },
      { name: "5월 2주", values: [18, 9, 7, 6] },
      { name: "5월 3주", values: [12, 2, 9, 8] },
      { name: "5월 4주", values: [10, 3, 12, 0] },
      { name: "6월 1주", values: [18, 7, 14, 2] },
    ],
    []
  );

  useEffect(() => {
    if (!svgRef.current) return;

    const barSvg = d3.select(svgRef.current);
    const margin = { top: 20, right: 30, bottom: 30, left: 40 };
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;

    const xScale = d3
      .scaleBand()
      .domain(data.map((d) => d.name))
      .range([0, innerWidth])
      .padding(0.2);

    const maxVal = d3.max(data, (d) => d3.max(d.values)) ?? 0;
    console.log(`최대값 ${maxVal}`);

    const yScale = d3
      .scaleLinear()
      .domain([0, maxVal + 15])
      .nice()
      .range([innerHeight, 0]);

    const xAxis = d3
      .axisBottom(xScale)
      .tickValues(data.map((d) => d.name))
      .tickSize(0)
      .tickPadding(10);

    const yAxis = d3.axisLeft(yScale);

    const barWidth = xScale.bandwidth() / data[0].values.length; // 막대 너비 계산

    // 개선된 선택자 사용
    ["day", "eve", "night", "off"].forEach((className, i) => {
      barSvg
        .selectAll(`rect.${className}`)
        .data(data)
        .join("rect")
        .attr("class", className)
        .attr("x", (d) => xScale(d.name)! + margin.left + barWidth * i)
        .attr("y", (d) => innerHeight + margin.top)
        .attr("width", barWidth)
        .attr("height", 0)
        .attr("fill", ["#18C5B5", "#ffe68a", "#7c90b6", "#b4aaeb"][i])
        .attr("rx", 7)
        .attr("ry", 7)
        .transition()
        .duration(500)
        .attr("y", (d) => yScale(d.values[i]) + margin.top)
        .attr("height", (d) => innerHeight - yScale(d.values[i]));
    });

    barSvg
      .append("g")
      .attr("class", "x-axis")
      .attr(
        "transform",
        `translate(${margin.left}, ${innerHeight + margin.top})`
      )
      .call(xAxis)
      .selectAll("text")
      .style("text-anchor", "center");

    barSvg
      .append("g")
      .attr("class", "y-axis")
      .attr("transform", `translate(${margin.left}, ${margin.top})`)
      .call(yAxis);
  }, [svgRef, data, width, height]);

  return (
    <>
      <svg ref={svgRef} width={width} height={height}>
        <g className="x-axis" />
        <g className="y-axis" />
      </svg>
    </>
  );
};

export default WorkStaticsGraph;
