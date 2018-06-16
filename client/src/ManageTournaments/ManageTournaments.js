import React from 'react';
import PathLink from '../PathLink/PathLink';
import ReactTable from "react-table";
import { URL } from '../commons/Constants';
import { dateWithHoursAndDay} from '../commons/Utils';

import './ManageTournaments.css'
import "../../node_modules/react-table/react-table.css";
import "../commons/react-table.css"

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
                                    id:"status",
                                    accessor : t => (
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
                                    accessor: () => (
                                        <div>
                                            <i className="material-icons manage-tournament-icon">edit</i>
                                            <i className="material-icons manage-tournament-icon">delete</i>
                                        </div>
                                    )
                                }
                            ]}
                            defaultPageSize={rows}
                            pageSize={Math.min(rows, 15)}
                            style={{
                                height: Math.min(this.state.tournaments.length*100, 750) + "px" // This will force the table body to overflow and scroll, since there is not enough room
                            }}
                            showPagination={false}
                            className="-highlight"
                        />
                        <br />
                    </div>
                </div>
            </div>
        )
    }
}

export default ManageTournaments;