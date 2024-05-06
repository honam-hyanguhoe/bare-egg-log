import React, { useRef, useEffect, useState, useMemo } from "react";
import * as d3 from "d3";
import styled from "styled-components";

interface DataItem {
  name: string;
  values: number[];
}

interface WorkStaticsGraphProps {
  data: DataItem[];
}
const WorkStaticsGraph = ({ data }: WorkStaticsGraphProps) => {
  const svgRef = useRef(null);
  const dataRef = useRef(data);
  const width = 500;
  const height = 400;
  const backgroundColor = "#fff";
  useEffect(() => {
    dataRef.current = data;
  }, [data]);

  useEffect(() => {
    if (!svgRef.current || !dataRef.current) return;

    const barSvg = d3.select(svgRef.current);
    const margin = { top: 20, right: 30, bottom: 30, left: 40 };
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;

    const xScale = d3
      .scaleBand()
      .domain(dataRef.current.map((d) => d.name))
      .range([0, innerWidth])
      .padding(0.4);

    // const maxVal = d3.max(dataRef.current, (d) => d3.max(d.values)) ?? 0;
    const maxVal = 7;

    const yScale = d3
      .scaleLinear()
      .domain([0, maxVal + 5])
      .nice()
      .range([innerHeight, 0]);

    const xAxis = d3
      .axisBottom(xScale)
      .tickValues(dataRef.current.map((d) => d.name))
      .tickSize(0)
      .tickPadding(15);

    const yAxis = d3
      .axisLeft(yScale)
      .tickValues([0, 4, 8])
      .ticks(2)
      .tickSize(0)
      .tickPadding(10);

    const makeYLines = () => {
      return d3
        .axisLeft(yScale)
        .ticks(2)
        .tickValues([4, 8])
        .tickSize(-innerWidth)
        .tickFormat(() => "");
    };

    const barWidth = xScale.bandwidth() / dataRef.current[0].values.length;

    ["DAY", "EVE", "NIGHT", "OFF"].forEach((className, i) => {
      console.log(`className ${className} / ${i}`);
      barSvg
        .selectAll(`rect.${className}`)
        .data(dataRef.current)
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

    const gridLines = barSvg
      .insert("g", ":first-child")
      .attr("class", "grid")
      .attr("transform", `translate(${margin.left}, ${margin.top})`)
      .call(makeYLines());

    gridLines
      .selectAll(".tick line")
      .attr("stroke", "#ECECEC")
      .attr("opacity", 0.5);

    gridLines.select(".domain").attr("stroke", backgroundColor);

    const existingXAxis = barSvg.select(".x-axis");
    if (existingXAxis.node()) {
      existingXAxis.remove();
    }

    const existingYAxis = barSvg.select(".y-axis");
    if (existingYAxis.node()) {
      console.log("y축 삭제");
      existingYAxis.remove();
    }

    const xAxisG = barSvg
      .append("g")
      .attr("class", "x-axis")
      .attr(
        "transform",
        `translate(${margin.left}, ${innerHeight + margin.top})`
      )
      .call(xAxis);

    xAxisG
      .selectAll("text")
      .style("text-anchor", "center")
      .style("fill", "gray")
      .style("font-size", "12px");
    xAxisG.select(".domain").attr("stroke", "#ECECEC");
    const yAxisG = barSvg
      .append("g")
      .attr("class", "y-axis")
      .attr("transform", `translate(${margin.left}, ${margin.top})`)
      .call(yAxis);
    yAxisG.selectAll("text").style("fill", "gray").style("font-size", "12px");
    yAxisG.select(".domain").attr("stroke", "#fff");
    return () => {
      gridLines.remove();
    };
  }, [width, height, data]);

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
