import React from 'react';
import PathLink from '../PathLink/PathLink'
import ReactTable from 'react-table';
import { Redirect, Link } from 'react-router-dom';
import Popup from '../Popup/Popup';

import { URL } from '../commons/Constants';
import { splitUrl } from '../commons/Utils';

import '../../node_modules/react-table/react-table.css'
import './ManageTestCases.css';

class ManageTestCases extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            testCases: [],
            input: '',
            popupInfo: [],
            currentTestCase: 0,
            currentMode: '',
            modalTitle: ''
        }
        this.child = React.createRef();
        this.onChange = this.onChange.bind(this);
        this.onClick = this.onClick.bind(this);
        this.onModalClose = this.onModalClose.bind(this);
    }

    componentDidMount() {
        const problemName = splitUrl(this.props.location.pathname)[3]
        fetch(URL + '/api/database/Problem/viewAllTestCasesByProblemName/' + problemName).then(res => res.json())
            .then(data => {
                console.log(data);
                this.setState({ testCases: data });
            })
    }

    onClick(text, mode, title, testCase) {
        this.setState({ currentText: text, modalTitle: title, currentTestCase: testCase, currentMode: mode });
        this.child.current.openModal();
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

    /*
        1. Que index estou a editar
        2. O que estou a editar (input/ output / description)
        3. 
    */

    render() {

        const PopupInformation = () => (
            <div className="tc-popup">
                <h2 style={{ color: 'black', margin: 'auto' }}>{this.state.modalTitle}</h2>
                <textarea autoFocus name="input" id="" style={{ border: '1px solid #6a44ff' }} cols="100" rows="25"
                    value={this.state.currentText}
                    placeholder={this.state.currentMode === 'description' ? 'If you select to show this test case, this description will show on the Problem page on top of the input and the output' : ''}
                    onChange={this.onChange}></textarea>
            </div>
        );
        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Test Cases" />
                    <h3 style={{ color: '#aaa' }}>Make sure the test cases you insert cover the problem fully.</h3>
                    <p style={{ color: '#aaa' }}>Add new test cases or edit the current ones. To edit, click on the respective button, edit the data and the changes will be saved when leaving the window.</p>

                    <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                        <i className="material-icons manage-tournament-icon" id="add-test-case">add_circle_outline</i>
                    </div>

                    {this.state.testCases.map((t, i) => (
                        <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                            <h3>Test Case {i + 1}</h3>
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
                    <Popup ref={this.child} onModalClose={this.onModalClose}>
                        <PopupInformation />
                    </Popup>
                </div>
            </div>
        )
    }
}

export default ManageTestCases;