import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from "../components/pages/Main";
import PrivacyPolicy from "../components/pages/PrivacyPolicy";
import ServiceAgreement from "../components/pages/ServiceAgreement";
import RemainingDutyPage from "../components/pages/RemainingDutyPage";
import WorkStaticsPage from "../components/pages/WorkStaticsPage";

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/privacy" element={<PrivacyPolicy />} />
        <Route path="/agreement" element={<ServiceAgreement />} />
        <Route path="/remain" element={<RemainingDutyPage />} />
        <Route path="/statics" element={<WorkStaticsPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
