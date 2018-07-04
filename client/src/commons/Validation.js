export const validateLength = (string, min, max) => {
    if (string) {
        let length = string.trim().length;
        console.log("LENGTH" + length);
        if (length >= min && length <= max) {
            return true;
        }
    }
    return false;
}

export const validateNumberRange = (number, min, max) => {

    if (number >= min && number <= max) {
        return true;
    }
    return false;
}

export const validateRegex = (string, regexp) => {
    if (string) {
        string = string.match(regexp);
        if (string) {
            return string[0];
        }
    }
    return false;
}

export const validateStringChars = (string) => {
    return validateRegex(string, "^[A-Za-z0-9 _:]*[A-Za-z0-9][A-Za-z0-9 _]*$")
}

export const validateStringCharsNoSpaces = (string) => {
    return validateRegex(string, "^[A-Za-z0-9_]*[A-Za-z0-9][A-Za-z0-9_]*$");
}

export const validateEmail = (email) => {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

export const areStringEqual = (string1, string2) => {
    if (string1 && string2) {
        if (string1.trim().localeCompare(string2.trim()) == 0) {
            console.log('equal')
            return true;
        }
    }
    return false;
}

export const isStringEmpty = (string) => {
    if (string == "") {
        return true;
    }
    return false;
}

export const isEditorStateEmpty = (string) => {
    if (string && string.trim().localeCompare("<p></p>") === 0) {
        return true;
    }
    return false;
}