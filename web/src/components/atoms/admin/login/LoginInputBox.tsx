import React from 'react';
import styled from 'styled-components';

interface LoginInputProps {
    type: 'email' | 'password';
}

const StyledLoginInput = styled.input`
  padding: 0.8em;
  border: 0.1em solid #F9F9F9;
  border-radius: 0.4em;
  width: 20em;
  font-size: 0.8em;
  font-weight: 300;
  font-family: "Line-Seed-Sans-App";
  color: #F9F9F9;
  outline: none;
  
  &::placeholder {
    color: #F9F9F9;
  }
  
  &:focus {
    border-color: #FFF;
  }
`;

const InputField: React.FC<LoginInputProps> = ({ type }) => {
    const placeholder = type === 'email' ? '이메일' : '비밀번호';
    return <StyledLoginInput type={type} placeholder={placeholder} />;
};

export default InputField;
