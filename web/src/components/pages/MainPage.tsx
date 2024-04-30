import React from "react";
import LoginInputBox from "../atoms/admin/login/LoginInputBox";
import LoginButton from "../atoms/admin/login/LoginButton";

import ContentTitle from "../atoms/admin/main/common/ContentTitle";
import TabBox from "../atoms/admin/main/common/TabBox";
import Title from "../atoms/admin/main/common/Title";
import UserInfo from "../atoms/admin/main/common/UserInfo";

import ChangeStatusButton from "../atoms/admin/main/inquiry/ChangeStatusButton";
import MailSendButton from "../atoms/admin/main/inquiry/MailSendButton";
import InquiryUserListBox from "../atoms/admin/main/inquiry/InquiryUserListBox";

import ConfirmAcceptButton from "../atoms/admin/main/confirm/ConfirmAcceptButton";
import ConfirmDenyButton from "../atoms/admin/main/confirm/ConfirmDenyButton";

import BigNumber from "bignumber.js";
import { User } from "@custom-types/User";

import { Inquiry } from "@custom-types/Inquiry";
import { InquiryUser } from "@custom-types/InquiryUser";
import UserDefaultImage from "../../assets/images/default/default-user-profile.png";
import ConfirmDefaultImage01 from "../../assets/images/default/confirm-sample-01.png";
import ConfirmDefaultImage02 from "../../assets/images/default/confirm-sample-02.png";
import {Images} from "@custom-types/Images";
import InfoDetailTitle from "../atoms/admin/main/confirm/InfoDetailTitle";
import {InfoDetailElement} from "@custom-types/InfoDetailElement";
import InfoDetailListElement from "../atoms/admin/main/confirm/InfoDetailListElement";
import InquiryDetail from "../atoms/admin/main/inquiry/InquiryDetail";
import MailInputBox from "../atoms/admin/main/inquiry/MailInputBox";
import CategoryListBox from "../atoms/admin/sidebar/CategoryListBox";
import LogoutButton from "../atoms/admin/sidebar/LogoutButton";
import SideBar from "../organisms/admin/sidebar/SideBar";
import TabBar from "../molecules/admin/main/common/TabBar";
import InfoDetailList from "../molecules/admin/main/confirm/InfoDetailList";

