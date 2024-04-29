import React from 'react';
import styled from 'styled-components';

// 스타일드 버튼 컴포넌트 정의
const StyledButton = styled.button`
  background-color: #FEC84B;
  font-size: 1em;
  font-weight: 500;
  font-family: "Line-Seed-Sans-App";
  color: #F9F9F9;
  width: 16em;
  height: 2.5em;
  border: none;
  outline: none;
  border-radius: 0.4em;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #ffb618;
  }

  &:active {
    background-color: #ffb618;
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
