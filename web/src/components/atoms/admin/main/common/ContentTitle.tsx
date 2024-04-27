import React from "react";
import styled from "styled-components";

const ContentTitle = styled.h2`
  font-size: 18px;
  font-weight: bold;
  color: #000;
`;

const ContentAtom: React.FC<{ title: string }> = ({ title }) => {
  return <ContentTitle>{title}</ContentTitle>;
};

export default ContentAtom;
