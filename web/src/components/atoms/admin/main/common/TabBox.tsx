import React from 'react';
import styled from 'styled-components';
interface TabBoxProps {
    text:string,
    isActive: boolean
}

const StyledTabBox = styled.h2<{ isActive:boolean }>`
  font-size: 1.2em;
  font-weight: 400;
  font-family: "Line-Seed-Sans-App";
  color: #000;
  text-align: center;
  padding-top: 0.8em;
  padding-bottom: 0.8em;
  border-bottom: 2px ${props => props.isActive ? 'solid' : 'hidden'} #FEC84B; // 언더라인 설정
`;


const TabBox = (data:TabBoxProps) => {
    return <StyledTabBox isActive={data.isActive}>{data.text}</StyledTabBox>;
};

export default TabBox;
