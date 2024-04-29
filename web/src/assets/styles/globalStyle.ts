import { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`
 * {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  list-style: none;
  text-decoration: none;
 }

 body{
   background-color: black;
    width: 100%;
    height: 100vh;
    position: relative;
 }

 .action-button-container {
   position: fixed;
   bottom: 30px;
   right: 30px;
  }
 
`;

export default GlobalStyle;
