import React, { useEffect, useRef } from "react";
import styled from "styled-components";
import {InquiryUser} from "@custom-types/InquiryUser";
import {User} from "@custom-types/User";
import {Inquiry} from "@custom-types/Inquiry";
import CheckedUserProfile from "../../../../../assets/images/default/check_round_fill.svg";

interface InquiryUserInfoProps {
    inquiry: InquiryUser
}

const StyledInquiryUserInfoBox = styled.div<{ isNoted:boolean }>`
  display: flex;
  flex-direction: row;
  gap: 0.5em;
  font-family: "Line-Seed-Sans-App";
  & > .checkedMark {
    display: flex;
    visibility: ${props => props.isNoted ? 'visible' : 'hidden'}; /* isNoted가 true일 때만 확인 마크 표시 */
    align-items: center;
    justify-content: center;
  }
`;
const StyledUserImgArea = styled.div`
  width: 3em;
  min-width: 3em;
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
  height: 3em;
  & > div > .userInfoHeader{
    font-size: 1.2em;
    font-weight: bold;
  }
  & > div > .userInfoTime{
    color: #D0D5DD;
    font-weight: 300;
  }
  & > .inquiryUserInfoContent{
    height: 3em;
    overflow: hidden; /* 넘치는 내용은 숨김 처리 */
    text-overflow: ellipsis; /* 넘치는 텍스트를 말줄임표로 표시 */
  }
`;
const UserInfo = (userInfoProps:InquiryUserInfoProps) => {
    const user:User = userInfoProps.inquiry.user;
    const inquiry:Inquiry = userInfoProps.inquiry.inquiry;
    console.log(user);
    return (
        <StyledInquiryUserInfoBox isNoted={inquiry.isNoted}>
            <StyledUserImgArea>
                <img src={user.profileImgUrl} alt="사용자 이미지" />
            </StyledUserImgArea>
            <StyledUserInfoArea>
                <div>
                    <span className={"userInfoHeader"}>{user.name} ID{user.id.toString()}</span> <span className={"userInfoTime"}>{user.createdAt}</span>
                </div>
                <div className={"inquiryUserInfoContent"}>{inquiry.content}</div>
            </StyledUserInfoArea>
            <div className={"checkedMark"}>
                <img src={CheckedUserProfile} alt="확인함" />
            </div>
        </StyledInquiryUserInfoBox>
    );
};

export default UserInfo;
