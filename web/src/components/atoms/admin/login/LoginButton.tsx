import React from 'react';
import styled from 'styled-components';

// 스타일드 버튼 컴포넌트 정의
const StyledButton = styled.button`
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  outline: none;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #0056b3;
  }

  &:active {
    background-color: #004085;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2) inset;
  }

  &:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
`;

// 버튼 컴포넌트
const LoginButton: React.FC<{onClick: () => void; disabled?: boolean}> = ({ onClick, disabled }) => {
    return (
        <StyledButton onClick={onClick} disabled={disabled}>
            로그인
        </StyledButton>
    );
};

export default LoginButton;
