import { Alert } from '@mui/material';
import React, { useState } from 'react';
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import logo from "./assets/syone-logo.png";
import FormSection from "./components/FormSection";
import Paragraph from "./components/Paragraph";
import SendOnboardingFormSection from "./components/SendOnboardingFormSection";
import UploadContractSection from './components/UploadContractSection';

export default function App() {
  const [toast, setToast] = useState({ message: "", severity: "" });

  function handleSetToast(message, severity) {
    setToast({ message, severity });
    setTimeout(() => {
      setToast({ message: "", severity: "" });
    }, 5000);
  }

  return (
    <Router>
      <main className="sm:bg-slate-100 min-h-screen sm:py-10">
        <div className="bg-white w-full sm:w-[500px] mx-auto flex flex-col items-center sm:shadow-md">
          <div className="bg-black w-full h-40 flex justify-center items-center">
            <img className="w-36" src={logo} alt="Logo" />
          </div>
          <div className="w-full p-10">
            <Routes>
              <Route exact path="/" element={<Paragraph text="Nothing to show here." />} />
              <Route exact path="/form" element={<FormSection handleSetToast={handleSetToast} />} />
              <Route exact path="/send-onboarding-form" element={<SendOnboardingFormSection handleSetToast={handleSetToast} />} />
              <Route exact path="/upload-contract" element={<UploadContractSection handleSetToast={handleSetToast} />} />
            </Routes>
          </div>
        </div>
        {toast.message &&
          <Alert severity={toast.severity} elevation={2} variant="filled" onClose={() => setToast({ message: "", severity: "" })}
            style={{ width: "350px", position: "fixed", bottom: 10, right: 10 }} className="transition ease-in-out duration-1000">
            {toast.message}
          </Alert>
        }
      </main>
    </Router>
  )
}