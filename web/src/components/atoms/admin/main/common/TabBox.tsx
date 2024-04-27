import React from 'react';
import styled from 'styled-components';

const TabBox = styled.h2`
  font-size: 24px;
  color: #333;
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
  padding-bottom: 5px;
  border-bottom: 2px solid #007bff; // 언더라인 설정
`;

const TabBoxAtom: React.FC<{text: string}> = ({ text }) => {
    return <TabBox>{text}</TabBox>;
};

export default TabBoxAtom;
