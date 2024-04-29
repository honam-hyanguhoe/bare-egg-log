import React from 'react';
import styled from 'styled-components';


const StyledTitle = styled.h2`
  font-size: 1.8em;
  font-weight: bold;
  color: #000;
  font-family: "Line-Seed-Sans-App";
`;

const Title= ({ title } : { title: string }) => {
  return <StyledTitle>{title}</StyledTitle>;
};

export default Title;
