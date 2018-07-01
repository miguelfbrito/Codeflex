import React, { Component } from 'react';
import Script from 'react-load-script';
import AceEditor from 'react-ace';
import brace from 'brace';
import Parser from 'html-react-parser';
import { Redirect, Link } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { splitUrl, textToLowerCaseNoSpaces, dateWithDay, getAuthorization, parseLocalJwt } from '../commons/Utils';
import Submissions from './Submissions/Submissions';
import Leaderboard from './Leaderboard/Leaderboard';
import CompilerError from './CompilerError/CompilerError';
import PathLink from '../PathLink/PathLink';
import $ from 'jquery';

import 'brace/mode/java';
import 'brace/mode/javascript';
import 'brace/mode/csharp';
import 'brace/mode/c_cpp';
import 'brace/mode/python';

import 'brace/theme/github';
import 'brace/theme/tomorrow';
import 'brace/theme/monokai';
import 'brace/theme/terminal';

import './Problem.css';

class Problem extends Component {

    constructor(props) {
        super(props);
        this.state = {
            mathJaxLoaded: false,
            problemLoaded: false,
            scriptLoaded: false,
            problem: [],
            sentSubmission: {
                submitting: false,
                waitingForResults: false,
                submission: [],
                scoringResults: []
            },
            page: { problem: true, submissions: false, leaderboard: false },
            results: {
                loaded: false,
                result: [],
                error: ''
            },
            displayLanguages: [],
            language: { mode: 'java', name: 'Java' },
            theme: 'github',
            code: `import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int scores[] = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = in.nextInt();
        }
        minimumDistances(n, scores);
    }

    static void minimumDistances(int n, int array[]) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < n-1; i++) {
            for(int j = i+1; j<n; j++) {
                if(array[i] == array[j]){
                    min = Math.min(min,  Math.abs(i-j));
                }
            }
        }
        if(min==Integer.MAX_VALUE) {
            min = -1;
        }
        System.out.println(min);
    }


}`
        }

        this.mathJaxRender = React.createRef();

        this.handleSelectBoxChange = this.handleSelectBoxChange.bind(this);
        this.onAceChange = this.onAceChange.bind(this);
        this.submitSubmission = this.submitSubmission.bind(this);
        this.fetchForResults = this.fetchForResults.bind(this);
        this.onPageClick = this.onPageClick.bind(this);
        this.handleScriptLoad = this.handleScriptLoad.bind(this);
    }

    componentDidMount() {
        let currentProblem = splitUrl(this.props.location.pathname)[2];
        console.log(currentProblem);

        fetch(URL + '/api/database/problem/getProblemByName/' + currentProblem, { headers: { ...getAuthorization() } })
            .then(res => res.json()).then(data => {
                console.log('problem')
                console.log(data)
                this.setState({ problem: data, problemLoaded: true });

                this.setOpenedProblem(data);

            });

        fetch(URL + '/api/database/Language/view', { headers: { ...getAuthorization() } }).then(res => res.json()).then(data => {
            console.log(data)
            this.setState({ displayLanguages: data })

        });

        if (localStorage.getItem('problem-page') != null) {
            this.setState({ page: JSON.parse(localStorage.getItem('problem-page')) });
            console.log('Storage exist');
        } else {
            console.log('Storage doesnt exists');
            localStorage.setItem('problem-page', JSON.stringify(this.state.page));
        }

    }

    componentDidUpdate() {
        let MathJax = $(window)[0].MathJax;

        try {
            console.log("UPDATTTTTT")
            console.log(MathJax);
            MathJax.Hub.Queue(["Typeset", MathJax.Hub]);

            <div id="anchor-remove-mathjax"></div>
            // removes the duplicates creadted by MathJax, not the best solution.
            if (this.state.page.problem) {
                while (typeof $('#problem-statement').prev()[0] !== 'undefined') {
                    $('#problem-statement').prev()[0].remove();
                }
            } else {
                $('.problem-description-container').children('.MathJax_CHTML').remove();
            }

        } catch (err) {
        }

    }

    setOpenedProblem = (p) => {
        const durationsData = {
            users: {
                username: parseLocalJwt().username
            },
            problems: {
                id: p.id
            }
        }

        fetch(URL + '/api/database/Durations/onProblemOpening', {
            method: 'POST',
            body: JSON.stringify(durationsData),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log("OPENING PROBLEM")
            console.log(data)
        });
    }

    onAceChange(newValue) {
        console.log(newValue);
        this.setState({ code: newValue });
    }

