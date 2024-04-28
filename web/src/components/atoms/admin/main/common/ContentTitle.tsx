import React from "react";
import styled from "styled-components";

const StyledContentTitle = styled.h2`
  font-size: 1.4em;
  font-weight: 700;
  font-family: "Line-Seed-Sans-App";
  color: #000;
`;

const ContentTitle = ({ title }: { title: string }) => {
  return <StyledContentTitle>{title}</StyledContentTitle>;
};

export default ContentTitle;
