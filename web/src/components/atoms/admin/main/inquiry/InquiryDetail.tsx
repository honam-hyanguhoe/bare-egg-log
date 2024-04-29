import React from 'react';
import styled from 'styled-components';


const StyledInquiryDetail = styled.h3`
  font-size: 1em;
  font-weight: 400;
  color: #000;
  width: 100%;
  display: flex;
  justify-content: center;
  font-family: "Line-Seed-Sans-App";
`;

const InquiryDetail= ({ content } : { content: string }) => {
    return <StyledInquiryDetail>{content}</StyledInquiryDetail>;
};

export default InquiryDetail;