const MainPage = () => {
  const handleLoginClick = () => {
    alert("로그인 버튼이 클릭되었습니다!");
  };
  const defaultUser: User = {
    name: "김반숙",
    hospital: "전남대학병원",
    id: new BigNumber(1),
    profileImgUrl: UserDefaultImage,
    createdAt: "2024.04.24 19:30:21",
    email: "dhmonukim24@gmail.com",
  };
  const defaultInquiryTrue: Inquiry = {
    id: new BigNumber(1),
    title: "집에 가고 싶어요",
    content:
      "국회의 회의는 공개한다. 다만, 출석의원 과반수의 찬성이 있거나 의장이 국가의 안전보장을 위하여 필요하다고 인정할 때에는 공개하지 아니할 수 있다. 국무회의는 대통령·국무총리와 15인 이상 30인 이하의 국무위원으로 구성한다. 국가는 건전한 소비행위를 계도하고 생산품의 품질향상을 촉구하기 위한 소비자보호운동을 법률이 정하는 바에 의하여 보장한다. 국가는 균형있는 국민경제의 성장 및 안정과 적정한 소득의 분배를 유지하고, 시장의 지배와 경제력의 남용을 방지하며, 경제주체간의 조화를 통한 경제의 민주화를 위하여 경제에 관한 규제와 조정을 할 수 있다. 국가는 과학기술의 혁신과 정보 및 인력의 개발을 통하여 국민경제의 발전에 노력하여야 한다.\n누구든지 체포 또는 구속을 당한 때에는 적부의 심사를 법원에 청구할 권리를 가진다. 국회의원은 국가이익을 우선하여 양심에 따라 직무를 행한다. 저작자·발명가·과학기술자와 예술가의 권리는 법률로써 보호한다. 국회의원은 그 지위를 남용하여 국가·공공단체 또는 기업체와의 계약이나 그 처분에 의하여 재산상의 권리·이익 또는 직위를 취득하거나 타인을 위하여 그 취득을 알선할 수 없다. 국회는 의장 1인과 부의장 2인을 선출한다. 국가안전보장회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다. 환경권의 내용과 행사에 관하여는 법률로 정한다. 대통령의 임기연장 또는 중임변경을 위한 헌법개정은 그 헌법개정 제안 당시의 대통령에 대하여는 효력이 없다.",
    isNoted: true,
  };
  const defaultInquiryFalse: Inquiry = {
    id: new BigNumber(1),
    title: "집에 가고 싶어요",
    content:
      "국회의 회의는 공개한다. 다만, 출석의원 과반수의 찬성이 있거나 의장이 국가의 안전보장을 위하여 필요하다고 인정할 때에는 공개하지 아니할 수 있다. 국무회의는 대통령·국무총리와 15인 이상 30인 이하의 국무위원으로 구성한다. 국가는 건전한 소비행위를 계도하고 생산품의 품질향상을 촉구하기 위한 소비자보호운동을 법률이 정하는 바에 의하여 보장한다. 국가는 균형있는 국민경제의 성장 및 안정과 적정한 소득의 분배를 유지하고, 시장의 지배와 경제력의 남용을 방지하며, 경제주체간의 조화를 통한 경제의 민주화를 위하여 경제에 관한 규제와 조정을 할 수 있다. 국가는 과학기술의 혁신과 정보 및 인력의 개발을 통하여 국민경제의 발전에 노력하여야 한다.\n누구든지 체포 또는 구속을 당한 때에는 적부의 심사를 법원에 청구할 권리를 가진다. 국회의원은 국가이익을 우선하여 양심에 따라 직무를 행한다. 저작자·발명가·과학기술자와 예술가의 권리는 법률로써 보호한다. 국회의원은 그 지위를 남용하여 국가·공공단체 또는 기업체와의 계약이나 그 처분에 의하여 재산상의 권리·이익 또는 직위를 취득하거나 타인을 위하여 그 취득을 알선할 수 없다. 국회는 의장 1인과 부의장 2인을 선출한다. 국가안전보장회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다. 환경권의 내용과 행사에 관하여는 법률로 정한다. 대통령의 임기연장 또는 중임변경을 위한 헌법개정은 그 헌법개정 제안 당시의 대통령에 대하여는 효력이 없다.",
    isNoted: false,
  };
  const defaultInquiryUserTrue: InquiryUser = {
    user: defaultUser,
    inquiry: defaultInquiryTrue,
  };
  const defaultInquiryUserFalse: InquiryUser = {
    user: defaultUser,
    inquiry: defaultInquiryFalse,
  };

  const defaultImages : Images = {
   imageFirst: ConfirmDefaultImage01,
   imageSecond: ConfirmDefaultImage02
  }



  return (
    <>
      <div>에그로그 메인이에요</div>
      <LoginInputBox type={"email"} />
      <LoginInputBox type={"password"} />
      <LoginButton onClick={handleLoginClick} />

        <Title title={"소제목"} />
        <UserInfo user={defaultUser} />
        <InquiryUserListBox inquiry={defaultInquiryUserTrue} />
        <InquiryUserListBox inquiry={defaultInquiryUserFalse} />
        <ConfirmAcceptButton id={new BigNumber(1)} />
        <ConfirmDenyButton id={new BigNumber(1)} />
        <ChangeStatusButton id={new BigNumber(1)} />
        <MailSendButton email={defaultUser.email} />
        {/*<ConfirmImageBox images={defaultImages}/>*/}
      <InquiryDetail content={defaultInquiryTrue.content}/>
        <ContentTitle title={"문의사항"} />
      <InfoDetailList/>

      <TabBar/>
      <MailInputBox/>
      <SideBar/>
    </>
  );
};

export default MainPage;
