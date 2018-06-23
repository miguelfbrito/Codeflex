import React from 'react';
import ReactDOM from 'react-dom';
import Script from 'react-load-script';

class MathJax extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            scriptLoaded: false
        }
    }

    handleScriptLoad() {
        this.setState({ scriptLoaded: true })
    }

    componentDidUpdate(){
        console.log('did update')
    }

    render() {
        return (
            <div>
                {this.props.text}
            </div>
        );
    }
}

export default MathJax;