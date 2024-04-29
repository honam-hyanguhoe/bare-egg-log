import React from 'react';
import styled from 'styled-components';


const StyledInquiryDetail = styled.h3`
  font-size: 1.3em;
  font-weight: 600;
  color: #258D3F;
  font-family: "Line-Seed-Sans-App";
`;

const InquiryDetail= ({ title } : { title: string }) => {
    return <StyledInquiryDetail>{title}</StyledInquiryDetail>;
};

export default InquiryDetail;