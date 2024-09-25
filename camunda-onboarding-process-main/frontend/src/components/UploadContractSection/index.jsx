import { Button } from "@mui/material";
import axios from "axios";
import { MuiFileInput } from "mui-file-input";
import { useState } from "react";
import { UPLOAD_CONTRACT_URL } from "../../constants";

export default function UploadContractSection({ handleSetToast }) {
    const [contract, setContract] = useState(null);
    const [errorMsg, setErrorMsg] = useState("");

    function handleSubmit(e) {
        e.preventDefault();

        if (!isFormDataValid()) return;
        
        const formData = new FormData();
        const queryParameters = new URLSearchParams(window.location.search);
        formData.append("processId", queryParameters.get("processId"));
        formData.append("contract", contract);
        
        axios.post(UPLOAD_CONTRACT_URL, formData, {
            "Content-Type": "multipart/form-data"
        }).then(() => handleSuccess()).catch(() => {
            handleSetToast("Could not upload contract.", "error");
        })
    }

    function handleSuccess() {
        handleSetToast("Contract uploaded successfully.", "success");
        setContract(null);
        setErrorMsg("");
    }


    function isFormDataValid() {
        if (contract === null) {
            setErrorMsg("Please attach a file.")
            return false;
        }
        if (contract && contract.size > 1048576) {
            setErrorMsg("Contract size can be at most 1 MB.")
            return false;
        }
        return true;
    }

    return (
        <div>
            <h1 className="text-xl">Upload Contract</h1>
            <form onSubmit={handleSubmit}>
                <MuiFileInput fullWidth variant="standard" margin="normal"
                    placeholder="Add File" value={contract} onChange={file => setContract(file)}
                />
                <div className="text-right mt-5 flex flex-row-reverse justify-between items-center gap-5">
                    <Button type="submit" variant="contained" style={{ backgroundColor: "#30B8A1" }}>Submit</Button>
                    {errorMsg &&
                        <p className="text-red-500 text-sm text-left">{errorMsg}</p>
                    }
                </div>
            </form>
        </div>
    )
}