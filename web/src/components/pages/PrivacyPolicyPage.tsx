import React from "react";
import styled from "styled-components";

const PageBody = styled.div`
  font-family: "Line-Seed-Sans-App";
  font-weight: 800;
  padding: 20px;
  @media (max-width: 768px) {
    padding: 10px;
  }
`;

const PageTitle = styled.p`
  font-size: 2rem;
  font-weight: 800;
  color: #333;
  margin-top: 20px;
  margin-bottom: 10px;
  @media (max-width: 768px) {
    font-size: 1.6rem;
  }
`;
const SectionTitle = styled.p`
  font-size: 1.5rem;
  font-weight: 800;
  color: #333;
  margin-top: 20px;
  margin-bottom: 10px;
  @media (max-width: 768px) {
    font-size: 1.2rem;
  }
`;

const Article = styled.p`
  color: #666;
  font-weight: 400;
  margin-bottom: 10px;
  @media (max-width: 768px) {
    font-size: 0.9rem;
  }
`;

const ListUl = styled.ul``;

const ListItem = styled.li`
  margin-bottom: 10px;
  font-weight: 400;
  @media (max-width: 768px) {
    font-size: 0.8rem;
  }
`;

const Table = styled.table`
  width: 100%;
  font-weight: 400;
  border-collapse: collapse;
  margin-bottom: 20px;
  /* @media (max-width: 768px) {
    display: block;
    overflow-x: auto;
    white-space: nowrap;
  } */
`;

const TH = styled.th`
  background-color: #f4f4f4;
  padding: 10px;
  border: 1px solid #ddd;
`;

const TD = styled.td`
  text-align: center;
  padding: 8px;
  border: 1px solid #ddd;
`;

