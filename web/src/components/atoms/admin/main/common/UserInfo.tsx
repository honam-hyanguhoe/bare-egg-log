import React, { useEffect, useRef } from "react";
import styled from "styled-components";
import { User } from "@custom-types/User";

interface UserInfoProps {
    user: User;
}

const StyledUserInfoBox = styled.div`
  display: flex;
  flex-direction: row;
  gap: 0.5em;
  font-family: "Line-Seed-Sans-App";
`;
const StyledUserImgArea = styled.div`
  width: 3em;
  height: 3em;
  border-radius: 1.5em;
  background-color: aqua;
  display: flex;
  align-items: center;
  justify-content: center;
  & > img {
    width: 100%;
  }
`;
const StyledUserInfoArea = styled.div`
  display: flex;
  flex-direction: column;
  & > div > .userInfoHeader{
    font-size: 1.2em;
    font-weight: bold;
  }
  & > div > .userInfoTime{
    color: #D0D5DD;
    font-weight: 300;
  }
`;
const UserInfo = (userInfoProps:UserInfoProps) => {
    const user:User = userInfoProps.user;
    console.log(user);
  return (
    <StyledUserInfoBox>
      <StyledUserImgArea>
        <img src={user.profileImgUrl} alt="사용자 이미지" />
      </StyledUserImgArea>
      <StyledUserInfoArea>
              <div>
                  <span className={"userInfoHeader"}>{user.name} ID{user.id.toString()}</span> <span className={"userInfoTime"}>{user.createdAt}</span>
              </div>
              <div>{user.hospital}</div>
      </StyledUserInfoArea>
    </StyledUserInfoBox>
  );
};

export default UserInfo;
