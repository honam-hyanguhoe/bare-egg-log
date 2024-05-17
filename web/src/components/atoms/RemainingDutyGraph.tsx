import React, { useRef, useEffect, useState, useMemo } from "react";
import * as d3 from "d3";
import styled from "styled-components";
import { Tree } from "istanbul-lib-report";

interface TreeNode {
  name: string;
  remain?: number;
  value?: number;
  children?: TreeNode[];
  colname?: string;
  color?: string;
}

interface RemainingDutyGraphProps {
  data: TreeNode;
}
const RemainingDutyGraph = ({ data }: RemainingDutyGraphProps) => {
  const svgRef = useRef(null);

  const [dimensions, setDimensions] = useState({
    width: window.innerWidth > 500 ? 500 : window.innerWidth,
    height: window.innerWidth > 500 ? 500 : window.innerWidth,
  });

  useEffect(() => {
    const handleResize = () => {
      setDimensions({
        width: window.innerWidth > 500 ? 500 : window.innerWidth,
        height: window.innerWidth > 500 ? 500 : window.innerWidth,
      });
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, [data]);

  useEffect(() => {
    console.log(`잔여 duty ${JSON.stringify(data)}`);
    if (!svgRef.current) return;

    const { width, height } = dimensions;
    const mapSvg = d3.select(svgRef.current);
    mapSvg.selectAll(".dutyMap").remove();

    var margin = { top: 10, right: 10, bottom: 10, left: 10 };
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;

    const nodesGroup = mapSvg
      .append("g")
      .attr("class", "dutyMap")
      .attr("transform", `translate(${margin.left}, ${margin.top})`);

    const root = d3.hierarchy<TreeNode>(data).sum((d) => d.value ?? 0);

    d3.treemap<TreeNode>().size([innerWidth, innerHeight]).padding(5)(root);

    const mapNodes = nodesGroup
      .selectAll("g")
      .data(root.leaves())
      .enter()
      .append("g")
      .attr(
        "transform",
        (d) =>
          `translate(${(d as d3.HierarchyRectangularNode<TreeNode>).x0},${(d as d3.HierarchyRectangularNode<TreeNode>).y0})`
      )
      .filter((d) => d.data.value !== 0);

    mapNodes
      .append("rect")
      .attr("width", (d) => {
        return (
          (d as d3.HierarchyRectangularNode<TreeNode>).x1 -
          (d as d3.HierarchyRectangularNode<TreeNode>).x0 +
          2
        );
      })
      .attr(
        "height",
        (d) =>
          (d as d3.HierarchyRectangularNode<TreeNode>).y1 -
          (d as d3.HierarchyRectangularNode<TreeNode>).y0
      )
      .attr("fill", (d) => d.data.color as string)
      .attr("rx", 20)
      .attr("ry", 20);

    mapNodes
      .append("text")
      .attr("class", "nodeDuty")
      .attr(
        "x",
        (d) =>
          ((d as d3.HierarchyRectangularNode<TreeNode>).x1 -
            (d as d3.HierarchyRectangularNode<TreeNode>).x0) /
          2
      )
      .attr(
        "y",
        (d) =>
          ((d as d3.HierarchyRectangularNode<TreeNode>).y1 -
            (d as d3.HierarchyRectangularNode<TreeNode>).y0) /
          2
      )
      .text((d) => d.data.name)
      .attr("text-anchor", "middle")
      .attr("dominant-baseline", "middle")
      .style("fill", "#f9f9f9")
      .style("font-family", "Line-Seed-Sans-App")
      .style("font-weight", "700")
      .style("font-size", (d) => {
        const rectWidth =
          (d as d3.HierarchyRectangularNode<TreeNode>).x1 -
          (d as d3.HierarchyRectangularNode<TreeNode>).x0;
        const rectHeight =
          (d as d3.HierarchyRectangularNode<TreeNode>).y1 -
          (d as d3.HierarchyRectangularNode<TreeNode>).y0;
        return `${Math.min(rectWidth, rectHeight) / 5}px`; // 폰트 크기 산정 방식 조정
      });
  }, [data, dimensions]);

  return (
    <svg ref={svgRef} width={dimensions.width} height={dimensions.height}></svg>
  );
};

export default RemainingDutyGraph;
