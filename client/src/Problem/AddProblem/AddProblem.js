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
            problemName: '',
            difficulty: {
                id: '', name: ''
            },
            displayDifficulties: [],
            description: '',
            constraints: '',
            inputFormat: '',
            outputFormat: ''
        }

        this.onNameChange = this.onNameChange.bind(this);
        this.onDescriptionChange = this.onDescriptionChange.bind(this);
        this.onConstraintChange = this.onConstraintChange.bind(this);
        this.onInputFormatChange = this.onInputFormatChange.bind(this);
        this.onOutputFormatChange = this.onOutputFormatChange.bind(this);
        this.handleSelectBoxChange = this.handleSelectBoxChange.bind(this);
        this.saveProblem = this.saveProblem.bind(this);
    }

    componentDidMount() {
        fetch(URL + '/api/database/difficulty/view')
            .then(res => res.json()).then(data => { console.log(data); this.setState({ displayDifficulties: data }) })
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

    onNameChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    // couldn't figure out how to get the target for the editors. This will have to do.
    onDescriptionChange(change) {
        this.setState({ description: change });
    }
    onConstraintChange(change) {
        this.setState({ constraints: change });
    }
    onInputFormatChange(change) {
        this.setState({ inputFormat: change });
    }
    onOutputFormatChange(change) {
        this.setState({ outputFormat: change });
    }

    handleSelectBoxChange(e) {
        let selectedItem = [...e.target.options].filter(o => o.selected)[0]; //
        console.log(selectedItem.id);
        this.setState({ [e.target.name]: { id: selectedItem.value, name: selectedItem.id } });
    }

    saveProblem() {
        const data = {
            problem: {
                name: this.state.problemName,
                description: draftToHtml(convertToRaw(this.state.description.getCurrentContent())),
                constraints: draftToHtml(convertToRaw(this.state.constraints.getCurrentContent())),
                inputFormat: draftToHtml(convertToRaw(this.state.inputFormat.getCurrentContent())),
                outputFormat: draftToHtml(convertToRaw(this.state.outputFormat.getCurrentContent())),
                creationDate: new Date().getTime(),
            },
            difficulty: {
                id: this.state.difficulty.id,
                name: this.state.difficulty.name
            },
            owner: {
                id: JSON.parse(localStorage.getItem('userData')).id
            }, 
            tournament : {
                name : splitUrl(this.props.location.pathname)[2]
            }
        }

        console.log(data);
        fetch(URL + '/api/database/Problem/add', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {

            console.log(data);
            // TODO : notify the user about invalid data
        })
    }



    render() {
        return (
            <div className="container add-problem">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Add problem" />
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Name</h3>
                        <p>Add a name to your problem.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <input name="problemName" className="textbox-problem" onChange={this.onNameChange} value={this.state.problemName} type="text" id="input-problem-name" placeholder="Problem name" />
                    </div>
                    {this.state.description !== '' && draftToHtml(convertToRaw(this.state.description.getCurrentContent()))}
                </div>

                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Difficulty</h3>
                        <p>Select the difficulty of the problem.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <select name="difficulty" className="textbox-problem" placeholder="Difficulty" onChange={this.handleSelectBoxChange}>
                            {this.state.displayDifficulties.map((d, index) => (
                                <option key={d.id} value={d.id} selected={index == 0 ? "selected" : ''} id={d.name}>{d.name}</option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Problem Statement</h3>
                        <p>Explain your problem here. Keep it simple and clean.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <div className="text-editor-wrapper">
                            <Editor
                                wrapperClassName="demo-wrapper "
                                editorClassName="demo-editor"
                                editorState={this.state.description}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onDescriptionChange}
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
                                editorState={this.state.constraints}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onConstraintChange}
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
                                editorState={this.state.inputFormat}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onInputFormatChange}
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
                                editorState={this.state.outputFormat}
                                //    editorState={this.state.description}
                                onEditorStateChange={this.onOutputFormatChange}
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
                        <input type="button" className="btn btn-primary" onClick={this.saveProblem} name="" id="save-problem" value="Save problem" />
                    </div>
                </div>
            </div>
        )
    }
}

export default AddProblem;