import React from 'react';
import styled from 'styled-components';


const Title = styled.h2`
  font-size: 16px;
  font-weight: bold;
  color: #000;
`;

const TitleAtom: React.FC<{ title: string }> = ({ title }) => {
  return <Title>{title}</Title>;
};

export default TitleAtom;
