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
        d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();

}

export const timeUntilDate = (initialDate, finalDate) => {
    let i = new Date(initialDate);
    let f = new Date(finalDate);

    return f.getTime() - i.getTime();
}

// Taken from: https://stackoverflow.com/questions/19700283/how-to-convert-time-milliseconds-to-hours-min-sec-format-in-javascript
export const getTimeHoursMins = (millisec) => {
    var seconds = (millisec / 1000).toFixed(0);
    var minutes = Math.floor(seconds / 60);
    var hours = "";
    if (minutes > 59) {
        hours = Math.floor(minutes / 60);
        hours = (hours >= 10) ? hours : "0" + hours;
        minutes = minutes - (hours * 60);
        minutes = (minutes >= 10) ? minutes : "0" + minutes;
    }

    seconds = Math.floor(seconds % 60);
    seconds = (seconds >= 10) ? seconds : "0" + seconds;
    if (hours != "") {
        return hours + ":" + minutes;
    }
    return "00 : " + minutes;
}

// Taken from https://stackoverflow.com/questions/19700283/how-to-convert-time-milliseconds-to-hours-min-sec-format-in-javascript
export const msToTime = (duration) => {
    let milliseconds = parseInt((duration % 1000) / 100),
        seconds = parseInt((duration / 1000) % 60),
        minutes = parseInt((duration / (1000 * 60)) % 60),
        hours = parseInt((duration / (1000 * 60 * 60)) % 24);

    hours = (hours < 10) ? "0" + hours : hours;
    minutes = (minutes < 10) ? "0" + minutes : minutes;
    seconds = (seconds < 10) ? "0" + seconds : seconds;

    return hours + ":" + minutes + ":" + seconds;
}


export const getDatesRange = (start, end) => {
    var
        arr = new Array(),
        dt = new Date(start);

    while (dt <= end) {
        arr.push(new Date(dt));
        dt.setDate(dt.getDate() + 1);
    }
    return arr;

}

export const getRndInteger = (min, max) => {
    return Math.floor(Math.random() * (max - min)) + min;
}


export const parseJwt = (token) => {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}

export const parseLocalJwt = () => {
    return parseJwt(localStorage.getItem('token'));
}

export const getToken = () => {
    return localStorage.getItem('token');
}

export const getAuthorization = () => {
    return {
        'Authorization': 'Bearer ' + getToken()
    }
}