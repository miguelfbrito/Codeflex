import { EditorState, convertToRaw } from 'draft-js';
import {stateToHTML} from 'draft-js-export-html';
import { draftToHtml } from 'draftjs-to-html';
import { isEditorStateEmpty, areStringEqual, validateEmail, validateLength, validateStringChars, isStringEmpty, validateNumberRange } from '../../commons/Validation';
import { ToastContainer, toast } from 'react-toastify';

export const convertDraftToHtml = (editorstate) => {
    if (editorstate) {
        return stateToHTML(editorstate.getCurrentContent());
    }
    return "<p></p>";
}

export const validateSaveProblem = (data) => {
    console.log("DATA VALIDATIOn");
    console.log(data);

    if (isStringEmpty(data.problemName) || isStringEmpty(data.difficulty.name) ||
        isEditorStateEmpty(convertDraftToHtml(data.problemDescription)) ||
        isEditorStateEmpty(convertDraftToHtml(data.problemConstraints)) ||
        isEditorStateEmpty(convertDraftToHtml(data.problemInputFormat)) ||
        isEditorStateEmpty(convertDraftToHtml(data.problemOutputFormat)) ||
        isStringEmpty(data.problemMaxScore)) {
        toast.error("Fill in all the data", { autoClose: 2500 })
        return false;
    } else {

        if (!validateLength(data.problemName, 5, 50)) {
            toast.error("Problem name must be between 5 and 50 characters")
            return false;
        } else {
            if (!validateStringChars(data.problemName)) {
                toast.error("Name can only contain letters, numbers and _")
                return false;
            }
        }
        if (validateLength(convertDraftToHtml(data.problemDescription, 0, 5000))) {
            toast.error("Description can't have more than 5000 character")
            return false;
        }

        if (validateLength(convertDraftToHtml(data.problemConstraints, 0, 5000))) {
            toast.error("Constraints can't have more than 5000 characters")
            return false;
        }
        if (validateLength(convertDraftToHtml(data.problemInputFormat, 0, 5000))) {
            toast.error("Input format can't have more than 5000 characters")
            return false;
        }
        if (validateLength(convertDraftToHtml(data.outputFormat, 0, 5000))) {
            toast.error("Output format can't have more than 5000 characters")
            return false;
        }

        if (!validateNumberRange(parseInt(data.problemMaxScore), 10, 250)) {
            toast.error("Max score has to be between 10 and 250")
            return false;
        }

    }

    return true;
}
