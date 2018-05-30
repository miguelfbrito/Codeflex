export const sum = (a, b) => {
    console.log(a + b);
}

export const textToLowerCaseNoSpaces = (text) => {
    return text.toLowerCase().replace(/\s+/g, '-');
}

export const splitUrl = (text) => {
    if (text.charAt(0) === ('/')) {
        text = text.substring(1, text.length);
    }
    return text.split('/');
}