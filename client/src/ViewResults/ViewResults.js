import React, { Component } from 'react';
import AceEditor from 'react-ace';
import PathLink from '../PathLink/PathLink';

import { Router, Redirect } from 'react-router';
import { textToLowerCaseNoSpaces, splitUrl } from '../commons/Utils';
import { URL } from '../commons/Constants';

import './ViewResults.css'



class ViewResults extends Component {

    constructor(props) {
        super(props);
        this.state = {
            results: []
        }
        this.fetchScoringResults = this.fetchScoringResults.bind(this);
    }

    componentDidMount() {
        // TODO : mudar para a App
        /*if (!('submissionId' in this.props)) {
            console.log("Doesn't exist");
        }   */

        if (typeof this.props.location.state.information !== "undefined") {
            this.setState({ results: this.props.location.state.information });
            console.log('HI');
        } else {
            console.log('CALLING FETCH RESULTS')
            this.fetchScoringResults();
        }
    }

    fetchScoringResults() {
        let pathname = splitUrl(this.props.location.pathname)[2];

        console.log('Fetching view-results');
        console.log(pathname);
        fetch(URL + '/api/database/Scoring/viewBySubmissionId/' + this.props.location.state.submissionId).then(res => res.json())
            .then(data => {
                console.log('VIEW RESULTS')
                console.log(data);
                console.log(data.length);
                this.setState({ results: data })
            });

    }

    render() {

        const aceStyle = {
            minHeight: '600px',
            marginTop: '15px',
            paddingTop: '5px',
            border: '1px solid #ccc',
            width: '100%',
            boxShadow: '0px 3px 8px 0px #ccc',
            marginLeft: '0'
        }

        let renderTestCases = '';
        let renderCode = '';

        if (typeof this.state.results !== "undefined") {
            console.log('Logging scoring results on vr')
            renderTestCases =
                <div className="testcase-container">
                    {this.state.results.map((s, index) => (
                        <div className="col-sm-4">
                            <div className="col-sm-11 testcase">
                                <div className="testcase-icons">
                                    {s.isRight === 1 ? <i className="material-icons green-icon">check_circle_outline</i> :
                                        s.isRight === 0 ? <i className="material-icons red-icon">highlight_off</i> :
                                            <i className="material-icons red-icon">error</i>}
                                </div>
                                <p>Test Case {index + 1}</p>
                            </div>
                        </div>
                    ))}
                </div>

            if (typeof this.state.results[0] !== "undefined") {
                renderCode = <div>
                    <h3>Code submitted</h3>
                    <AceEditor style={aceStyle} mode={this.state.results[0].submissions.language.mode} theme={this.state.theme} name=""
                        fontSize={14} showPrintMargin={true} showGutter={true} highlightActiveLine={true} value={atob(this.state.results[0].submissions.code)}
                        setOptions={{
                            enableBasicAutocompletion: true,
                            enableLiveAutocompletion: true,
                            enableSnippets: false,
                            showLineNumbers: true,
                            tabSize: 3,
                        }} />
                </div>
            }
        }

        console.log(renderCode);
        return (
            <div className="container">
                <div className="row ">
                    <PathLink path={this.props.location.pathname} title="View Results" />
                    {renderTestCases}
                </div>
                <div>
                    {renderCode}
                </div>
            </div>
        );
    }
}

export default ViewResults;