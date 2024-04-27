import React from 'react';
import styled from 'styled-components';

interface LoginInputProps {
    type: 'email' | 'password';
}

const StyledLoginInput = styled.input`
  padding: 8px 12px;
  border: 2px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  color: #333;
  outline: none;
  
  &::placeholder {
    color: #aaa;
  }
  
  &:focus {
    border-color: #0056b3;
    box-shadow: 0 0 5px rgba(0, 86, 179, 0.25);
  }
`;

const InputField: React.FC<LoginInputProps> = ({ type }) => {
    const placeholder = type === 'email' ? '이메일' : '비밀번호';
    return <StyledLoginInput type={type} placeholder={placeholder} />;
};

export default InputField;
