import React from 'react'
import PathLink from '../PathLink/PathLink';

import { URL } from '../commons/Constants';
import { dateWithHoursAndDay } from '../commons/Utils';

import './ListTournaments.css';

class ListTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tournaments: []
        }

        this.onClickRegister = this.onClickRegister.bind(this);
    }

    componentDidMount() {
        this.fetchTournamentList();
        setInterval(() => {
            console.log('refreshing after 60seconds');
            this.fetchTournamentList();
        }, 60000);
    }

    fetchTournamentList() {
        const userData = JSON.parse(localStorage.getItem('userData'));
        fetch(URL + '/api/database/Tournament/viewTournamentsToList/' + userData.id).then(res => res.json()).then(data => {
            console.log(data);
            this.setState({ tournaments: data });
        })
    }

    onClickRegister(tournamentId) {
        const userData = JSON.parse(localStorage.getItem('userData'));
        const data = {
            user: { id: userData.id },
            tournament: { id: tournamentId }
        }
        fetch(URL + '/api/database/Tournament/registerUser', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log("logging tournaments")
            console.log(data);
            this.setState({ tournaments: data });
        })
    }

    render() {

        let tournaments = this.state.tournaments;

        let availableTournaments = '';
        let archivedTournaments = '';

        if (typeof tournaments.availableTournaments !== 'undefined') {
            // TODO @Review Is this sorting working
            availableTournaments = tournaments.availableTournaments.sort((a, b) => a.tournament.endingDate - b.tournament.endingDate).map(t => (
                <div className="tournament-container">
                    <div key={t.tournament.id} className="col-sm-10 col-md-10 col-xs-12">
                        <p>{t.tournament.name}</p>
                        <p>{t.tournament.description}</p>
                        {new Date(t.tournament.startingDate) > new Date().getTime() ? <p className="green-text">Starting at {dateWithHoursAndDay(t.tournament.startingDate)}</p> : ''}
                    </div>
                    <div className="col-sm-2 col-md-2 col-xs-4 button-container-tournaments" >
                        <input type="submit" className="btn btn-primary" value={
                            t.registered ? (new Date(t.tournament.startingDate) >= new Date().getTime() ? 'Starting soon' : 'Enter') : 'Sign up'
                        }
                            onClick={() => this.onClickRegister(t.tournament.id)} />
                    </div>
                </div>
            ))
        }

        if (typeof tournaments.archivedTournaments !== 'undefined') {
            console.log('teste')
            archivedTournaments = tournaments.archivedTournaments.map(t => (
                <div key={t.tournament.id} className="tournament-container">
                    <p>{t.tournament.name}</p>
                    <input type="submit" className="btn btn-primary" style={{ float: 'right' }} value="View Problems" />
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