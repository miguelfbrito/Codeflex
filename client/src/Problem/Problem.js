import React, { Component } from 'react';
import Script from 'react-load-script';
import AceEditor from 'react-ace';
import brace from 'brace';

import 'brace/mode/java';
import 'brace/mode/javascript';
import 'brace/mode/csharp';
import 'brace/mode/c_cpp';
import 'brace/mode/python';

import 'brace/theme/github';

class Problem extends Component {

    constructor(props) {
        super(props);
        this.state = {
        }

    }

    onChange(newValue) {
        console.log(newValue);
    }

    /* https://github.com/securingsincity/react-ace */

    render() {
        const aceStyle = {
            width: '80%',
            border: '2px solid #ccc',
            boxShadow: '0px 4px 80px -25px rgba(0,0,0,0.75)'
        }

        return (
            <AceEditor
                style={aceStyle}
                mode="javascript"
                theme="tomorrow"
                name=""
                onLoad={this.onLoad}
                onChange={this.onChange}
                fontSize={14}
                showPrintMargin={true}
                showGutter={true}
                highlightActiveLine={true}
                value={`function onLoad(editor) {
  console.log("i've loaded");
  console.log()
}`}
                setOptions={{
                    enableBasicAutocompletion: true,
                    enableLiveAutocompletion: true,
                    enableSnippets: false,
                    showLineNumbers: true,
                    tabSize: 2,
                }} />
        );
    }
}

export default Problem;
