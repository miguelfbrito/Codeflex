import React from 'react';
import PathLink from '../../PathLink/PathLink'
import { Editor } from 'react-draft-wysiwyg';
import draftToHtml from 'draftjs-to-html';
import { Link } from 'react-router-dom';
import htmlToDraft from 'html-to-draftjs';
import { stateFromHTML } from 'draft-js-import-html';
import { EditorState, convertToRaw, ContentState } from 'draft-js';

import { URL } from '../../commons/Constants';
import { splitUrl, getAuthorization, parseLocalJwt } from '../../commons/Utils';
import '../../../node_modules/react-draft-wysiwyg/dist/react-draft-wysiwyg.css'
import '../../commons/draft-editor.css'
import './AddProblem.css'

class AddProblem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            problemId: '',
            problemName: '',
            problemMaxScore: '',
            problemDescription: '',
            problemConstraints: '',
            problemInputFormat: '',
            problemOutputFormat: '',
            difficulty: {
                id: '', name: ''
            },
            category: {
                id: '', name: ''
            },
            displayDifficulties: [],
            displayCategories: [],
        }

        this.onTextBoxChange = this.onTextBoxChange.bind(this);
        this.onDescriptionChange = this.onDescriptionChange.bind(this);
        this.onConstraintChange = this.onConstraintChange.bind(this);
        this.onInputFormatChange = this.onInputFormatChange.bind(this);
        this.onOutputFormatChange = this.onOutputFormatChange.bind(this);
        this.handleSelectBoxChange = this.handleSelectBoxChange.bind(this);
        this.saveProblem = this.saveProblem.bind(this);
        this.fetchCurrentProblem = this.fetchCurrentProblem.bind(this);
        this.updateProblem = this.updateProblem.bind(this);
    }

    componentDidMount() {
        fetch(URL + '/api/database/difficulty/view', {
            headers: { ...getAuthorization() }
        })
            .then(res => res.json()).then(data => { console.log(data); this.setState({ displayDifficulties: data }) })

        fetch(URL + '/api/database/PractiseCategory/view', {
            headers: { ...getAuthorization() }
        })
            .then(res => res.json()).then(data => { console.log(data); this.setState({ displayCategories: data }) })

        const path = splitUrl(this.props.location.pathname);

        if (path[3] === 'edit' || path[2] === 'edit') {
            this.fetchCurrentProblem();
        }
    }

    fetchCurrentProblem() {
        const problemName = this.props.match.params.problemName;
        fetch(URL + '/api/database/Problem/viewAllDetails/' + problemName, {
            headers: { ...getAuthorization() }
        }).then(res => res.json()).then(data => {
            console.log('getting stuff')
            console.log(data);

            // TODO : check category might be null
            this.setState({
                problemId: data.problem.id,
                problemName: data.problem.name,
                problemDescription: EditorState.createWithContent(stateFromHTML(data.problem.description)),
                problemConstraints: EditorState.createWithContent(stateFromHTML(data.problem.constraints)),
                problemInputFormat: EditorState.createWithContent(stateFromHTML(data.problem.inputFormat)),
                problemOutputFormat: EditorState.createWithContent(stateFromHTML(data.problem.outputFormat)),
                problemCreationDate: EditorState.createWithContent(stateFromHTML(data.problem.creationDate)),
                problemMaxScore: data.problem.maxScore,
                difficulty: {
                    id: data.problem.difficulty.id,
                    name: data.problem.difficulty.name
                },
                category: {
                    id: data.category.id,
                    name: data.category.id
                }
            })
        })
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

    onTextBoxChange(e) {
        console.log(e.target.value)
        this.setState({ [e.target.name]: e.target.value });
    }

    // couldn't figure out how to get the target for the editors. This will have to do. ( maybe refs)
    onDescriptionChange(change) {
        console.log("Problem")
        this.setState({ problemDescription: change });
    }
    onConstraintChange(change) {
        console.log("Constraint")
        this.setState({ problemConstraints: change });
    }
    onInputFormatChange(change) {
        console.log("Input")
        this.setState({ problemInputFormat: change });
    }
    onOutputFormatChange(change) {
        console.log(change);
        this.setState({ problemOutputFormat: change });
    }

    handleSelectBoxChange(e) {
        let selectedItem = [...e.target.options].filter(o => o.selected)[0]; //
        console.log(selectedItem.id);
        this.setState({ [e.target.name]: { id: selectedItem.value, name: selectedItem.id } });
    }

    updateProblem() {

        const data = {
            problem: {
                id: this.state.problemId,
                name: this.state.problemName,
                maxScore: parseInt(this.state.problemMaxScore),
                description: draftToHtml(convertToRaw(this.state.problemDescription.getCurrentContent())),
                constraints: draftToHtml(convertToRaw(this.state.problemConstraints.getCurrentContent())),
                inputFormat: draftToHtml(convertToRaw(this.state.problemInputFormat.getCurrentContent())),
                outputFormat: draftToHtml(convertToRaw(this.state.problemOutputFormat.getCurrentContent())),
                difficulty: {
                    id: this.state.difficulty.id,
                    name: this.state.difficulty.name
                }
            },
            category: {
                id: this.state.category.id,
                name: this.state.category.name
            },
            owner: {
                username: parseLocalJwt().username
            },
            tournament: {
                name: this.props.match.params.tournamentName
            }
        }

        console.log(data)

        fetch(URL + '/api/database/Problem/update', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log("PROBLEM UPDATED")
            console.log(data);
        })
    }

    saveProblem() {

        let url = splitUrl(this.props.location.pathname)
        if (url[3] === 'edit' || url[2] === 'edit') {
            this.updateProblem();
            return;
        }

        const data = {
            problem: {
                name: this.state.problemName,
                maxScore: Number(this.state.problemMaxScore),
                description: draftToHtml(convertToRaw(this.state.problemDescription.getCurrentContent())),
                constraints: draftToHtml(convertToRaw(this.state.problemConstraints.getCurrentContent())),
                inputFormat: draftToHtml(convertToRaw(this.state.problemInputFormat.getCurrentContent())),
                outputFormat: draftToHtml(convertToRaw(this.state.problemOutputFormat.getCurrentContent())),
                creationDate: new Date().getTime(),
            },
            difficulty: {
                id: this.state.difficulty.id,
                name: this.state.difficulty.name
            },
            category: {
                id: this.state.category.id,
                name: this.state.category.name
            },
            owner: {
                username: parseLocalJwt().username 
            },
            tournament: {
                name: this.props.match.params.tournamentName
            }
        }

        console.log(data);
        fetch(URL + '/api/database/Problem/add', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log(data.error);
            console.log(data.message);
            //  console.log(data.message.split("propertyPath=")[1].split(',')[0]);
            console.log(data);
            // TODO : notify the user about invalid data
        })
    }



    render() {

        const categorySection =
            <div className="row info-section">
                <div className="col-sm-2 add-problem-desc">
                    <h3>Category</h3>
                    <p>Select the category that best represents the problem.</p>
                </div>
                <div className="col-sm-10 add-problem-textarea">
                    <select name="category" className="textbox-problem" placeholder="Category" onChange={this.handleSelectBoxChange}>
                        <option value="" disabled selected>Select category</option>
                        {this.state.displayCategories.map((d, index) => (
                            <option key={d.id} value={d.id}
                                selected={d.id === this.state.category.id ? 'selected' : ''}
                                id={d.name}>{d.name}</option>
                        ))}
                    </select>
                </div>
            </div>

        return (
            <div className="container add-problem">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Add problem" />
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Name</h3>
                        <p>Add a name to your problem.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <input name="problemName" className="textbox-problem" onChange={this.onTextBoxChange} value={this.state.problemName} type="text" id="input-problem-name" placeholder="Problem name" />
                    </div>
                </div>

                {splitUrl(this.props.location.pathname)[0] === 'manage' ? <div>{categorySection}</div> : ''}

                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Difficulty</h3>
                        <p>Select the difficulty of the problem.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <select name="difficulty" className="textbox-problem" placeholder="Difficulty" onChange={this.handleSelectBoxChange}>
                            <option value="" disabled selected>Select difficulty</option>
                            {this.state.displayDifficulties.map((d, index) => (
                                <option key={d.id} value={d.id} selected={d.id === this.state.difficulty.id ? 'selected' : ''
                                } id={d.name}>{d.name}</option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Problem Statement</h3>
                        <p>Explain your problem here. Keep it simple and clear.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <div className="text-editor-wrapper">
                            <Editor
                                wrapperClassName="demo-wrapper "
                                editorClassName="demo-editor"
                                editorState={this.state.problemDescription}
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
                                editorState={this.state.problemConstraints}
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
                                editorState={this.state.problemInputFormat}
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
                                editorState={this.state.problemOutputFormat}
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
                <div className="row info-section">
                    <div className="col-sm-2 add-problem-desc">
                        <h3>Max score</h3>
                        <p>Max score of the problem. It will be divided equally by all the test cases that aren't shown to the user.</p>
                    </div>
                    <div className="col-sm-10 add-problem-textarea">
                        <input name="problemMaxScore" className="textbox-problem" onChange={this.onTextBoxChange} value={this.state.problemMaxScore} type="text" placeholder="Max score (e.g. 100)" />
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-offset-2 col-sm-10 col-xs-12">
                        {/*  <Link to={"/compete/manage-tournaments/" + splitUrl(this.props.location.pathname)[2]}>*/}
                        <input type="button" className="btn btn-codeflex" onClick={this.saveProblem} name="" id="save-problem" value="Save problem" />
                        {/*</Link>*/}
                    </div>
                </div>
            </div>
        )
    }
}

export default AddProblem;