import React from "react";
import LoginInputBox from "../atoms/admin/login/LoginInputBox";
import LoginButton from "../atoms/admin/login/LoginButton";
import ContentField from "../atoms/admin/main/common/ContentTitle";
import TabBox from "../atoms/admin/main/common/TabBox";
import Title from "../atoms/admin/main/common/Title";

const MainPage = () => {
  const handleLoginClick = () => {
    alert("로그인 버튼이 클릭되었습니다!");
  };
  return (
    <>
      <div>에그로그 메인이에요</div>
      <div>호남향우회</div>
      <LoginInputBox type={"email"} />
      <LoginInputBox type={"password"} />
      <LoginButton onClick={handleLoginClick} />
      <ContentField title={"문의사항"} />
      <TabBox text={"선택"} />
        <Title title={"소제목"}/>
    </>
  );
};

export default MainPage;
