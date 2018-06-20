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
            problems: []
        }
    }

    componentDidMount() {
        const problemName = splitUrl(this.props.location.pathname)[2];
        fetch(URL + '/api/database/Tournament/getAllProblemsByName/' + problemName).then(res => res.json())
            .then(data => {
                console.log(data);
                this.setState({ problems: data });
            })
    }

    onIconClick(e) {

    }

    render() {

        let content =
            <div className="add-problem-container">
                <h3>There are no problems on this tournament yet.</h3>
                <Link to={"/compete/manage-tournaments/" + splitUrl(this.props.location.pathname)[2] + "/add"}>
                    <input className="btn btn-primary" type="button" value="Add problem" />
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
                    <PathLink path={this.props.location.pathname} title={splitUrl(this.props.match.url)[2]} />
                    <div>
                        {content}
                    </div>
                </div>
            </div>
        )
    }
}

export default ManageProblems;