import React, { useEffect } from "react";
import ReactDOM from "react-dom/client";
import Router from "./router";
import "./assets/styles/index.css";
import GlobalStyle from "./assets/styles/globalStyle";
// import GlobalStyle from "./styles/globalStyle";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

declare var sayHello: any;

const App = () => {
  useEffect(() => {
    window.sayHello = new CustomEvent("NativeEvent");
    const nativeEventCallback = (event: any) => {
      alert(`event receive from Native`);
    };

    window.addEventListener("NativeEvent", nativeEventCallback);

    // event listener clean up
    return () => {
      window.removeEventListener("NativeEvent", nativeEventCallback);
    };
  }, []);

  return (
    <>
      <GlobalStyle />
      <Router />
    </>
  );
};
root.render(<App />);
