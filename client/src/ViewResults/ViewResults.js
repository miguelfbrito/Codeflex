import React, { Component } from 'react';
import './ViewResults.css'
import { Router, Redirect } from 'react-router';
import { textToLowerCaseNoSpaces, splitUrl } from '../commons/Utils';
import { URL } from '../commons/Constants';
import PathLink from '../PathLink/PathLink';

class ViewResults extends Component {

    constructor(props) {
        super(props);
        this.state = {
            results: []
        }
    }

    componentWillMount() {
        // TODO : mudar para a App
        if (!('submissionId' in this.props)) {
            console.log("Doesn't exist");
        }
    }
    componentDidMount() {

        let pathname = splitUrl(this.props.location.pathname)[2];

        console.log('Fetching view-results');
        console.log(pathname);
        /*fetch(URL + ':8080/api/database/Scoring/viewBySubmissionId/' + this.state.sentSubmission.submission.id).then(res => res.json())
            .then(data => {
                console.log(data);
                console.log(data.length);
            });*/
    }

    render() {



        let finalRender = '';
        let scoringResults = this.props.location.state.information;

        if (typeof scoringResults !== undefined) {
            console.log('Logging scoring results on vr')
            console.log(scoringResults);
            finalRender = <div className="testcase-container">
                {scoringResults.map((s, index) => (
                    <div className="col-sm-4 testcase">
                        <div className="testcase-icons">
                            {s.isRight === 1 ? <i className="material-icons green-icon">check_circle</i> :
                                s.isRight === 0 ? <i className="material-icons red-icon">highlight_off</i> :
                                    <i className="material-icons">error</i>}
                        </div>
                        <p>Test Case {index + 1}</p>
                    </div>
                ))}
            </div>
        }


        return (
            <div className="container">
                <div className="row ">
                    <PathLink path={this.props.location.pathname} title="View Results" />
                    {finalRender}
                </div>
            </div>
        );
    }
}

export default ViewResults;