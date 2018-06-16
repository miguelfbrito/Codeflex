import React from 'react';
import PathLink from '../PathLink/PathLink'
import ReactTable from 'react-table';
import { Redirect } from 'react-router-dom';

import { URL } from '../commons/Constants';
import { splitUrl } from '../commons/Utils';

import '../../node_modules/react-table/react-table.css'
import './ManageIndividualTournament.css';

class ManageIndividualTournament extends React.Component {
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
                <input className="btn btn-primary" type="button" value="Add problem" />
            </div>

        if (this.state.problems.length > 0) {
            content = <ReactTable
                data={this.state.problems}
                columns={[
                    {
                        Header: "Name",
                        id: "problemName",
                        accessor: p => p.name
                    },
                    {
                        Header: "Difficulty",
                        id: "startingDate",
                        accessor: p => p.difficulty.name
                    },
                    {
                        Header: "Max score",
                        id: "maxScore",
                        accessor: t => t.maxScore
                    },
                    {
                        Header: "#TestCases",
                        id: "testCases",
                        accessor: p => p.testCases.length
                    },
                    {
                        Header: "",
                        id: "icons",
                        accessor: t => (
                            <div>
                                <i className="material-icons manage-tournament-icon" id="edit" onClick={this.onIconClick}>edit</i>
                                <i className="material-icons manage-tournament-icon" id="delete" onClick={this.onIconClick}>delete</i>
                                {/*this.state.redirect && this.state.redirectDestination === 'edit' ?
                                <Redirect to={{ pathname: "/compete/manage-tournaments/" + textToLowerCaseNoSpaces(t.tournament.name) }} /> : ''*/}
                            </div>
                        )
                    }
                ]}
                defaultPageSize={25}
                pageSize={Math.min(this.state.problems.length, 25)}
                style={{
                    height: Math.min(this.state.problems.length * 100, 1000) + "px" // This will force the table body to overflow and scroll, since there is not enough room
                }}
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

export default ManageIndividualTournament;