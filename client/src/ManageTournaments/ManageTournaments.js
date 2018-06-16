import React from 'react';
import PathLink from '../PathLink/PathLink';
import ReactTable from 'react-table';
import { Redirect } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { dateWithHoursAndDay, textToLowerCaseNoSpaces } from '../commons/Utils';

import './ManageTournaments.css'
import "../../node_modules/react-table/react-table.css";
import "../commons/react-table.css"

class ManageTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tournaments: [],
            redirect: false,
            redirectDestination: '',
            problemDestination : ''
        }

        this.onIconClick = this.onIconClick.bind(this);
    }

    componentDidMount() {
        console.log('Fecthing data')
        fetch(URL + '/api/database/Tournament/viewAllWithRegisteredUsersByOwnerId/' + JSON.parse(localStorage.getItem('userData')).id).then(res => res.json())
            .then(data => {
                this.setState({ tournaments: data })
                console.log(data);
            })
    }

    onIconClick(e, problemName) {
        console.log(e.target.id)
        switch (e.target.id) {
            case 'edit':
                console.log("EDITOU")
                this.setState({ redirectDestination: 'edit' })
                break;
            case 'delete':
                this.setState({ redirectDestination: 'delete' })
                break;
            default:
                break;
        }

        this.setState({ redirect: true, problemDestination : problemName});
    }

    render() {
        const rows = this.state.tournaments.length;
        console.log("ROWS " + rows);
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Manage tournaments" />

                    <div>
                        <ReactTable
                            noDataText="You haven't created any tournaments"
                            data={this.state.tournaments}
                            columns={[
                                {
                                    Header: "Status",
                                    id: "status",
                                    accessor: t => (
                                        new Date(t.tournament.startingDate).getTime() > new Date().getTime() ? "Starting soon" :
                                            new Date(t.tournament.endingDate).getTime() >= new Date().getTime() ? "Ongoing" :
                                                "Finished"
                                    )
                                },
                                {
                                    Header: "Name",
                                    id: "tournamentName",
                                    accessor: t => t.tournament.name
                                },
                                {
                                    Header: "Starting on",
                                    id: "startingDate",
                                    accessor: t => (
                                        dateWithHoursAndDay(t.tournament.startingDate)
                                    )
                                },
                                {
                                    Header: "Ending on",
                                    id: "endingDate",
                                    accessor: t => dateWithHoursAndDay(t.tournament.endingDate)
                                },
                                {
                                    Header: "Code",
                                    id: "code",
                                    accessor: t => t.tournament.code
                                },
                                {
                                    Header: "Registered users",
                                    id: "registeredUsers",
                                    accessor: t => t.users.length
                                },

                                {
                                    Header: "",
                                    id: "icons",
                                    accessor: t => (
                                        <div key={t.tournament.id}>
                                            <i className="material-icons manage-tournament-icon" id="visibility" onClick={this.onIconClick}>visibility</i>
                                            <i key={t.tournament.id} className="material-icons manage-tournament-icon" id="edit" name={t.tournament.name} onClick={(e, name) => this.onIconClick(e, textToLowerCaseNoSpaces(t.tournament.name))}>edit</i>
                                            <i className="material-icons manage-tournament-icon" id="delete" onClick={this.onIconClick}>delete</i>
                                            {console.log("Icon index " + t.tournament.name)}
                                            {this.state.redirect && this.state.redirectDestination === 'edit' ?
                                                <Redirect to={{ pathname: "/compete/manage-tournaments/" + this.state.problemDestination }} /> : ''}
                                        </div>
                                    )
                                }
                            ]}
                            defaultPageSize={rows}
                            pageSize={Math.min(rows, 15)}
                            style={{
                                height: Math.min(this.state.tournaments.length * 100, 750) + "px" // This will force the table body to overflow and scroll, since there is not enough room
                            }}
                            showPagination={false}
                            className="-highlight"
                        />
                    </div>
                </div>
            </div>
        )
    }
}

export default ManageTournaments;