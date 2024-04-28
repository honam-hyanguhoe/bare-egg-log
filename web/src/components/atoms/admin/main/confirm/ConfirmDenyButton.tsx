import React from "react";
import styled from "styled-components";
import BigNumber from "bignumber.js";

const StyledConfirmDenyButton = styled.button`
  font-size: 0.9em;
  font-weight: 600;
  font-family: "Line-Seed-Sans-App";
  background-color: white;
  box-shadow: 0 0 0 0.1em #fec84b inset;
  color: #fec84b;
  border-radius: 0.5em;
  text-align: center;
  padding: 0.8em 2em;
  border: none;

  &:hover {
    background-color: #fffdf5;
  }
`;

const ConfirmDenyButton = ({ id }: { id: BigNumber }) => {
    const someAction= () => {
        alert(`버튼이 클릭되었습니다! ID 값: ${id.toString()}`);
    };
    return <StyledConfirmDenyButton onClick={someAction}>거부</StyledConfirmDenyButton>;
};

export default ConfirmDenyButton;
