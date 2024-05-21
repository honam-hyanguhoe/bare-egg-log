import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import RemainingDutyPage from "../components/pages/RemainingDutyPage";
import WorkStaticsPage from "../components/pages/WorkStaticsPage";
import MainPage from "../components/pages/MainPage";
import PrivacyPolicyPage from "../components/pages/PrivacyPolicyPage";
import ServiceAgreementPage from "../components/pages/ServiceAgreementPage";
import ErrorPage from "../components/pages/ErrorPage";

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/privacy" element={<PrivacyPolicyPage />} />
        <Route path="/agreement" element={<ServiceAgreementPage />} />
        <Route path="/remain" element={<RemainingDutyPage />} />
        <Route path="/statics" element={<WorkStaticsPage />} />
        <Route path="/error" element={<ErrorPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
