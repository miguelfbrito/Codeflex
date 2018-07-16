import React from 'react';
import PathLink from '../PathLink/PathLink'
import ReactTable from 'react-table';
import { Redirect, Link } from 'react-router-dom';

import { URL } from '../commons/Constants';
import { splitUrl, textToLowerCaseNoSpaces, getAuthorization, parseLocalJwt } from '../commons/Utils';

import '../../node_modules/react-table/react-table.css'
import './ManageProblems.css';
import PageNotFound from '../PageNotFound/PageNotFound';

class ManageProblems extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            origin: '',
            problems: [],
            userIsOwner: true
        }
    }

    componentDidMount() {
        if (parseLocalJwt().role != 'CONTENT_MANAGER') {
            this.isUserTournamentOwner();
        }
        this.fetchProblems();
    }

    isUserTournamentOwner = () => {
        fetch(URL + '/api/database/tournament/isUserTournamentOwner/' + this.props.match.params + "/" + parseLocalJwt().username, {
            headers: new Headers({ ...getAuthorization() })
        }).then(res => { if (res.status === 200) { this.setState({ userIsOwner: true }); } else { this.setState({ userIsOwner: false }); } })
    }

    fetchProblems = () => {
        const url = splitUrl(this.props.location.pathname);
        let origin = '';
        if (url[0] === 'manage') {
            this.setState({ origin: 'manage' });
            origin = 'manage';
        } else {
            this.setState({ origin: 'tournament' });
            origin = 'tournament'
        }

        if (origin === 'tournament') {
            this.fetchAllProblemsByTournamentName(url[2]);
        } else if (origin === 'manage') {
            if (url[1] === 'problems') {

                fetch(URL + '/api/database/Problem/viewAllByOwnerUsername/' + parseLocalJwt().username, {
                    headers: { ...getAuthorization() }
                })
                    .then(res => res.json())
                    .then(data => {
                        data = data.filter(d => d.tournament === null);
                        console.log('Getting problems from manage')
                        console.log(data);
                        this.setState({ problems: data });
                    })
            } else if (url[1] === 'tournaments') {
                this.fetchAllProblemsByTournamentName(url[2]);
            }
        }
    }

    fetchAllProblemsByTournamentName = (tournamentName) => {
        fetch(URL + '/api/database/Tournament/getAllProblemsByName/' + tournamentName, {
            headers: { ...getAuthorization() }
        }).then(res => res.json())
            .then(data => {
                console.log(data);
                this.setState({ problems: data });
            })
    }

    deleteProblem = (p) => {
        fetch(URL + '/api/database/Problem/deleteByName/' + textToLowerCaseNoSpaces(p.name), {
            method: 'POST',
            headers: { ...getAuthorization() }
        })
            .then(() => {
                this.fetchProblems();

                let currentProblems = this.state.problems;
                let index = currentProblems.indexOf(p);
                currentProblems.splice(index, 1);

                this.setState({ problems: currentProblems });

            }).catch((error) => console.log(error));
    }

    render() {

        if (!this.state.userIsOwner) {
            return (
                <PageNotFound />
            )
        }

        let content =
            <div className="add-problem-container">
                <h3 style={{ marginBottom: '15px' }}>{this.state.origin === 'tournament' ?
                    'There are no problems on this tournament yet.' : "You haven't added any problems yet."
                }</h3>

                <Link to={
                    this.state.origin === 'tournament' ? "/compete/manage-tournaments/" + splitUrl(this.props.location.pathname)[2] + "/add"
                        : "/manage/problems/add"}>
                    <input className="btn btn-codeflex" type="button" value="Add problem" />
                </Link>
            </div>

        if (this.state.problems.length > 0) {
            content = <div>
                <ReactTable
                    data={this.state.problems}
                    columns={[
                        {
                            Header: "Name",
                            id: "problemName",
                            accessor: p => (
                                <p>{p.name} </p>
                            )
                        },
                        {
                            Header: "Difficulty",
                            id: "startingDate",
                            accessor: p => p.difficulty.name,
                            sortMethod: (a, b) => {
                                console.log(a);
                                return a >= b ? 1 : -1;
                            }
                        },
                        {
                            Header: "Max score",
                            id: "maxScore",
                            accessor: t => t.maxScore
                        },
                        {
                            Header: "#TestCases",
                            id: "testCases",
                            accessor: p => (
                                <div>
                                    {p.testCases.length}
                                    <Link to={this.props.location.pathname + "/" + textToLowerCaseNoSpaces(p.name) + "/test-cases"}> <i className="material-icons manage-tournament-icon" onClick={this.onIconClick}>edit</i> </Link>
                                </div>
                            )
                        },
                        {
                            Header: "",
                            id: "icons",
                            accessor: p => (
                                <div>
                                    <Link to={this.props.location.pathname + '/' + textToLowerCaseNoSpaces(p.name) + '/edit'}>
                                        <i name="edit" className="material-icons manage-tournament-icon" id="edit">edit</i>
                                    </Link>
                                    <i name="delete" className="material-icons manage-tournament-icon icon-ligth-blue" id="delete" onClick={() => this.deleteProblem(p)}>delete</i>
                                    {/*this.state.redirect && this.state.redirectDestination === 'edit' ?
                                <Redirect to={{ pathname: "/compete/manage-tournaments/" + textToLowerCaseNoSpaces(t.tournament.name) }} /> : ''*/}
                                </div >
                            )
                        }
                    ]}
                    defaultPageSize={25}
                    pageSize={Math.min(this.state.problems.length, 25)}
                    style={{
                    }
                    }
                    showPagination={false}
                    className="-highlight"
                />

                <div style={{ textAlign: 'right', marginTop: '15px' }}>
                    <Link to={this.props.location.pathname + '/add'}>
                        <input type="button" className="btn btn-codeflex" value="Add new problem" />
                    </Link>
                </div>
            </div>
        }

        return (
            <div className="container">
                <div className="row">
                    {this.state.origin === 'tournament' ?
                        <PathLink path={this.props.location.pathname} title={splitUrl(this.props.match.url)[2]} />
                        :
                        <PathLink path={this.props.location.pathname} title="Manage Problems" />}
                    <div>
                        <div>
                            {content}
                        </div>

                    </div>
                </div>
            </div>
        )
    }
}

export default ManageProblems;