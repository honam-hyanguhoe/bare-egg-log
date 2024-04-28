import React from "react";
import styled from "styled-components";
import BigNumber from "bignumber.js";

const StyledMailSendButton = styled.button`
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

const MailSendButton = ({ email }: { email: string }) => {
    const someAction= () => {
        alert(`메일 전송 버튼이 클릭되었습니다! email: ${email}`);
    };
    return <StyledMailSendButton onClick={someAction}>메일 전송</StyledMailSendButton>;
};

export default MailSendButton;
