import React from 'react';
import PathLink from '../../PathLink/PathLink'
import { Editor } from 'react-draft-wysiwyg';
import draftToHtml from 'draftjs-to-html';
import htmlToDraft from 'html-to-draftjs';
import { EditorState, convertToRaw, ContentState } from 'draft-js';

import { URL } from '../../commons/Constants';
import { splitUrl } from '../../commons/Utils';
import '../../../node_modules/react-draft-wysiwyg/dist/react-draft-wysiwyg.css'
import '../../commons/draft-editor.css'
import './AddProblem.css'

class AddProblem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            description: ''
        }

        this.onEditorStateChange = this.onEditorStateChange.bind(this);
    }

    uploadImageCallBack(file) {
        return new Promise(
            (resolve, reject) => {
                const xhr = new XMLHttpRequest();
                xhr.open('POST', 'https://api.imgur.com/3/image');
                xhr.setRequestHeader('Authorization', 'Client-ID 85af3a3fbda6df3');
                const data = new FormData();
                data.append('image', file);
                xhr.send(data);
                xhr.addEventListener('load', () => {
                    const response = JSON.parse(xhr.responseText);
                    resolve(response);
                });
                xhr.addEventListener('error', () => {
                    const error = JSON.parse(xhr.responseText);
                    reject(error);
                });
            }
        );
    }

    onEditorStateChange(change) {
        console.log(change);
        const contentState = change.getCurrentContent();
        console.log('content state', draftToHtml(convertToRaw(contentState)));
        this.setState({
            description: change
        });
    }

    render() {
        return (
            <div className="container add-problem">
                <div className="row ">
                    <PathLink path={this.props.location.pathname} title="Add problem" />
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Name</h3>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <input type="text" id="input-problem-name" placeholder="Problem name" />
                    </div>
                    {this.state.description !== '' && draftToHtml(convertToRaw(this.state.description.getCurrentContent()))}
                </div>

                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Problem Statement</h3>
                        <p>Explain your problem here. Keep it simple and clean.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <div className="text-editor-wrapper">
                            <Editor
                                id="description"
                                wrapperClassName="demo-wrapper "
                                editorClassName="demo-editor"
                                editorState={this.state.description}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onEditorStateChange}
                                toolbar={{
                                    inline: { inDropdown: true },
                                    list: { inDropdown: true },
                                    textAlign: { inDropdown: true },
                                    link: { inDropdown: true },
                                    image: { uploadCallback: this.uploadImageCallBack, alt: { present: true, mandatory: true } }
                                }}
                            />
                        </div>

                    </div>
                </div>
                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Constraints</h3>
                        <p>Include all your constraints here, such as input and output size limitations.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <div className="text-editor-wrapper">
                            <Editor
                                id="description"
                                wrapperClassName="demo-wrapper "
                                editorClassName="demo-editor"
                                editorState={this.state.description}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onEditorStateChange}
                                toolbar={{
                                    inline: { inDropdown: true },
                                    list: { inDropdown: true },
                                    textAlign: { inDropdown: true },
                                    link: { inDropdown: true },
                                    image: { uploadCallback: this.uploadImageCallBack, alt: { present: true, mandatory: true } }
                                }}
                            />
                        </div>
                    </div>
                </div>
                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Input Format</h3>
                        <p>Add your information regarding the expected input format.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <div className="text-editor-wrapper">
                            <Editor
                                id="description"
                                wrapperClassName="demo-wrapper "
                                editorClassName="demo-editor"
                                editorState={this.state.description}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onEditorStateChange}
                                toolbar={{
                                    inline: { inDropdown: true },
                                    list: { inDropdown: true },
                                    textAlign: { inDropdown: true },
                                    link: { inDropdown: true },
                                    image: { uploadCallback: this.uploadImageCallBack, alt: { present: true, mandatory: true } }
                                }}
                            />
                        </div>
                    </div>
                </div>
                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Output Format</h3>
                        <p>Add your information regarding the expected output format.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <div className="text-editor-wrapper">
                            <Editor
                                id="description"
                                wrapperClassName="demo-wrapper "
                                editorClassName="demo-editor"
                                editorState={this.state.description}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onEditorStateChange}
                                toolbar={{
                                    inline: { inDropdown: true },
                                    list: { inDropdown: true },
                                    textAlign: { inDropdown: true },
                                    link: { inDropdown: true },
                                    image: { uploadCallback: this.uploadImageCallBack, alt: { present: true, mandatory: true } }
                                }}
                            />
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-offset-2 col-sm-10 col-xs-12">
                        <input type="button" className="btn btn-primary" name="" id="save-problem" value="Save problem"/>
                    </div>
                </div>
            </div>
        )
    }
}

export default AddProblem;