    handleSelectBoxChange(e) {
        let selectedItem = [...e.target.options].filter(o => o.selected)[0].value; //

        if (e.target.name === 'language') {
            selectedItem = this.state.displayLanguages.filter(l => l.compilerName === selectedItem)[0];
            this.setState({ [e.target.name]: { mode: selectedItem.mode, name: selectedItem.name } })
        } else {
            this.setState({ [e.target.name]: selectedItem });
        }
    }

    submitSubmission() {
        console.log(this.state);
        console.log(this.state.language.mode);
        console.log(btoa(this.state.code));
        this.setState({ sentSubmission: { submitting: true }, results: { result: [], error: '' } })
        let data = {
            code: btoa(this.state.code),
            language: { name: this.state.language.name },
            users: { username: parseLocalJwt().username },
            problem: { name: textToLowerCaseNoSpaces(this.state.problem.name) }
        }

        console.log(data);

        fetch(URL + '/submission', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log(data);
            this.setState({ sentSubmission: { submitting: true, waitingForResults: true, submission: data } })
            console.log(data.problem.testCases.length);
            window.resultsListener = setInterval(this.fetchForResults, 1000);
        });

    }

    fetchForResults() {
        console.log('Fetching results');
        fetch(URL + '/api/database/Scoring/viewBySubmissionId/' + this.state.sentSubmission.submission.id, { headers: { ...getAuthorization() } }).then(res => res.json())
            .then(data => {
                console.log(data);
                console.log(data.length);
                console.log(this.state.sentSubmission.submission.problem.testCases.length);
                if (JSON.stringify(data) !== '[]' && this.state.sentSubmission.submission.problem.testCases.length === data.length) {
                    this.setState({
                        sentSubmission: { submitting: false, scoringResults: data, waitingForResults: false },
                        results: { loaded: true }
                    })
                    clearInterval(window.resultsListener);

                }

                if (data.length === 1 && data[0].submissions.result != null) {

                    /* TODO : corrigir este corner case
                        caso a solução seja válida e faça 
                        um update que dará uma length de 1 emitirá um erro
    
                        alterar o if para garantir que apenas soluções
                        com erro o ativem
                    */

                    let submissionResult = data[0].submissions.result;
                    let name = submissionResult.name;
                    let errorMessage = submissionResult.message


                    if (name === 'Compiler Error') {
                        console.log(submissionResult);
                        this.setState({
                            sentSubmission: {
                                submitting: false, scoringResults: [], waitingForResults: false
                            },
                            results: {
                                result: { ...submissionResult },
                                error: 'Compiler Error'
                            }
                        })
                        clearInterval(window.resultsListener);
                    } else if (name === 'Runtime Error') {

                    }
                    //console.log("error message " + this.state.results.result.message);
                }

                // HERE
            })
    }

    onPageClick(e) {
        console.log(e.target.innerHTML.toLowerCase());
        let newPage = this.state.page;
        for (let property in newPage) {
            if (property === e.target.innerHTML.toLowerCase()) {
                newPage[property] = true;
                console.log(property);
            } else {
                newPage[property] = false;
            }
        }
        this.setState({ page: newPage });
        localStorage.setItem('problem-page', JSON.stringify(newPage));
        console.log(this.state.page);
    }

    handleScriptLoad() {
        console.log('LOADING SCRIPT');
        this.setState({ scriptLoaded: true })
    }

    render() {

        const aceStyle = {
            border: '1px solid #ccc',
            width: '100%',
            boxShadow: '0px 3px 8px 0px #ccc',
            marginLeft: '0'
        }

        let showLoading = "";
        if (this.state.sentSubmission.submitting) {
            showLoading =
                <div className="loader-container">
                    <h3>Evaluating your submission...</h3>
                    <div className="loader"></div>
                </div>
        } else {
            showLoading = <div className="button-container" style={{ marginTop: '-15px' }}>
                <input type="submit" className="btn btn-primary" value="Submit your code!" onClick={this.submitSubmission} />
            </div>
        }

        const problemInformation =
            <div className="col-sm-2 problem-info-container ">
                <table>
                    <tbody>
                        <tr>
                            <th><p className="align-left">Difficulty</p></th>
                            <th>
                                <p className="align-right">{this.state.problemLoaded ? this.state.problem.difficulty.name : ''}</p>
                            </th>
                        </tr>
                        <tr>
                            <th><p className="align-left">Creator</p></th>
                            <th>
                                <p className="align-right">{this.state.problemLoaded ?
                                    <Link to={'/user/' + this.state.problem.owner.username}>
                                        {this.state.problem.owner.username}
                                    </Link> : ''}</p>
                            </th>
                        </tr>
                        <tr>
                            <th><p className="align-left">Date</p></th>
                            <th>
                                <p className="align-right">{this.state.problemLoaded ? dateWithDay(this.state.problem.creationDate) : ''}</p>
                            </th>
                        </tr>
                        <tr>
                            <th><p className="align-left">Max Score</p></th>
                            <th>
                                <p className="align-right">{this.state.problemLoaded ? this.state.problem.maxScore : ''}</p>
                            </th>
                        </tr>
                    </tbody>
                </table>
            </div>;

        const problem = this.state.problem;


        const problemSection =
            <div>
                <div className="col-sm-10 problem-description-container" id="problem-section">
                    <div id="anchor-remove-mathjax"></div>
                    <h3 id="problem-statement">Problem Statement</h3>
                    {Parser(String(problem.description))}

                    <h3>Constraints</h3>
                    {Parser(String(problem.description))}

                    <h3>Input Format</h3>
                    {Parser(String(problem.description))}

                    <h3>Output Format</h3>
                    {Parser(String(problem.outputFormat))}

                    {typeof problem.testCases !== "undefined" ? problem.testCases.map((tc, i) => {
                        if (tc.shown) {
                            return <div>
                                <h3>Test Case {i + 1}</h3>
                                <div className="problem-testcase-wrapper">
                                    <div>
                                        <h4>Explanation</h4>
                                        <p>{tc.description}</p>
                                    </div>
                                    <div>
                                        <h4>Input</h4>
                                        <p>{tc.input}</p>
                                    </div>
                                    <div>
                                        <h4>Output</h4>
                                        <p>{tc.output}</p>
                                    </div>
                                </div>
                            </div>
                        }
                    }) : ''}

                </div>
                {problemInformation}
                <div className="col-sm-12 ace-editor-container">
                    <div className="ace-editor">
                        <div className="ace-editor-navbar">
                            <select name="language" id="" placeholder="Language" onChange={this.handleSelectBoxChange}>
                                {this.state.displayLanguages.map(l => (
                                    <option key={l.id} value={l.compilerName}>{l.name}</option>
                                ))}
                            </select>

                            <select name="theme" id="" onChange={this.handleSelectBoxChange}>
                                <option value="github">github</option>
                                <option value="tomorrow">tomorrow</option>
                                <option value="monokai">monokai</option>
                                <option value="terminal">terminal</option>
                            </select>
                        </div>
                        <AceEditor style={aceStyle} mode={this.state.language.mode} theme={this.state.theme} name="" onLoad={this.onLoad} onChange={this.onAceChange}
                            fontSize={14} showPrintMargin={true} showGutter={true} highlightActiveLine={true} value={this.state.code}
                            setOptions={{
                                enableBasicAutocompletion: true,
                                enableLiveAutocompletion: true,
                                enableSnippets: false,
                                showLineNumbers: true,
                                tabSize: 3,
                            }} />
                    </div>

                    {showLoading}

                </div>
            </div>;

        const submissionSection =
            <div>
                <div className="col-sm-12 problem-description-container ">
                    <div id="anchor-remove-mathjax"></div>
                    <Submissions pathname={this.props.location.pathname} />
                </div>
            </div>;

        const leaderboardSection =
            <div>
                <div className="col-sm-12 problem-description-container">
                    <div id="anchor-remove-mathjax"></div>
                    <Leaderboard pathname={this.props.location.pathname} />
                </div>
            </div>;

        let sectionToRender = "";
        console.log('STATE')
        console.log(this.state);
        if (this.state.page.submissions) {
            sectionToRender = submissionSection;
        } else if (this.state.page.leaderboard) {
            sectionToRender = leaderboardSection;
        } else {
            if (this.state.problemLoaded) {
                sectionToRender = problemSection;
            }
        }


        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title={this.state.problem.name} />
                    <div className="problem-nav">
                        <ul onClick={this.onPageClick}>
                            <li className={this.state.page.problem ? 'active' : ''}>Problem</li>
                            <li className={this.state.page.submissions ? 'active' : ''}>Submissions</li>
                            <li className={this.state.page.leaderboard ? 'active' : ''}>Leaderboard</li>
                        </ul>
                    </div>

                    {sectionToRender}



                    {this.state.results.loaded ?
                        <Redirect to={{
                            pathname: this.props.location.pathname + "/view-results", state: {
                                information: this.state.sentSubmission.scoringResults
                            }
                        }} /> : ''
                    }

                </div>
                <div className="row">
                    {this.state.results.error === 'Compiler Error' && this.state.page.problem ? <CompilerError errorMessage={this.state.results.result.message} /> : ''}
                </div>
            </div >
        );
    }
}

export default Problem;
