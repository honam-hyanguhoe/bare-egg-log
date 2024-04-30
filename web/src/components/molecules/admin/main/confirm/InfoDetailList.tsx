import React from "react";
import styled from "styled-components";
import InfoDetailListElement from "../../../../atoms/admin/main/confirm/InfoDetailListElement";
import {InfoDetailElement} from "@custom-types/InfoDetailElement";
import InfoDetailTitle from "../../../../atoms/admin/main/confirm/InfoDetailTitle";

const StyledInfoDetailList = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    gap: 0.5em;
    & > .infoDetailList {
        padding-left: 1em;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        gap: 0.3em;
    }
`;

const InfoDetailList = () => {
    const defaultInfoDetailElement : InfoDetailElement = {
        item: "항목",
        desc: "동그란병원"
    }
    return <StyledInfoDetailList>
        <InfoDetailTitle title={"병원 정보"}/>
        <div className={"infoDetailList"}>
            <InfoDetailListElement infoDetailElement={defaultInfoDetailElement}/>
            <InfoDetailListElement infoDetailElement={defaultInfoDetailElement}/>
            <InfoDetailListElement infoDetailElement={defaultInfoDetailElement}/>
            <InfoDetailListElement infoDetailElement={defaultInfoDetailElement}/>
        </div>
    </StyledInfoDetailList>
}
export default InfoDetailList;