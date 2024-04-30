import React from "react";
import styled from "styled-components";
import MainLogo from "../../../atoms/admin/sidebar/MainLogo";
import CategoryList from "../../../molecules/admin/sidebar/CategoryList";
import LogoutButton from "../../../atoms/admin/sidebar/LogoutButton";

const StyledSideBar = styled.div`
  background-color: #333;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 30em;
  width: 20vw;
  min-width: 17em;
  padding: 3em 1em 1em;
  
  & > .upperSideBar {
    display: flex;
    flex-direction: column;
    gap: 5em;
  }
`;
const SideBar = () => {
  return <StyledSideBar>
    <div className={"upperSideBar"}>
      <MainLogo />
      <CategoryList />
    </div>
    <LogoutButton />
  </StyledSideBar>;
};
export default SideBar;
