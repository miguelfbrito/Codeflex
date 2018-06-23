$(document).ready(function () {
    console.log('Page ready from jquery')
    if (typeof MathJax.Hub != "undefined") {
        MathJax.Hub.Queue(["Typeset", MathJax.Hub])
    }
});