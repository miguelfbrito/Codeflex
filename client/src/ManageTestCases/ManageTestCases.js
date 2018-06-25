import React from 'react';
import PathLink from '../PathLink/PathLink'
import ReactTable from 'react-table';
import { Redirect, Link } from 'react-router-dom';
import Popup from '../Popup/Popup';

import { URL } from '../commons/Constants';
import { splitUrl } from '../commons/Utils';

import '../../node_modules/react-table/react-table.css'
import './ManageTestCases.css';



/*

Should probably change all the input/output/description edit mess to REFs for simplicity


*/

class ManageTestCases extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            testCases: [],
            input: '',
            popupInfo: [],
            currentTestCase: 0,
            currentMode: '',
            modalTitle: '',
            addingTestCase: false,
            inputNew: '',
            outputNew: '',
            descriptionNew: '',
            showNew: false
        }

        this.modalEdit = React.createRef();
        this.modalAdd = React.createRef();

        this.newInput = React.createRef();
        this.newOutput = React.createRef();
        this.newDescription = React.createRef();

        this.onChange = this.onChange.bind(this);
        this.onClick = this.onClick.bind(this);
        this.onClickAdd = this.onClickAdd.bind(this);
        this.onModalClose = this.onModalClose.bind(this);
        this.onNewTestCaseModalClose = this.onNewTestCaseModalClose.bind(this);

        this.fetchTestCases = this.fetchTestCases.bind(this);
    }

    componentDidMount() {
        this.fetchTestCases();
    }

    onClick(text, mode, title, testCase) {
        this.setState({ currentText: text, modalTitle: title, currentTestCase: testCase, currentMode: mode });
        this.modalEdit.current.openModal();
    }

    onClickAdd() {
        this.modalAdd.current.openModal();
    }

    fetchTestCases() {
        fetch(URL + '/api/database/Problem/viewAllTestCasesByProblemName/' + this.props.match.params.problemName).then(res => res.json())
            .then(data => {
                console.log(data);
                this.setState({ testCases: data });
            })
    }

    persistChangesOnDatabase() {
        fetch(URL + '/api/database/TestCases/updateList', {
            method: 'POST',
            body: JSON.stringify(this.state.testCases),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        })
    }

    onModalClose() {

        let newTestCases = this.state.testCases;
        let mode = this.state.currentMode;
        let newArray = null;
        switch (mode) {
            case 'input':
                newArray = newTestCases.map(t => {
                    if (t === this.state.currentTestCase) {
                        t.input = this.state.currentText;
                    }
                    return t;
                })
                break;
            case 'output':
                newArray = newTestCases.map(t => {
                    if (t === this.state.currentTestCase) {
                        t.output = this.state.currentText;
                    }
                    return t;
                })
                break;
            case 'description':
                newArray = newTestCases.map(t => {
                    if (t === this.state.currentTestCase) {
                        t.description = this.state.currentText;
                    }
                    return t;
                })
                break;
            default:
                break;
        }

        this.setState({ testCases: newArray })
        this.persistChangesOnDatabase();
    }

    onChange(e) {
        this.setState({ currentText: e.target.value });
    }


    onChangeNew(e) {
        this.setState({[e.target.name] : e.target.value});

    }

    deleteTestCase(index) {

        fetch(URL + '/api/database/TestCases/delete/' + index, {
            method: 'DELETE'
        }).then(() => {
            this.fetchTestCases();
        });
    }

    onNewTestCaseModalClose() {
        const data = {
            input: this.newInput.current.value.trim(),
            output: this.newOutput.current.value.trim(),
            description: this.newDescription.current.value.trim()
        }
        fetch(URL + '/api/database/TestCases/addToProblem/' + this.props.match.params.problemName, {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(() => {
            this.fetchTestCases();
        });
    }

    render() {

        const PopupInformation = () => (
            <div className="tc-popup">
                <h2 style={{ color: 'black', margin: 'auto' }}>{this.state.modalTitle}</h2>
                <textarea autoFocus name="input" id="" style={{ border: '1px solid #6a44ff' }} className="modal-text-area"
                    value={this.state.currentText}
                    placeholder={this.state.currentMode === 'description' ? 'If you select to show this test case, this description will show on the Problem page on top of the input and the output' : ''}
                    onChange={this.onChange}></textarea>
            </div>
        );

        const PopupAddTestCase = () => (
            <div className="tc-popup">
                <h2 style={{ color: 'black', margin: 'auto' }}>Add new test case</h2>
                <div className="row">
                    <div className="col-sm-6">
                        <p>Input</p>
                        <textarea ref={this.newInput} name="inputNew" id="" style={{ border: '1px solid #6a44ff' }} className="modal-text-area-section"
                            placeholder={this.state.currentMode === 'description' ? 'If you select to show this test case, this description will show on the Problem page on top of the input and the output' : ''}
                        ></textarea>
                    </div>
                    <div className="col-sm-6">
                        <p>Output</p>
                        <textarea ref={this.newOutput} name="outputNew" id="" style={{ border: '1px solid #6a44ff' }} className="modal-text-area-section"
                            placeholder={this.state.currentMode === 'description' ? 'If you select to show this test case, this description will show on the Problem page on top of the input and the output' : ''}
                        ></textarea>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12">
                        <p>Description</p>
                        <textarea ref={this.newDescription} name="descriptionNew" id="" style={{ border: '1px solid #6a44ff', height: '100px', width: '100%' }} className="modal-text-area-section"
                            placeholder={this.state.currentMode === 'description' ? 'If you select to show this test case, this description will show on the Problem page on top of the input and the output' : ''}
                        ></textarea>
                    </div>
                </div>
            </div>
        );

        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Test Cases" />
                    <h3 className="page-subtitle">Make sure the test cases you insert cover the problem fully.</h3>
                    <p className="page-subtitle">Add new test cases or edit the current ones. To edit, click on the respective button, edit the data and the changes will be saved when leaving the window.</p>

                    <div className="col-sm-3 col-xs-12 test-case-wrapper tc add-test-case">
                        <i className="material-icons manage-tournament-icon" id="add-test-case" onClick={this.onClickAdd}>add_circle_outline</i>
                    </div>

                    {this.state.testCases.map((t, i) => (
                        <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                            <div >
                                <h3 style={{ display: 'inline-block' }}>Test Case {i + 1}</h3>
                                <i style={{ position: 'absolute', top: '20px', right: '15px' }} className="material-icons" onClick={() => this.deleteTestCase(t.id)}>delete</i>
                            </div>
                            <hr style={{ borderBottom: 'none', borderLeft: 'none', borderRight: 'none' }} />
                            <input className="btn btn-tc" type="button" value="Input" onClick={() => this.onClick(t.input, 'input', "Add input", t)} />
                            <input className="btn btn-tc" type="button" value="Output" onClick={() => this.onClick(t.output, 'output', "Add output", t)} />
                            <input className="btn btn-tc" type="button" value="Description" onClick={() => this.onClick(t.description, 'description', "Add description", t)} />
                            <label className="container inline-field">Show
                                <input type="checkbox" checked={t.shown ? "checked" : ''} />
                                <span className="checkmark"></span>
                            </label>
                        </div>
                    ))}

                    <Popup ref={this.modalAdd} onModalClose={this.onNewTestCaseModalClose} >
                        <PopupAddTestCase />
                    </Popup>

                    <Popup ref={this.modalEdit} onModalClose={this.onModalClose}>
                        <PopupInformation />
                    </Popup>
                </div>
            </div>
        )
    }
}

export default ManageTestCases;