const ServiceAgreementPage = () => {
  return (
    <PageBody>
      <PageTitle>개인정보처리방침</PageTitle>
      <Article>
        <b>에그로그</b>는 정보주체의 자유와 권리 보호를 위해 「개인정보 보호법」
        및 관계 법령이 정한 바를 준수하여, 적법하게 개인정보를 처리하고 안전하게
        관리하고 있습니다. 이에 「개인정보 보호법」 제30조에 따라 정보주체에게
        개인정보 처리에 관한 절차 및 기준을 안내하고, 이와 관련한 고충을
        신속하고 원활하게 처리할 수 있도록 하기 위하여 인터넷 사이트를 통해
        개인정보처리방침을 열람할 수 있도록 하고 있습니다.
      </Article>

      <SectionTitle>
        1. 개인정보의 처리 목적 및 수집 항목, 보관 기간
      </SectionTitle>
      <Table>
        <thead>
          <tr>
            <TH>서비스</TH>
            <TH>수집 목적</TH>
            <TH>수집 항목</TH>
            <TH>보유 기간</TH>
          </tr>
        </thead>
        <tbody>
          <tr>
            <TD>회원가입</TD>
            <TD>
              회원가입, 중복확인, 서비스 제공에 따른 본인 식별·인증, 본인확인,
              만 14세 이상 확인
            </TD>
            <TD>필수 성명, 이메일주소, 비밀번호, 병원명, 사번</TD>
            <TD>
              회원 탈퇴 혹은 본인 요청 시까지※ 단, 관계 법령 위반에 따른 수사,
              조사 등이 진행 중인 경우에는 해당 수사, 조사 종료시까지
            </TD>
          </tr>
          <tr>
            <TD>SNS 로그인</TD>
            <TD>
              회원가입, 중복확인, 서비스 제공에 따른 본인 식별·인증, 본인확인
            </TD>
            <TD>필수 이메일, 성명, 식별값선택 프로필 사진</TD>
            <TD> - </TD>
          </tr>
          <tr>
            <TD>푸시 알림</TD>
            <TD>푸시 알림 서비스 제공</TD>
            <TD>사용자 및 기기 정보(기기 구분을 위한 UUID,OS, FCM 토큰)</TD>
            <TD> - </TD>
          </tr>
        </tbody>
      </Table>

      <SectionTitle>2. 개인정보의 제 3자 제공 및 국외 위탁 처리</SectionTitle>
      <Article>
        <b>에그로그</b>는 원활한 서비스 제공을 위해 다음의 경우 정보주체의
        동의를 얻어 필요 최소한의 범위로만 제공합니다.
      </Article>
      <Table>
        <thead>
          <tr>
            <TH>업체명</TH>
            <TH>이용 목적</TH>
            <TH>이전 항목</TH>
            <TH>이전 일시 및 방법</TH>
            <TH>보유 기간</TH>
          </tr>
        </thead>
        <tbody>
          <tr>
            <TD>https://aws.amazon.com/ko/contact-us/compliance-support/</TD>
            <TD>개인정보가 저장된 인프라 운영</TD>
            <TD>서비스 이용 기록 또는 수집된 개인정보</TD>
            <TD>서비스 이용 시 네트워크 전송</TD>
            <TD>서비스 변경 시까지※ 클라우드 서비스 변경 시까지</TD>
          </tr>
        </tbody>
      </Table>

      <SectionTitle>3. 개인정보의 파기 절차 및 방법</SectionTitle>
      <Article>
        개인정보의 파기 사유가 발생한 즉시(회원 탈퇴, 본인 요청) 개인정보를
        파기합니다. 개인정보 파기의 절차 및 방법은 다음과 같습니다.
      </Article>
      <ul>
        <ListItem>파기 사유가 발생한 개인정보를 선정합니다.</ListItem>
        <ListItem>
          사용자 요청의 경우 개인정보 보호책임자의 승인을 받아 개인정보를
          파기합니다.
        </ListItem>
      </ul>

      <SectionTitle>
        4. 행태정보의 수집·이용·제공 및 거부 등에 관한 사항
      </SectionTitle>
      <Article>
        <b>에그로그</b>는 행태정보를 수집·이용·제공하지 않습니다.
      </Article>

      <SectionTitle>5. 개인정보 보호책임자</SectionTitle>
      <Article>
        개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한
        정보주체의 불만처리 및 피해구제, 정보 열람 등을 위하여 아래와 같이
        개인정보 보호책임자를 지정하고 있습니다.
      </Article>
      <ul>
        <ListItem>성명: 김다희</ListItem>
        <ListItem>연락처: eggrock2023@gmail.com</ListItem>
      </ul>

      <SectionTitle>6. 개인정보의 안전성 확보 조치</SectionTitle>
      <Article>
        <b>에그로그</b>는 이용자의 개인정보 보호를 위해 아래의 노력을 하고
        있습니다.
      </Article>
      <ul>
        <ListItem>
          개인정보를 암호화된 통신구간을 이용하여 전송하고, 비밀번호 등
          중요정보는 암호화하여 보관하고 있습니다.
        </ListItem>
        <ListItem>
          해킹이나 컴퓨터 바이러스로부터 보호하기 위하여 이상 징후를 감시하고
          있습니다.
        </ListItem>
        <ListItem>
          개인정보취급자는 이용자의 개인정보 보호에 대한 정기적인 교육을
          실시하고 있습니다.
        </ListItem>
      </ul>

      <SectionTitle>7. 권익침해 구제방법</SectionTitle>
      <Article>
        정보주체는 개인정보침해로 인한 구제를 받기 위하여
        개인정보분쟁조정위원회, 한국인터넷진흥원 개인정보침해신고센터 등에
        분쟁해결이나 상담 등을 신청할 수 있습니다.
      </Article>
      <ul>
        <ListItem>
          개인정보분쟁조정위원회: (국번없이) 1833-6972 (www.kopico.go.kr)
        </ListItem>
        <ListItem>
          개인정보침해신고센터: (국번없이) 118 (privacy.kisa.or.kr)
        </ListItem>
        <ListItem>대검찰청: (국번없이) 1301 (www.spo.go.kr)</ListItem>
        <ListItem>경찰청: (국번없이) 182 (ecrm.cyber.go.kr)</ListItem>
      </ul>

      <SectionTitle>8. 개인정보 처리방침의 변경</SectionTitle>
      <Article>
        이 개인정보 처리방침은 2024. 01. 01부터 적용됩니다. 이전의 개인정보
        처리방침은 다음 링크의 문서에서 확인하실 수 있습니다.
      </Article>
    </PageBody>
  );
};

export default ServiceAgreementPage;
