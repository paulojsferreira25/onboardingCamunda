import axios from "axios";
import { useEffect, useState } from "react";
import { VALIDATE_ACCESS_KEY_URL } from "../../constants";
import Paragraph from "../Paragraph";
import Form from "./Form";

export default function FormSection({ handleSetToast }) {
  const [hasValidKey, setHasValidKey] = useState(false);
  const [isFormSubmitted, setIsFormSubmitted] = useState(false);

  useEffect(() => {
    // validate access key
    const key = new URLSearchParams(window.location.search).get("key");
    axios.get(VALIDATE_ACCESS_KEY_URL + key).then(resp => {
      if (resp.data["isValid"]) setHasValidKey(true);
    }).catch(() => console.log("Network error."))
  }, []);

  return (
    <>
      {hasValidKey ?
        isFormSubmitted ?
          <Paragraph text="Thank you for your response! We'll get back to you soon." />
          : <Form handleSetToast={handleSetToast} setIsFormSubmitted={setIsFormSubmitted} />
        : <Paragraph text="You don't have permission to view this form." />
      }
    </>
  )
}