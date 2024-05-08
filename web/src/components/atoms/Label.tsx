import React from "react";
import styled from "styled-components";

interface CircleProps {
  borderColor?: string;
  bgColor?: string;
}

const LabelContainer = styled.div``;
const Circle = styled.div<CircleProps>`
  border-radius: 50%;
  border: 1px solid ${(props) => props.borderColor || "black"};
  background-color: ${(props) => props.bgColor || "black"};
  width: 15px;
  height: 15px;
`;

const Label = ({ borderColor, bgColor }: CircleProps) => {
  return (
    <LabelContainer>
      <Circle borderColor={borderColor} bgColor={bgColor}></Circle>
    </LabelContainer>
  );
};

export default Label;
