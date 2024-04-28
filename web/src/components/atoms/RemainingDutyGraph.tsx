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

  const width = 500;
  const height = 500;
  var margin = { top: 10, right: 10, bottom: 10, left: 10 };

  const defaultColor = "white";

  useEffect(() => {
    console.log(`잔여 duty ${JSON.stringify(data)}`);
    if (!svgRef.current) return;

    const mapSvg = d3.select(svgRef.current);
    mapSvg.selectAll(".dutyMap").remove();

    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;

    const nodesGroup = mapSvg
      .append("g")
      .attr("class", "dutyMap")
      .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    const root = d3.hierarchy<TreeNode>(data).sum((d) => d.value ?? 0);

    d3.treemap<TreeNode>().size([innerWidth, innerHeight]).padding(7)(root);

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
      .attr("rx", 25)
      .attr("ry", 25);

    // let firstFontSize: number = 0;
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
      .each(function (d) {
        const rectWidth =
          (d as d3.HierarchyRectangularNode<TreeNode>).x1 -
          (d as d3.HierarchyRectangularNode<TreeNode>).x0;
        const rectHeight =
          (d as d3.HierarchyRectangularNode<TreeNode>).y1 -
          (d as d3.HierarchyRectangularNode<TreeNode>).y0;
        const textNode = d3.select(this);
        const fontSize = Math.min(rectWidth, rectHeight) / 3 - 30;
        textNode.style("font-size", `${fontSize <= 15 ? 24 : fontSize}px`);
      });

    // mapNodes
    //   .append("text")
    //   .attr(
    //     "x",
    //     (d) =>
    //       ((d as d3.HierarchyRectangularNode<TreeNode>).x1 -
    //         (d as d3.HierarchyRectangularNode<TreeNode>).x0) /
    //       2
    //   )
    //   .attr("y", (d) => {
    //     const currentNode = document.querySelector(".nodeDuty");

    //     if (currentNode && currentNode.nextElementSibling) {
    //       const siblingNode =
    //         currentNode.nextElementSibling.querySelector(".nodeDuty");

    //       if (siblingNode) {
    //         // 선택된 형제 요소의 계산된 스타일에서 font-size 값을 가져옵니다.
    //         const computedStyle = window.getComputedStyle(siblingNode);
    //         firstFontSize = Number(computedStyle.getPropertyValue("font-size"));

    //         // 가져온 font-size 값을 출력합니다.
    //         console.log(
    //           "Font size of sibling node with class 'nodeDuty':",
    //           firstFontSize
    //         );
    //       } else {
    //         console.error("Sibling node with class 'nodeDuty' not found.");
    //       }
    //     }

    //     return (
    //       ((d as d3.HierarchyRectangularNode<TreeNode>).y1 -
    //         (d as d3.HierarchyRectangularNode<TreeNode>).y0) /
    //         2 +
    //       firstFontSize
    //     );
    //   })
    //   .text((d) => `+${d.data.value}일`)
    //   .attr("text-anchor", "middle")
    //   .attr("dominant-baseline", "middle")
    //   .style("fill", "#f9f9f9")
    //   .style("font-family", "Line-Seed-Sans-App")
    //   .style("font-weight", "700")
    //   .each(function (d) {
    //     const rectWidth =
    //       (d as d3.HierarchyRectangularNode<TreeNode>).x1 -
    //       (d as d3.HierarchyRectangularNode<TreeNode>).x0;
    //     const rectHeight =
    //       (d as d3.HierarchyRectangularNode<TreeNode>).y1 -
    //       (d as d3.HierarchyRectangularNode<TreeNode>).y0;
    //     const textNode = d3.select(this);
    //     const fontSize = Math.min(rectWidth, rectHeight) / 3 - 35;
    //     textNode.style("font-size", `${fontSize}px`);
    //     // textNode.style("font-size", `${fontSize <= 15 ? 24 : fontSize}px`);
    //   });
  }, [data]);

  return (
    <svg
      ref={svgRef}
      width={width + margin.left + margin.right}
      height={height + margin.top + margin.bottom}
    ></svg>
  );
};

export default RemainingDutyGraph;
