import React from 'react';
import Script from 'react-load-script';

class MathJax extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            scriptLoaded: false,
            scriptError: false
        }
    }

    handleScriptCreate() {
        this.setState({ scriptLoaded: false })
    }

    handleScriptError() {
        this.setState({ scriptError: true })
        console.log('Error loading!')
    }

    handleScriptLoad() {
        this.setState({ scriptLoaded: true })
        console.log('Loaded script!')
    }

    escapeBracket(text){
        console.log(text);
        text.replace("{", "&#123");
        text.replace("}", "&#125");
        return text;
    }

/*
Left Curly Brace { : &#123;
Right Curly Brace } : &#125;
*/
    render() {
        let variable = <p>\(ax^2 + bx + c = 0\)</p>;
        let variable2 = "\\(x = b^2 - \\sqrt{\\frac{2a}{4c}} * 2^{3} <= 3 \\prod \\)"
        let variable3 = " \\( \\frac{2}{9} \\)"
        return (
            <div>
                <h1>MathJax</h1>
                <Script
                    url = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-MML-AM_CHTML"
                    onCreate={this.handleScriptCreate.bind(this)}
                    onError={this.handleScriptError.bind(this)}
                    onLoad={this.handleScriptLoad.bind(this)}
                />

                {variable}
                <p> 
{this.escapeBracket(variable2)}
                </p>
                <p>{this.escapeBracket(variable3)}</p>
            </div>
        );
    }
}

export default MathJax;