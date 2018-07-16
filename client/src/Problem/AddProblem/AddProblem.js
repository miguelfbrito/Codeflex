import React from 'react';
import PathLink from '../../PathLink/PathLink'
import { Editor } from 'react-draft-wysiwyg';
import { Link } from 'react-router-dom';
import { EditorState, convertToRaw, convertFromRaw } from 'draft-js';
import { ToastContainer, toast } from 'react-toastify';
import { URL } from '../../commons/Constants';
import { splitUrl, getAuthorization, parseLocalJwt, isContentManager } from '../../commons/Utils';
import { validateSaveProblem } from './DataValidation';

import 'react-toastify/dist/ReactToastify.css';
import '../../../node_modules/react-draft-wysiwyg/dist/react-draft-wysiwyg.css'
import '../../commons/draft-editor.css'
import './AddProblem.css'
import PageNotFound from '../../PageNotFound/PageNotFound';

class AddProblem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            invalidData: true,
            userIsOwner: true,
            problemId: '',
            problemName: '',
            problemMaxScore: 100,
            problemDescription: EditorState.createEmpty(),
            problemConstraints: EditorState.createEmpty(),
            problemInputFormat: EditorState.createEmpty(),
            problemOutputFormat: EditorState.createEmpty(),
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
        if (!isContentManager()) {
            this.isUserTournamentOwner();
        }

        fetch(URL + '/api/database/difficulty/view', {
            headers: { ...getAuthorization() }
        })
            .then(res => res.json()).then(data => { console.log(data); this.setState({ displayDifficulties: data }) })

        fetch(URL + '/api/database/PractiseCategory/view', {
            headers: { ...getAuthorization() }
        })
            .then(res => res.json()).then(data => { console.log(data); this.setState({ displayCategories: data }) })

        const path = splitUrl(this.props.location.pathname);

        if (this.isEditing()) {
            this.fetchCurrentProblem();
        }
    }

    isUserTournamentOwner = () => {
        fetch(URL + '/api/database/tournament/isUserTournamentOwner/' + this.props.match.params + "/" + parseLocalJwt().username, {
            headers: new Headers({ ...getAuthorization() })
        }).then(res => { if (res.status === 200) { this.setState({ userIsOwner: true }); } else { this.setState({ userIsOwner: false }); } })
    }

    fetchCurrentProblem() {
        const problemName = this.props.match.params.problemName;
        fetch(URL + '/api/database/Problem/viewAllDetails/' + problemName, {
            headers: { ...getAuthorization() }
        }).then(res => res.json()).then(data => {
            console.log('getting stuff')
            console.log(data);

            let saveData = '';

            try {

                saveData = {
                    problemId: data.problem.id,
                    problemName: data.problem.name,
                    problemDescription: EditorState.createWithContent(convertFromRaw(JSON.parse(data.problem.description))),
                    problemConstraints: EditorState.createWithContent(convertFromRaw(JSON.parse(data.problem.constraints))),
                    problemInputFormat: EditorState.createWithContent(convertFromRaw(JSON.parse(data.problem.inputFormat))),
                    problemOutputFormat: EditorState.createWithContent(convertFromRaw(JSON.parse(data.problem.outputFormat))),
                    problemMaxScore: data.problem.maxScore,
                }
            } catch (err) {
            }

            if (data.category) {
                saveData = {
                    ...saveData,
                    category: {
                        id: data.category.id,
                        name: data.category.id
                    }
                }
            }

            if (data.problem.difficulty) {
                saveData = {
                    ...saveData,
                    difficulty: {
                        id: data.problem.difficulty.id,
                        name: data.problem.difficulty.name
                    }
                }
            }

            // TODO : check category might be null
            this.setState({
                ...saveData
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
                description: JSON.stringify(convertToRaw(this.state.problemDescription.getCurrentContent())),
                constraints: JSON.stringify(convertToRaw(this.state.problemConstraints.getCurrentContent())),
                inputFormat: JSON.stringify(convertToRaw(this.state.problemInputFormat.getCurrentContent())),
                outputFormat: JSON.stringify(convertToRaw(this.state.problemOutputFormat.getCurrentContent())),
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
            window.location.href = this.getRedirectDestinationOnSave();
            console.log(data);
        })
    }

    isEditing = () => {
        const path = splitUrl(this.props.location.pathname);
        if (path[3] === 'edit' || path[2] === 'edit' || (typeof path[4] != "undefined" && path[4] === 'edit')) {
            return true;
        }
        return false;
    }


    saveProblem() {

        let url = splitUrl(this.props.location.pathname)

        if (!validateSaveProblem(this.state)) {
            return;
        }



        if (this.isEditing()) {
            this.updateProblem();
            return;
        }

        const data = {
            problem: {
                name: this.state.problemName,
                maxScore: Number(this.state.problemMaxScore),
                description: JSON.stringify(convertToRaw(this.state.problemDescription.getCurrentContent())),
                constraints: JSON.stringify(convertToRaw(this.state.problemConstraints.getCurrentContent())),
                inputFormat: JSON.stringify(convertToRaw(this.state.problemInputFormat.getCurrentContent())),
                outputFormat: JSON.stringify(convertToRaw(this.state.problemOutputFormat.getCurrentContent())),
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
        }).then(res => {
            if (res.status === 500) {
                toast.error("There is already a problem named " + data.problem.name, { autoClose: 2500 })
            } else {
                return res.json();
            }
        }).then(data => {
            window.location.href = this.getRedirectDestinationOnSave();
        })
    }

    getRedirectDestinationOnSave = () => {
        let url = splitUrl(this.props.location.pathname);

        if (url[0] === 'compete') {
            return "/compete/manage-tournaments/" + this.props.match.params.tournamentName;
        } else if (url[0] === 'manage') {
            if (url[1] === 'problems') {
                return "/manage/problems";
            } else if (url[1] === 'tournaments') {
                return "/manage/tournaments/" + this.props.match.params.tournamentName;
            }
        }
    }



    render() {

        if (!this.state.userIsOwner) {
            return (
                <PageNotFound />
            )
        }

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
                        <small className="fill-info"> &nbsp; Length between 5 and 50 characters</small>
                    </div>
                </div>

                {splitUrl(this.props.location.pathname)[0] === 'manage' && splitUrl(this.props.location.pathname)[1] !== 'tournaments' ? <div>{categorySection}</div> : ''}

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
                        <small className="fill-info"> &nbsp; Value between 10 and 250</small>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-offset-2 col-sm-10 col-xs-12">
                        <input type="button" className="btn btn-codeflex" onClick={this.saveProblem} name="" id="save-problem" value="Save problem" />
                    </div>
                </div>


                <ToastContainer
                    position="top-right"
                    autoClose={5500}
                    hideProgressBar={false}
                    closeOnClick
                    rtl={false}
                    pauseOnVisibilityChange
                    draggable
                    pauseOnHover
                    style={{ fontFamily: "'Roboto', sans-serif", fontSize: '12pt', letterSpacing: '1px', textAlign: 'center' }}
                />

            </div>
        )
    }
}

export default AddProblem;