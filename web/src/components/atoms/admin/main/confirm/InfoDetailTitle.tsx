import React from 'react';
import styled from 'styled-components';


const StyledInfoDetailTitle = styled.h3`
  font-size: 1.3em;
  font-weight: 600;
  color: #258D3F;
  font-family: "Line-Seed-Sans-App";
`;

const InfoDetailTitle= ({ title } : { title: string }) => {
    return <StyledInfoDetailTitle>{title}</StyledInfoDetailTitle>;
};

export default InfoDetailTitle;