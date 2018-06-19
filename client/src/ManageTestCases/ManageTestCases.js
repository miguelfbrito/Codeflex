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
            currentText: '',
            modalTitle : ''
        }
        this.child = React.createRef();
        this.onChange = this.onChange.bind(this);
        this.onClick = this.onClick.bind(this);
    }

    componentDidMount() {
        const problemName = splitUrl(this.props.location.pathname)[3]
        fetch(URL + '/api/database/Problem/viewAllTestCasesByProblemName/' + problemName).then(res => res.json())
            .then(data => {
                console.log(data);
                this.setState({ testCases: data });
            })
    }

    onClick(text, title) {
        this.setState({ currentText: text, modalTitle : title});
        this.child.current.openModal();
    }

    onChange(e) {
        console.log(e.target.value + " " + e.target.name);
        this.setState({ currentText: e.target.value });
    }

    render() {

        const PopupInformation = () => (
            <div className="tc-popup">
                <h2 style={{ color: 'black', margin: 'auto' }}>{this.state.modalTitle}</h2>
                <textarea autoFocus name="input" id="" style={{ border: '1px solid #6a44ff' }} cols="100" rows="25" value={this.state.currentText} onChange={this.onChange}></textarea>
            </div>
        );

        const addTestCase =
            <div>
                <div className="col-sm-3 col-xs-12 test-case-wrapper">
                    <h3>New test case</h3>
                    <hr style={{ borderBottom: 'none', borderLeft: 'none', borderRight: 'none' }} />
                    <div>
                        <input onClick={() => this.onClick("hi")} type="button" />
                        <p>Insert Input</p>
                        <p>Insert Output</p>
                        <p>Insert Description</p>
                        <input type="checkbox" /> Shown
                    </div>
                    <Popup ref={this.child}>
                        <PopupInformation />
                    </Popup>
                </div>
            </div>

        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Test Cases" />
                    <h3 style={{ color: '#aaa' }}>Make sure the test cases you insert cover the problem fully.</h3>
                    {this.state.testCases.map((t, i) => (
                        <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                            <h3>Test Case {i + 1}</h3>
                            <hr style={{ borderBottom: 'none', borderLeft: 'none', borderRight: 'none' }} />
                            <input className="btn btn-tc" type="button" value="Input" onClick={() => this.onClick(t.input, "Add input")} />
                            <input className="btn btn-tc" type="button" value="Output" onClick={() => this.onClick(t.output, "Add output")} />
                            <input className="btn btn-tc" type="button" value="Description" onClick={() => this.onClick(t.description, "Add description")}/>
                            <label className="container inline-field">Shown
                                <input type="checkbox" />
                                <span className="checkmark"></span>
                            </label>
                        </div>
                    ))}
                    {addTestCase}
                </div>
            </div>
        )
    }
}

export default ManageTestCases;