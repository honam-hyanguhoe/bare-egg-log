import React from "react";
import styled from "styled-components";
import TabBox from "../../../../atoms/admin/main/common/TabBox";

const StyledTabBar = styled.div`
    display: flex;
    flex-direction: row;
`;

const TabBar = () => {
    return <StyledTabBar>
        <TabBox isActive={true} text={"선택1"} />
        <TabBox isActive={false} text={"선택2"} />
    </StyledTabBar>
}

export default TabBar;
