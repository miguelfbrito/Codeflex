export const sum = (a,b) => {
   console.log(a+b); 
}

export const textToLowerCaseNoSpaces = (text) => {
    return text.toLowerCase().replace(/\s+/g, '-');
}