import React from 'react';
import styled from 'styled-components';
import MainLogoIcon from '../../../../assets/images/default/main_logo.svg';

const StyledMainLogo = styled.div`
    display: flex;
    justify-content: center;
`;

const MainLogo = () => {
    const someAction= () => {
        alert(`메인 로고가 클릭되었습니다!`);
    };
    return <StyledMainLogo onClick={someAction}>
        <img src={MainLogoIcon} alt={"에그로그-호남향우회"}/>
    </StyledMainLogo>
}
export default MainLogo;
