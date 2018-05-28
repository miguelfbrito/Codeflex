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

import './Problem.css';

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
            minWidth: '80%',
            border: '2px solid #ccc',
            boxShadow: '0px 4px 80px -25px rgba(0,0,0,0.75)'
        }

        const problemName = <h2>Problem 1 : Calculating Prime Numbers</h2>;

        const problemDescription = <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Totam incidunt tempora quo, assumenda vitae nobis! Voluptates dolore placeat architecto enim quis ad, corporis assumenda amet laudantium praesentium. Expedita in optio reiciendis eum repellendus adipisci labore ipsam quos quia porro eaque, aspernatur incidunt temporibus dicta iure qui nemo distinctio nobis, velit, repudiandae amet! Neque nobis accusantium voluptatibus distinctio, incidunt, est fugiat voluptate fugit quae atque perferendis vitae magnam optio repellat dicta? Doloremque, eligendi tempore. Tenetur iste pariatur dolores quibusdam enim. Impedit, voluptates. Ad voluptatum suscipit laboriosam ut, quo dignissimos sunt amet totam nam! Necessitatibus modi, sequi dignissimos nostrum dolor porro inventore harum, illum a natus veniam quae beatae ut! Nostrum consequatur ducimus vitae, quibusdam est minus provident asperiores deserunt eligendi? Veniam unde modi eligendi commodi sed? Modi quo doloribus saepe ea quaerat vitae distinctio unde veritatis libero eaque cupiditate corrupti tempore amet quis atque reprehenderit, et voluptate inventore reiciendis, sint, dolores quas? Quod harum doloribus esse, nam obcaecati vero voluptas suscipit repellendus culpa, libero molestiae dolorem facere expedita, quibusdam excepturi deleniti dolore modi ipsa magni? Enim consequatur aliquam explicabo aut rem veritatis provident, quod aperiam unde repellendus velit odio tempora, eum fuga doloribus sapiente? Cupiditate quo itaque, sapiente atque magnam earum?</p>

        return (
            <div className="problem-container">
                {problemName}

                {problemDescription}

                <div className="ace-editor">
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
                </div>
            </div>
        );
    }
}

export default Problem;
