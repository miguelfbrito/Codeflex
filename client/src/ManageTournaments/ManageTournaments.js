import React from 'react';
import PathLink from '../PathLink/PathLink';
import ReactTable from "react-table";
import { URL } from '../commons/Constants';
import { dateWithHoursAndDay } from '../commons/Utils';

import './ManageTournaments.css'
import "../../node_modules/react-table/react-table.css";

class ManageTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tournaments: []
        }
    }

    componentDidMount() {
        console.log('Fecthing data')
        fetch(URL + '/api/database/Tournament/viewAllWithRegisteredUsersByOwnerId/' + JSON.parse(localStorage.getItem('userData')).id).then(res => res.json())
            .then(data => {
                this.setState({ tournaments: data })
                console.log(data);
            })
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Manage tournaments" />

                    <div>
                        <ReactTable
                            data={this.state.tournaments}
                            columns={[
                                {
                                    Header: "Name",
                                    id: "tournamentName",
                                    accessor: l => l.tournament.name
                                },
                                {
                                    Header: "Starting on",
                                    id: "startingDate",
                                    accessor: l => dateWithHoursAndDay(l.tournament.startingDate)
                                },
                                {
                                    Header: "Ending on",
                                    id: "endingDate",
                                    accessor: l => dateWithHoursAndDay(l.tournament.endingDate)
                                },
                                {
                                    Header: "Registered users",
                                    id: "registeredUsers",
                                    accessor: l => l.users.length
                                },
                                {
                                    Header: "",
                                    id: "icons",
                                    accessor: () => (
                                        <div>
                                            <i className="material-icons manage-tournament-icon">edit</i>
                                            <i className="material-icons manage-tournament-icon">delete</i>
                                        </div>
                                    )
                                }
                            ]}
                            defaultPageSize={10}
                            style={{
                                height: "500px" // This will force the table body to overflow and scroll, since there is not enough room
                            }}
                            className="-striped -highlight"
                        />
                        <br />
                    </div>
                </div>
            </div>
        )
    }
}

export default ManageTournaments;