import React from 'react';
import PathLink from '../PathLink/PathLink'
import Popup from '../Popup/Popup';
import LoadFiles from '../LoadFiles/LoadFiles';

import { URL } from '../commons/Constants';
import { splitUrl, getAuthorization, parseLocalJwt, getRndInteger, readFile, isContentManager } from '../commons/Utils';

import '../../node_modules/react-table/react-table.css'
import './ManageTestCases.css';
import PageNotFound from '../PageNotFound/PageNotFound';



/*

Should probably change all the input/output/description edit mess to REFs for simplicity


*/

class ManageTestCases extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userIsOwner: true,
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
        if (!isContentManager()) {
            this.isUserTournamentOwner();
        }
        this.fetchTestCases();
    }

    isUserTournamentOwner = () => {
        fetch(URL + '/api/database/tournament/isUserTournamentOwner/' + this.props.match.params + "/" + parseLocalJwt().username, {
            headers: new Headers({ ...getAuthorization() })
        }).then(res => { if (res.status === 200) { this.setState({ userIsOwner: true }); } else { this.setState({ userIsOwner: false }); } })
    }

    onClick(text, mode, title, testCase) {
        this.setState({ currentText: text, modalTitle: title, currentTestCase: testCase, currentMode: mode });
        this.modalEdit.current.openModal();
    }

    onClickAdd() {
        this.modalAdd.current.openModal();
    }

    fetchTestCases() {
        fetch(URL + '/api/database/Problem/viewAllTestCasesByProblemName/' + this.props.match.params.problemName, {
            headers: { ...getAuthorization() }
        }).then(res => res.json())
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
                ...getAuthorization(),
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
                newArray = this.state.testCases;
                break;
        }

        this.setState({ testCases: newArray, currentMode : ''})
        this.persistChangesOnDatabase();
    }

    onChange(e) {
        this.setState({ currentText: e.target.value });
    }


    onChangeNew(e) {
        this.setState({ [e.target.name]: e.target.value });

    }

    deleteTestCase(index) {

        fetch(URL + '/api/database/TestCases/delete/' + index, {
            method: 'POST',
            headers: { ...getAuthorization() }
        }).then(() => {
            this.fetchTestCases();
        });
    }

    onNewTestCaseModalClose() {
        if (this.state.currentMode !== 'bulk') {

            const data = {
                input: this.newInput.current.value.trim(),
                output: this.newOutput.current.value.trim(),
                description: this.newDescription.current.value.trim()
            }

            this.addTestCase(data);
        }

        this.setState({currentMode : ''})
    }

    changeToBulk = () => {
        this.setState({ currentMode: 'bulk' })
    }

    addTestCase = (data) => {
        fetch(URL + '/api/database/TestCases/addToProblem/' + this.props.match.params.problemName, {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(() => {
            this.fetchTestCases();
        });
    }

    toggleCheckBox = (e, t) => {

        console.log("OUTPUT ");

        let newTestCases = this.state.testCases.map((tc, i) => {
            if (tc === t) {
                console.log(tc.shown);
                tc.shown = !tc.shown;
            }
            return tc;
        });

        console.log("NEW TEST CASES")
        console.log(newTestCases);

        this.setState({ testCases: newTestCases });
        this.persistChangesOnDatabase();

    }

    addTestCasesFromFiles = (files) => {
        console.log('Saving files from manage test cases');

        if (files.length > 0) {

            console.log('SORTING')
            files = files.sort((a, b) => { return a.name.toLowerCase().localeCompare(b.name.toLowerCase()) });
            console.log(files);
            for (let i = 0; i < files.length; i++) {
                let current = files[i];
                let next = (i + 1 >= files.length) ? files[i] : files[i + 1];

                let splitCurrent = current.name.split("_");
                let splitNext = next.name.split("_");


                let input = '';
                let output = '';

                // if same test case
                if (splitCurrent[0].trim() === splitNext[0].trim()) {
                    input = readFile(current).trim();
                    output = readFile(next).trim();
                    i++;

                } else {
                    if (splitCurrent[1].trim() === 'input.txt') {
                        input = readFile(current).trim();
                    } else if (splitCurrent[1].trim() === 'output.txt') {
                        output = readFile(current).trim();
                    }
                }

                console.log("Input " + input);
                console.log("Output" + output);
                console.log('\n');

                const data = {
                    input: input,
                    output: output,
                    description: ''
                }

                this.addTestCase(data);
            }

            this.modalAdd.current.closeModal();
            this.setState({currentMode : ''})
        }


    }

    render() {

        if (!this.state.userIsOwner) {
            return (
                <PageNotFound />
            )
        }

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

            this.state.currentMode === 'bulk' ?
                <LoadFiles addTestCasesFromFiles={this.addTestCasesFromFiles} />
                :
                <div className="tc-popup">
                    <h2 style={{ color: 'black', margin: 'auto' }}>Add new </h2>
                    <input type="button" id="add-in-bulk" className="btn" style={{ position: 'absolute', right: '25px', top: '25px' }} value="Add in bulk" onClick={this.changeToBulk} />
                    <div className="row">
                        <div className="col-sm-6">
                            <p>Input</p>
                            <textarea ref={this.newInput} name="inputNew" id="" style={{ border: '1px solid #6a44ff' }} className="modal-text-area-section"
                            ></textarea>
                        </div>
                        <div className="col-sm-6">
                            <p>Output</p>
                            <textarea ref={this.newOutput} name="outputNew" id="" style={{ border: '1px solid #6a44ff' }} className="modal-text-area-section"
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

                    {this.state.testCases.sort((a, b) => a.id - b.id).map((t, i) => (
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
                                <input type="checkbox" name="shown" checked={t.shown ? "checked" : ''} onClick={(e) => this.toggleCheckBox(e, t)} />
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