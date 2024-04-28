import React from "react";
import styled from "styled-components";
import BigNumber from "bignumber.js";

const StyledConfirmAcceptButton = styled.button`
  font-size: 0.9em;
  font-weight: 600;
  font-family: "Line-Seed-Sans-App";
  background-color: #fec84b;
  color: white;
  border-radius: 0.5em;
  text-align: center;
  padding: 0.8em 2em;
  border: none;

  &:hover {
    background-color: #ffb618;
  }
`;

const ConfirmAcceptButton = ({ id }: { id: BigNumber }) => {
    const someAction= () => {
        alert(`승인 버튼이 클릭되었습니다! ID 값: ${id.toString()}`);
    };
  return <StyledConfirmAcceptButton onClick={someAction}>승인</StyledConfirmAcceptButton>;
};

export default ConfirmAcceptButton;
