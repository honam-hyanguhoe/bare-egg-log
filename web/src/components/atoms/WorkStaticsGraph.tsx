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
  const backgroundColor = "#F2F4F7";

  const [dimensions, setDimensions] = useState({
    width: window.innerWidth > 500 ? 500 : window.innerWidth * 0.95,
    height: window.innerHeight > 300 ? 300 : window.innerHeight * 0.5,
  });

  useEffect(() => {
    const handleResize = () => {
      setDimensions({
        width: window.innerWidth > 500 ? 500 : window.innerWidth * 0.95,
        height: window.innerHeight > 300 ? 300 : window.innerHeight * 0.5,
      });
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  useEffect(() => {
    dataRef.current = data;
  }, [data]);

  useEffect(() => {
    if (!svgRef.current || !dataRef.current) return;

    const { width, height } = dimensions;
    const barSvg = d3.select(svgRef.current);
    const margin = { top: 0, right: 30, bottom: 30, left: 20 };
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
      .domain([0, maxVal + 2])
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

    const barWidth = xScale.bandwidth() / dataRef.current[0].values.length + 2;

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
      .attr("stroke", "#D4D4D4")
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
    xAxisG.select(".domain").attr("stroke", "#D4D4D4");

    const yAxisG = barSvg
      .append("g")
      .attr("class", "y-axis")
      .attr("transform", `translate(${margin.left}, ${margin.top})`)
      .call(yAxis);

    yAxisG.selectAll("text").style("fill", "gray").style("font-size", "12px");
    yAxisG.select(".domain").attr("stroke", "#F2F4F7");

    return () => {
      gridLines.remove();
    };
  }, [data, dimensions]);

  return (
    <>
      <svg ref={svgRef} width={dimensions.width} height={dimensions.height}>
        <g className="x-axis" />
        <g className="y-axis" />
      </svg>
    </>
  );
};

export default WorkStaticsGraph;
