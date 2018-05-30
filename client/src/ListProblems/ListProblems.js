import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import { Link } from 'react-router-dom';
import { splitUrl } from '../commons/Utils';

import './ListProblems.css';

class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
            problems: []
        }

    }

    componentDidMount() {
        fetch(URL + ':8080/api/database/PractiseCategory/getAllProblemsByCategoryId/' + this.props.location.state.categoryId//, {
            //headers: new Headers({
            // 'Content-Type': 'application/json',
            //'Authorization': 'Token ' + localUser.token
            // })
        ).then(res => res.json()).then(data => {
            this.setState({ problems: data })
            console.log(this.state.problems);
        })
    }


    render() {
        return (
            <div className="container">
                <p>{splitUrl(this.props.match.url)[1].toUpperCase().replace('-', ' ')}</p>
                <h2 className="page-title">Problems</h2>
                <hr style={{ width: '100%', height: '10px' }} />
                <div className="row">
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
                                    <input type="submit" className="btn btn-primary" id="problem-button" value="Solve Problem"/>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="col-sm-1"></div>
                    <div className="col-sm-2">
                        <h3>Status</h3>
                        <p>Solved</p>
                        <p>Unsolved</p>
                        <hr />
                        <h3>Difficulty</h3>
                        <p>Easy</p>
                        <p>Medium</p>
                        <p>Hard</p>
                        <p>Expert</p>
                    </div>
                </div>
            </div>
        );
    }
}

export default ListProblems;
