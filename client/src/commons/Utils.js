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

export const dateWithDay = (date) => {
    let d = new Date(date);
    return d.getDate() + "/" + d.getMonth() + "/" + d.getFullYear();
}

export const dateWithHoursAndDay = (date) => {
    let d = new Date(date);

    return d.getHours() + ":" + (d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes()) + " " +
    d.getDate() + "/" + (d.getMonth()+1) + "/" + d.getFullYear();
    
}

export const timeUntilDate = (initialDate, finalDate) => {
    let i = new Date(initialDate);
    let f = new Date(finalDate);

    return f.getTime() - i.getTime();
}