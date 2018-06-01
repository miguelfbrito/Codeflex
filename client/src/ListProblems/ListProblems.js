import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import { Link } from 'react-router-dom';
import { splitUrl, textToLowerCaseNoSpaces } from '../commons/Utils';

import './ListProblems.css';

class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
            problems: [],
            difficulties: []
        }

    }

    componentDidMount() {
        const currentCategory = splitUrl(this.props.location.pathname)[1];
        fetch(URL + ':8080/api/database/PractiseCategory/getAllWithoutTestCases/')
            .then(res => res.json()).then(data => {
                let newData = data.filter(d => textToLowerCaseNoSpaces(d.name) === currentCategory)
                if (JSON.stringify(newData) === '[]') {
                    window.location.href = '/'
                } else {
                    this.setState({ problems: newData[0].problem });
                }
            })

        fetch(URL + ':8080/api/database/difficulty/view')
            .then(res => res.json()).then(data => { this.setState({ difficulties: data }) })
    }


    render() {
        return (
            <div className="container">
                <div className="row">
                    <p>{splitUrl(this.props.match.url)[1].toUpperCase().replace('-', ' ')}</p>
                    <h2 className="page-title">Problems</h2>
                    <hr style={{ width: '100%', height: '10px' }} />
                    <div className="col-sm-9 problems-container">
                        {this.state.problems.map((problem, index) => (
                            <div className="problem-container">
                                <div>
                                    <p id="problem-name">
                                        {problem.name}
                                    </p>
                                    <p id="problem-difficulty">
                                        {problem.difficulty.name}
                                    </p>
                                </div>
                                <div id="button-container">
                                    <Link to={{
                                        pathname: this.props.location.pathname + '/' + textToLowerCaseNoSpaces(problem.name), state: {
                                            problemId: problem.id,
                                            problemName: problem.name
                                        }
                                    }}><input type="submit" className="btn btn-primary" id="problem-button" value="Solve Problem" /></Link>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="col-sm-1"></div>
                    <div className="col-sm-2 problem-info">
                        <h3>Status</h3>
                        <input type="checkbox" />
                        <p>Solved</p>
                        <input type="checkbox" />
                        <p>Unsolved</p>
                        <hr />
                        <h3>Difficulty</h3>
                        {this.state.difficulties.map((difficulty, diffIndex) => (
                            <div>
                                <input type="checkbox" />
                                <p>{difficulty.name}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        );
    }
}

export default ListProblems;
