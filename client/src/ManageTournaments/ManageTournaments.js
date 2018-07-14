import React from 'react';
import PathLink from '../PathLink/PathLink';
import ReactTable from 'react-table';
import { Redirect, Link } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { dateWithHoursAndDay, textToLowerCaseNoSpaces, splitUrl, getAuthorization, parseLocalJwt } from '../commons/Utils';

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
            problemDestination: '',
            teste: false,
            location: ''
        }

        this.onIconClick = this.onIconClick.bind(this);
        this.onIconDelete = this.onIconDelete.bind(this);
        this.fetchTournaments = this.fetchTournaments.bind(this);
    }

    componentDidMount() {
        this.fetchTournaments();
        this.setState({ location: splitUrl(this.props.location.pathname)[0] });
    }

    fetchTournaments() {
        console.log(this.state.location)
        let location = splitUrl(this.props.location.pathname)[0];
        if (location === 'compete') {
            fetch(URL + '/api/database/Tournament/viewAllWithRegisteredUsersByOwnerUsername/' + parseLocalJwt().username, {
                headers: { ...getAuthorization() }
            }).then(res => res.json())
                .then(data => {
                    this.setState({ tournaments: data })
                    console.log(data);
                })
        } else if (location === 'manage') {
            fetch(URL + '/api/database/Tournament/viewAllPublicTournaments', {
                headers: { ...getAuthorization() }
            }).then(res => res.json())
                .then(data => {
                    this.setState({ tournaments: data })
                    console.log(data);
                })
        }

    }

    onIconDelete(tournamentName) {
        console.log('Deleting')
        tournamentName = textToLowerCaseNoSpaces(tournamentName);
        const data = {
            tournament: { name: tournamentName }
        }
        fetch(URL + '/api/database/Tournament/deleteByName/' + tournamentName, {
            method: 'POST',
            headers: {
                ...getAuthorization()
            }
        }).then(() => {
            this.fetchTournaments();
        });

    }

    onIconClick(e, problemName) {
        console.log(e.target.id)
        switch (e.target.id) {
            case 'visibility':
                this.setState({ redirectDestination: 'visibility' })
                break;
            case 'delete':
                this.setState({ redirectDestination: 'delete' })
                break;
            case 'edit':
                this.setState({ redirectDestination: 'edit' })
                break;
            default:
                break;
        }

        this.setState({ redirect: true, problemDestination: textToLowerCaseNoSpaces(problemName) });
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
                                            <i className="material-icons manage-tournament-icon" id="visibility" onClick={(e, name) => this.onIconClick(e, t.tournament.name)} >visibility</i>
                                            <i className="material-icons manage-tournament-icon" id="edit" onClick={(e, name) => this.onIconClick(e, t.tournament.name)}>edit</i>
                                            <i className="material-icons manage-tournament-icon" id="delete" onClick={(name) => this.onIconDelete(t.tournament.name)}>delete</i>
                                            {console.log("Icon index " + t.tournament.name)}
                                            {this.state.redirect && this.state.redirectDestination === 'visibility' ?
                                                <Redirect to={this.state.location === 'compete' ?
                                                    { pathname: "/compete/manage-tournaments/" + this.state.problemDestination } :
                                                    { pathname: "/manage/tournaments/" + this.state.problemDestination }} /> : ''}

                                            {this.state.redirect && this.state.redirectDestination === 'edit' ?
                                                <Redirect to={this.state.location === 'compete' ? { pathname: "/compete/manage-tournaments/" + textToLowerCaseNoSpaces(t.tournament.name) + "/edit" }
                                                    : { pathname: "/manage/tournaments/" + textToLowerCaseNoSpaces(t.tournament.name) + "/edit" }} /> : ''}
                                        </div>
                                    )
                                }
                            ]}
                            defaultPageSize={rows}
                            pageSize={Math.min(rows, 15)}
                            style={{
                            }}
                            showPagination={false}
                            className="-highlight"
                        />
                    </div>
                </div>
                <Link to={this.state.location === 'compete' ? "/compete/create-tournament" : "/manage/tournaments/add"}> <input type="button" style={{ float: 'right', marginTop: '25px', marginRight: '0' }} className="btn btn-codeflex" value="Create tournament" /></Link>
            </div>
        )
    }
}

export default ManageTournaments;