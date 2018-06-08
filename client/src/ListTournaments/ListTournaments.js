import React from 'react'
import PathLink from '../PathLink/PathLink';

import { URL } from '../commons/Constants';

import './ListTournaments.css';

class ListTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tournaments: []
        }
    }

    componentDidMount() {
        let userData = JSON.parse(localStorage.getItem('userData'));
        fetch(URL + '/api/database/Tournament/viewTournamentsToList/' + userData.id).then(res => res.json()).then(data => {
            console.log(data);
            this.setState({ tournaments: data });
        })
    }

    render() {

        let tournaments = this.state.tournaments;

        let availableTournaments = '';
        let archivedTournaments = '';

        if (typeof tournaments.availableTournaments !== 'undefined') {
            console.log('teste')
            availableTournaments = tournaments.availableTournaments.map(t => (
                <div>
                    <p>
                        {t.tournament.name}
                    </p>
                </div>
            ))
        }

        if (typeof tournaments.archivedTournaments !== 'undefined') {
            console.log('teste')
            archivedTournaments = tournaments.archivedTournaments.map(t => (
                <div>
                    <p>
                        {t.tournament.name}
                    </p>
                </div>
            ))
        }




        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Tournaments" />
                </div>
                <div>
                    <div>
                        <h2>Available</h2>
                        {availableTournaments}
                    </div>
                    <div>
                        <h2>Archived</h2>
                        {archivedTournaments}
                    </div>
                </div>
            </div>
        )
    }

}

export default ListTournaments;