import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import { Link } from 'react-router-dom';
import { splitUrl, textToLowerCaseNoSpaces, parseLocalJwt, getAuthorization } from '../commons/Utils';
import PathLink from '../PathLink/PathLink';
import ProblemFilter from './ProblemFilter/ProblemFilter';

import './ListProblems.css';
import PageNotFound from '../PageNotFound/PageNotFound';

class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
            registered: true,
            problems: [],
            filteredProblems: [],
            difficulties: [],
            tournament: {}
        }

        this.checkBoxFilter = React.createRef();
        this.onChangeSelectBox = this.onChangeSelectBox.bind(this);
        this.fetchProblemsByCategory = this.fetchProblemsByCategory.bind(this);
        this.fetchProblemsByTournament = this.fetchProblemsByTournament.bind(this);

    }

    componentDidMount() {

        const url = splitUrl(this.props.location.pathname);



        if (url[0] === 'practise') {
            console.log('practise')
            this.fetchProblemsByCategory();
        } else if (url[0] === 'compete') {
            console.log('compete')
            this.isUserRegisteredInTournament();
        }

        fetch(URL + '/api/database/difficulty/view', {
            headers: {
                ...getAuthorization()
            }
        })
            .then(res => res.json()).then(data => { this.setState({ difficulties: data }) })
    }

    isUserRegisteredInTournament = () => {
        fetch(URL + '/api/database/Rating/isUserRegisteredInTournamentTest/' + parseLocalJwt().username + "/" + this.props.match.params.tournamentName, {
            headers: { ...getAuthorization() }
        }).then(res => {
            if (res.status === 200) {
                this.setState({ registered: true });
                this.fetchTournament();
                this.fetchProblemsByTournament();
            } else {
                this.setState({ registered: false });
            }
        })

    }

    fetchTournament = () => {
        fetch(URL + '/api/database/Tournament/viewByName/' + this.props.match.params.tournamentName, {
            headers: { ...getAuthorization() }
        }).then(res => res.json()
        ).then(data => {
            this.setState({ tournament: data });
        })

    }
    fetchProblemsByTournament() {
        const currentTournament = splitUrl(this.props.location.pathname)[1];
        fetch(URL + '/api/database/tournament/getAllProblemsByName/' + currentTournament, {
            headers: {
                ...getAuthorization()
            }
        }).then(res => res.json())
            .then(data => {
                console.log('Tournament problems')
                console.log(data);
                this.setState({ problems: data, filteredProblems: data })
            })
    }

    fetchProblemsByCategory() {
        const currentCategory = splitUrl(this.props.location.pathname)[1];
        fetch(URL + '/api/database/PractiseCategory/getAllWithoutTestCases/' + parseLocalJwt().username, {
            headers: {
                ...getAuthorization()
            }
        })
            .then(res => res.json()).then(data => {
                let newData = data.filter(d => textToLowerCaseNoSpaces(d.name) === currentCategory)
                if (JSON.stringify(newData) === '[]') {
                    window.location.href = '/'
                } else {
                    this.setState({ problems: newData[0].problem, filteredProblems: newData[0].problem });
                    console.log(newData[0].problem);
                }
            }).catch(err => console.log(err));
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

            if (!newList.includes("Solved") && !newList.includes("Unsolved")) {
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

        if (!this.state.registered && !this.state.tournament.showWebsite) {
            return (
                <PageNotFound />
            )
        }
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title={splitUrl(this.props.match.url)[1]} />
                    <div className="col-sm-10 problems-container">

                        {this.state.filteredProblems.sort((a, b) => a.difficulty.id - b.difficulty.id).map((problem, index) => (

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
                                    }}><input type="submit" className="btn btn-codeflex " value={problem.solved ? 'Solve again' : 'Solve problem'} /></Link>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="col-sm-2 problem-info" ref={this.checkBoxFilter} onChange={this.onChangeSelectBox}>
                        {splitUrl(this.props.location.pathname)[0] !== 'practise' ? <Link to={this.props.location.pathname + '/leaderboard'}>
                            <h3>Leaderboard</h3>
                        </Link> : ''}
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
