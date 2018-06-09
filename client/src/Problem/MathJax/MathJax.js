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
        if (typeof MathJax.Hub !== "undefined") {
            MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
        }
    }

    render() {

        return (
            <div>
                <Script
                    url="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-MML-AM_CHTML"
                    onCreate={this.handleScriptCreate.bind(this)}
                    onError={this.handleScriptError.bind(this)}
                    onLoad={this.handleScriptLoad.bind(this)}
                />
                <p>
                    {this.props.text}
                </p>
            </div>
        );
    }
}

export default MathJax;