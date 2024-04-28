import React from "react";
import styled from "styled-components";
import BigNumber from "bignumber.js";

const StyledChangeStatusButton = styled.button`
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

const ChangeStatusButton = ({ id }: { id: BigNumber }) => {
    const someAction= () => {
        alert(`확인 완료 버튼이 클릭되었습니다! ID 값: ${id.toString()}`);
    };
    return <StyledChangeStatusButton onClick={someAction}>확인 완료</StyledChangeStatusButton>;
};

export default ChangeStatusButton;
