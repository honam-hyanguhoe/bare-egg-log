import React from 'react';
import styled from 'styled-components';
import {InfoDetailElement} from "@custom-types/InfoDetailElement";
interface InfoDetailElementProps{
    infoDetailElement: InfoDetailElement
}

const StyledInfoDetailElement = styled.div`
  font-size: 1.1em;
  font-weight: 600;
  color: #000;
  font-family: "Line-Seed-Sans-App";
`;

const InfoDetailListElement= (data : InfoDetailElementProps) => {
    const infoDetailElement:InfoDetailElement=data.infoDetailElement;
    return <StyledInfoDetailElement>{infoDetailElement.item} : {infoDetailElement.desc}</StyledInfoDetailElement>;
};

export default InfoDetailListElement;