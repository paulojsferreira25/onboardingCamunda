import { TextField } from "@mui/material";
import Button from '@mui/material/Button';
import axios from "axios";
import { useState } from "react";
import { SEND_ONBOARDING_FORM_URL } from "../../constants";

export default function SendOnboardingFormSection({ handleSetToast }) {
  const [firstName, setFirstName] = useState("");
  const [emailAddress, setEmailAddress] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  return (
    <div>
      <h1 className="text-xl">Send Onboarding Form to Newcomer</h1>
      <form onSubmit={handleSubmit}>
        <TextField required fullWidth variant="standard" margin="normal"
          label="Newcomer First Name" value={firstName} onChange={e => setFirstName(e.target.value)}
        />
        <TextField required fullWidth variant="standard" margin="normal" type="email"
          label="Newcomer Email Address" value={emailAddress} onChange={e => setEmailAddress(e.target.value)}
        />
        <div className="text-right mt-5 flex flex-row-reverse justify-between items-center gap-5">
          <Button type="submit" variant="contained" style={{ backgroundColor: "#30B8A1" }}>Send</Button>
          {errorMsg &&
            <p className="text-red-500 text-sm text-left">{errorMsg}</p>
          }
        </div>
      </form>
    </div>
  )

  function handleSubmit(e) {
    e.preventDefault();
    if (!isFormDataValid()) return;

    axios.post(SEND_ONBOARDING_FORM_URL, {
      firstName, emailAddress
    }).then(() => handleSuccess())
      .catch(() => handleSetToast("Could not send form to newcomer.", "error"));
  }

  function isFormDataValid() {
    if (firstName === "") {
      setErrorMsg("Newcomer First Name cannot be empty.")
      return false;
    }
    if (emailAddress === "") {
      setErrorMsg("Newcomer Email Address cannot be empty.")
      return false;
    }
    return true;
  }

  function handleSuccess() {
    setFirstName("");
    setEmailAddress("");
    handleSetToast("Form sent to newcomer successfully.", "success");
  }
}