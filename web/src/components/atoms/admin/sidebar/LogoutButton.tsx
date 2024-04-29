import React from 'react';
import styled from 'styled-components';
import LogoutButtonIcon from '../../../../assets/images/default/on_button.svg';

const StyledLogoutButton = styled.div`
  font-size: 1.2em;
  font-weight: 300;
  font-family: "Line-Seed-Sans-App";
  color: #f9f9f9;
  text-align: center;
  padding: 0.7em;
  border-radius: 0.4em;
  display: flex;
  align-content: center;
  gap: 0.5em;
  & > .imageBox {
    display: inline-block;
    width: 1.2em;
    height: 1.2em;
  }
  & > .imageBox > img {
    width: 100%;
  }
`;


const LogoutButton = () => {
    const someAction= () => {
        alert(`로그아웃 버튼이 클릭되었습니다!`);
    };
    return <StyledLogoutButton onClick={someAction}>
        <div className={"imageBox"}>
            <img src={LogoutButtonIcon} alt={"icon"}/>
        </div>
        <span>로그아웃</span>
    </StyledLogoutButton>;
};

export default LogoutButton;
