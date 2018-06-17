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
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Add problem" />
                    <div className="col-sm-2 add-problem-name">
                        <h3>Name</h3>
                    </div>
                    <div className="col-sm-10">
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
                    {this.state.description !== '' && divdraftToHtml(convertToRaw(this.state.description.getCurrentContent()))}
                </div>
            </div>
        )
    }
}

export default AddProblem;