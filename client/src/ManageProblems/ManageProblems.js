import React from 'react';
import PathLink from '../PathLink/PathLink'
import ReactTable from 'react-table';
import { Redirect, Link } from 'react-router-dom';

import { URL } from '../commons/Constants';
import { splitUrl, textToLowerCaseNoSpaces } from '../commons/Utils';

import '../../node_modules/react-table/react-table.css'
import './ManageProblems.css';

class ManageProblems extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            origin: '',
            problems: []
        }
    }

    componentDidMount() {
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
            fetch(URL + '/api/database/Tournament/getAllProblemsByName/' + url[2]).then(res => res.json())
                .then(data => {
                    console.log(data);
                    this.setState({ problems: data });
                })
        } else if (origin === 'manage') {
            fetch(URL + '/api/database/Problem/viewAllByOwnerId/' + JSON.parse(localStorage.getItem('userData')).id)
                .then(res => res.json())
                .then(data => {
                    data = data.filter(d => d.tournament === null);
                    console.log('Getting problems from manage')
                    console.log(data);
                    this.setState({ problems: data });
                })
        }
    }

    onIconClick(e) {

    }

    render() {

        let content =
            <div className="add-problem-container">
                <h3 style={{marginBottom:'15px'}}>{this.state.origin === 'tournament' ? 
                'There are no problems on this tournament yet.' : "You haven't added any problems yet."
                }</h3>
                
                <Link to={
                    this.state.origin === 'tournament' ? "/compete/manage-tournaments/" + splitUrl(this.props.location.pathname)[2] + "/add"
                    : "/manage/problems/add"}>
                    <input className="btn btn-codeflex" type="button" value="Add problem" />
                </Link>
            </div>

        if (this.state.problems.length > 0) {
            content = <ReactTable
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
                        accessor: t => (
                            <div>
                                <Link to={this.props.location.pathname + '/edit/' + textToLowerCaseNoSpaces(t.name)}>
                                    < i className="material-icons manage-tournament-icon" id="edit" onClick={this.onIconClick}>edit</i>
                                </Link>
                                <i className="material-icons manage-tournament-icon" id="delete" onClick={this.onIconClick}>delete</i>
                                {/*this.state.redirect && this.state.redirectDestination === 'edit' ?
                                <Redirect to={{ pathname: "/compete/manage-tournaments/" + textToLowerCaseNoSpaces(t.tournament.name) }} /> : ''*/}
                            </div >
                        )
                    }
                ]}
                defaultPageSize={25}
                pageSize={Math.min(this.state.problems.length, 25)}
                style={{
                    height: Math.min(this.state.problems.length * 125, 1000) + "px" // This will force the table body to overflow and scroll, since there is not enough room
                }
                }
                showPagination={false}
                className="-highlight"
            />
        }

        return (
            <div className="container">
                <div className="row">
                    {this.state.origin === 'tournament' ?
                        <PathLink path={this.props.location.pathname} title={splitUrl(this.props.match.url)[2]} />
                        :
                        <PathLink path={this.props.location.pathname} title="Manage Problems" />}
                        <div>
                            {content}
                        </div>
                </div>
            </div>
                )
            }
        }
        
export default ManageProblems;