import React, { useEffect } from "react";
import ReactDOM from "react-dom/client";
import Router from "./router";
import "./assets/styles/index.css";
import GlobalStyle from "./assets/styles/globalStyle";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

const App = () => {
  function receiveDataFromApp(data: String) {
    console.log("Data received: " + data);
    alert(`들어오니... ${data}`);
    return "호남이 : " + data;
  }

  return (
    <>
      <GlobalStyle />
      <Router />
    </>
  );
};
root.render(<App />);
