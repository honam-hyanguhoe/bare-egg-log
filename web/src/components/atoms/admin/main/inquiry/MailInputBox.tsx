import React from 'react';
import styled from 'styled-components';

const StyledMailInputField = styled.input`
  padding: 1em;
  border: 0.1em solid #D6D6D6;
  border-radius: 0.4em;
  width: 100%;
  font-size: 1em;
  font-weight: 400;
  font-family: "Line-Seed-Sans-App";
  color: #D6D6D6;
  background-color: white;
  outline: none;
  
  &::placeholder {
    color: #D6D6D6;
  }
  
  &:focus {
    border-color: #fec84b;
  }
`;

const MailInputField = () => {
    const placeholder = "응답할 내용을 입력해주세요";
    return <StyledMailInputField placeholder={placeholder} />;
};

export default MailInputField;
