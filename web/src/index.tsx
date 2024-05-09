import React, { useEffect } from "react";
import ReactDOM from "react-dom/client";
import Router from "./router";
import "./assets/styles/index.css";
import GlobalStyle from "./assets/styles/globalStyle";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

const App = () => {
  return (
    <>
      <GlobalStyle />
      <Router />
    </>
  );
};
root.render(<App />);
