import { FormControl, InputLabel, MenuItem, Select, TextField } from "@mui/material";
import Button from '@mui/material/Button';
import axios from "axios";
import { MuiFileInput } from "mui-file-input";
import { useState } from "react";
import { START_PROCESS_URL } from "../../constants";

const getTodayDate = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
  const day = String(today.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

export default function Form({ handleSetToast, setIsFormSubmitted }) {
  const [fullName, setFullName] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("2000-01-01");
  const [emailAddress, setEmailAddress] = useState("");
  const [identificationNumber, setIdentificationNumber] = useState("");
  const [identificationValidity, setIdentificationValidity] = useState("2026-01-01");
  const [nif, setNIF] = useState("");
  const [niss, setNISS] = useState("");
  const [address, setAddress] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [maritalStatus, setMaritalStatus] = useState("");
  const [numberOfDependents, setNumberOfDependents] = useState(0);
  const [iban, setIBAN] = useState("");
  const [startDate, setStartDate] = useState(getTodayDate());
  const [photo, setPhoto] = useState(null);
  const [certificateOfQualifications, setCertificateOfQualifications] = useState(null);

  const [errorMsg, setErrorMsg] = useState("");

  return (
    <form onSubmit={handleSubmit}>
      <TextField required fullWidth variant="standard"
        label="Full Name" value={fullName} onChange={e => setFullName(e.target.value)}
      />
      <TextField required fullWidth variant="standard" margin="normal" type="date"
        label="Date of Birth" value={dateOfBirth} onChange={e => setDateOfBirth(e.target.value)}
      />
      <TextField required fullWidth variant="standard" margin="normal" type="email"
        label="Email Address" value={emailAddress} onChange={e => setEmailAddress(e.target.value)}
      />
      <TextField required fullWidth variant="standard" margin="normal"
        label="Citizen Card (CC) Number" value={identificationNumber} onChange={e => setIdentificationNumber(e.target.value)} inputProps={{ maxLength: 9 }} 
      />
      <TextField required fullWidth variant="standard" margin="normal" type="date"
        label="Citizen Card (CC) Validity" value={identificationValidity} onChange={e => setIdentificationValidity(e.target.value)}
      />
      <TextField required fullWidth variant="standard" margin="normal"
        label="NIF" value={nif} onChange={e => setNIF(e.target.value)} inputProps={{ maxLength: 9 }} 
      />
      <TextField required fullWidth variant="standard" margin="normal"
        label="NISS" value={niss} onChange={e => setNISS(e.target.value)} inputProps={{ maxLength: 11 }} 
      />
      <TextField required fullWidth variant="standard" margin="normal"
        label="Address" value={address} onChange={e => setAddress(e.target.value)}
      />
      <TextField required fullWidth variant="standard" margin="normal"
        label="Postal Code" value={postalCode} onChange={e => setPostalCode(e.target.value)}
      />
      <FormControl required fullWidth variant="standard" margin="normal">
        <InputLabel>Marital Status</InputLabel>
        <Select value={maritalStatus} onChange={e => setMaritalStatus(e.target.value)}>
          <MenuItem value="Não Casado">Single</MenuItem>
          <MenuItem value="Casado, único titular">Married (Sole Holder)</MenuItem>
          <MenuItem value="Casado, dois titulares">Married (Two Holders)</MenuItem>
          <MenuItem value="Não Casado – portador de deficiência">Single - Disabled</MenuItem>
          <MenuItem value="Casado, único titular - portador de deficiência">Married (Sole Holder) - Disabled</MenuItem>
          <MenuItem value="Casado, dois titulares - portador de deficiência">Married (Two Holders) - Disabled</MenuItem>
        </Select>
      </FormControl>
      <TextField required fullWidth variant="standard" margin="normal" type="number" InputProps={{ inputProps: { min: 0, max: 10 } }}
        label="Number of Dependents" value={numberOfDependents} onChange={e => setNumberOfDependents(e.target.value)}
      />
      <TextField required fullWidth variant="standard" margin="normal"
        label="IBAN" value={iban} onChange={e => setIBAN(e.target.value)} inputProps={{ maxLength: 25 }} 
      />
      <TextField required fullWidth variant="standard" margin="normal" type="date"
        label="Start Date" value={startDate} onChange={e => setStartDate(e.target.value)}
      />
      <MuiFileInput fullWidth variant="standard" margin="normal"
        label="Photo" placeholder="Add File" value={photo} onChange={file => setPhoto(file)}
      />
      <MuiFileInput fullWidth variant="standard" margin="normal"
        label="Certificate of Qualifications" placeholder="Add File" value={certificateOfQualifications} onChange={file => setCertificateOfQualifications(file)}
      />
      <div className="text-right mt-5 flex flex-row-reverse justify-between items-center gap-5">
        <Button type="submit" variant="contained" style={{ backgroundColor: "#30B8A1" }}>Submit</Button>
        {errorMsg &&
          <p className="text-red-500 text-sm text-left">{errorMsg}</p>
        }
      </div>
    </form>
  )

  function handleSubmit(e) {
    e.preventDefault();
    if (!isFormDataValid()) return;

    const blob = new Blob([JSON.stringify({
      fullName,
      dateOfBirth,
      emailAddress,
      identificationNumber,
      identificationValidity,
      nif,
      niss,
      address,
      postalCode,
      maritalStatus,
      numberOfDependents,
      iban,
      startDate
    })], { type: 'application/json' });

    const formData = new FormData();
    const key = new URLSearchParams(window.location.search).get("key");
    formData.append("accessKey", key);
    formData.append("newcomerInfo", blob);
    if (photo) formData.append("photo", photo);
    if (certificateOfQualifications) formData.append("certificateOfQualifications", certificateOfQualifications)

    axios.post(START_PROCESS_URL, formData, {
      "Content-Type": "multipart/form-data"
    }).then(() => handleSuccess())
      .catch(() => handleSetToast("Could not submit form. Try again later.", "error"));
  }

  function handleSuccess() {
    setIsFormSubmitted(true);
  }

  function isFormDataValid() {
    if (fullName === "") {
      setErrorMsg("Full Name cannot be empty.")
      return false;
    }
    if (dateOfBirth === "") {
      setErrorMsg("Date of Birth cannot be empty.")
      return false;
    }
    if (emailAddress === "") {
      setErrorMsg("Email Address cannot be empty.")
      return false;
    }
    if (identificationNumber === "") {
      setErrorMsg("Citizen Card (CC) Number cannot be empty.")
      return false;
    }
    if (identificationValidity === "") {
      setErrorMsg("Citizen Card (CC) Validity cannot be empty.")
      return false;
    }
    if (nif === "") {
      setErrorMsg("NIF cannot be empty.")
      return false;
    }
    if (niss === "") {
      setErrorMsg("NISS cannot be empty.")
      return false;
    }
    if (address === "") {
      setErrorMsg("Address cannot be empty.")
      return false;
    }
    if (postalCode === "") {
      setErrorMsg("Postal Code cannot be empty.")
      return false;
    }
    if (maritalStatus === "") {
      setErrorMsg("Marital Status cannot be empty.")
      return false;
    }
    if (numberOfDependents === "") {
      setErrorMsg("Number of Dependents cannot be empty.")
      return false;
    }
    if (iban === "") {
      setErrorMsg("IBAN cannot be empty.")
      return false;
    }
    if (startDate === "") {
      setErrorMsg("Start Date cannot be empty.")
      return false;
    }
    if (photo && photo.size > 1048576) {
      setErrorMsg("Photo size can be at most 1 MB.")
      return false;
    }
    if (certificateOfQualifications && certificateOfQualifications.size > 1048576) {
      setErrorMsg("Certificate of Qualifications size can be at most 1 MB.")
      return false;
    }
    return true;
  }
}