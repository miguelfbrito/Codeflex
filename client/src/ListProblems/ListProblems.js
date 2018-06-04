import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import { Link } from 'react-router-dom';
import { splitUrl, textToLowerCaseNoSpaces } from '../commons/Utils';
import PathLink from '../PathLink/PathLink';

import './ListProblems.css';

class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
            problems: [],
            filteredProblems: [],
            difficulties: []
        }

        this.checkBoxFilter = React.createRef();
        this.onChangeSelectBox = this.onChangeSelectBox.bind(this);
    }

    componentDidMount() {
        const currentCategory = splitUrl(this.props.location.pathname)[1];
        fetch(URL + ':8080/api/database/PractiseCategory/getAllWithoutTestCases/' + JSON.parse(localStorage.getItem('userData')).id)
            .then(res => res.json()).then(data => {
                let newData = data.filter(d => textToLowerCaseNoSpaces(d.name) === currentCategory)
                if (JSON.stringify(newData) === '[]') {
                    window.location.href = '/'
                } else {
                    this.setState({ problems: newData[0].problem, filteredProblems: newData[0].problem });
                    console.log(newData[0].problem);
                }
            })

        fetch(URL + ':8080/api/database/difficulty/view')
            .then(res => res.json()).then(data => { this.setState({ difficulties: data }) })
    }

    onChangeSelectBox(e) {

        let currentProblems = this.state.problems;
        let allNodes = this.checkBoxFilter.current;
        let newList = [];
        this.loopElements(allNodes, newList)

        if (JSON.stringify(newList) === '[]') {
            this.setState({ filteredProblems: this.state.problems })
            return;
        }
        let keepItem;
        currentProblems = currentProblems.filter(p => {
            keepItem = false;
            if (newList.includes("Solved") && p.solved) {
                keepItem = true;
            }
            if (newList.includes("Unsolved") && !p.solved) {
                keepItem = true;
            }

            if(!newList.includes("Solved") && !newList.includes("Unsolved")){
                keepItem = true;
            }

            if (keepItem) {
                return true;
            }
            return false;
        });

        keepItem = true;
        currentProblems = currentProblems.filter(p => {

            for (var key in newList) {
                if (newList[key] !== 'Unsolved' && newList[key] !== 'Solved') {
                    if (p.difficulty.name === newList[key]) {
                        keepItem = true;
                        break;
                    } else {
                        keepItem = false;
                    }
                }
            }

            if (keepItem) {
                return true;
            }
            return false;
        })
        this.setState({ filteredProblems: currentProblems });
    }

    loopElements(node, list) {
        let nodes = node.childNodes;
        for (let i = 0; i < nodes.length; i++) {
            if (nodes[i].tagName === 'INPUT') {
                if (nodes[i].checked) {
                    list.push(nodes[i].name);
                }
                console.log(nodes[i].name);
                console.log(nodes[i].checked);
            }

            if (nodes[i].childNodes.length > 0) {
                this.loopElements(nodes[i], list);
            }
        }
        return list;
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title={splitUrl(this.props.match.url)[1]}/>                   
                    <div className="col-sm-9 problems-container">
                        {this.state.filteredProblems.map((problem, index) => (
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
                                    }}><input type="submit" className="btn btn-primary problem-button" value={problem.solved ? 'Solve again' : 'Solve problem'} /></Link>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="col-sm-1"></div>
                    <div className="col-sm-2 problem-info" ref={this.checkBoxFilter} onChange={this.onChangeSelectBox}>
                        <h3>Status</h3>
                        <input name="Solved" type="checkbox" />
                        <p>Solved</p>
                        <input name="Unsolved" type="checkbox" />
                        <p>Unsolved</p>
                        <hr />
                        <h3>Difficulty</h3>
                        {this.state.difficulties.map((difficulty, diffIndex) => (
                            <div>
                                <input name={difficulty.name} type="checkbox" />
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
