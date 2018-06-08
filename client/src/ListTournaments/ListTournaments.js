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
                <div key={t.tournament.id} className="tournament-container">
                    <p>{t.tournament.name}</p>
                    <p>{t.tournament.description}</p>
                    <input type="submit" className="btn btn-primary" style={{ float: 'right' }} value={t.registered ? 'Solve problem' : 'Register!'} />
                </div>
            ))
        }

        if (typeof tournaments.archivedTournaments !== 'undefined') {
            console.log('teste')
            archivedTournaments = tournaments.archivedTournaments.map(t => (
                <div key={t.tournament.id} className="tournament-container">
                    <p>{t.tournament.name}</p>
                    <input type="submit" className="btn btn-primary" style={{ float: 'right' }} />
                </div>
            ))
        }

        return (
            <div className="container">
                <div className="row"> <PathLink path={this.props.location.pathname} title="Tournaments" />
                    <div className="col-sm-12 both-categories-container">
                        <h2>Available</h2>
                        <hr style={{ borderWidth: '1px' }} />
                        <div className="tournaments-container">
                            {availableTournaments}
                        </div>
                        <h2>Finished</h2>
                        <hr style={{ borderWidth: '1px' }} />
                        <div className="tournaments-container">
                            {archivedTournaments}
                        </div>
                    </div>
                </div>
            </div >
        )
    }

}

export default ListTournaments;