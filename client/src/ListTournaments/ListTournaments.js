import React from 'react'
import PathLink from '../PathLink/PathLink';
import { Link } from 'react-router-dom';
import Popup from '../Popup/Popup';

import { Redirect } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { dateWithHoursAndDay, textToLowerCaseNoSpaces, getTimeHoursMins } from '../commons/Utils';

import './ListTournaments.css';

class ListTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tournaments: [],
            redirect: { now: false, path: '/' }
        }

        this.onClickRegister = this.onClickRegister.bind(this);
        this.onClickTournament = this.onClickTournament.bind(this);
    }

    componentDidMount() {
        this.fetchTournamentList();
        window.updateEveryMinute = setInterval(() => {
            this.fetchTournamentList();
        }, 60000);
    }

    componentWillUnmount() {
        clearInterval(window.updateEveryMinute);
    }

    fetchTournamentList() {
        const userData = JSON.parse(localStorage.getItem('userData'));
        fetch(URL + '/api/database/Tournament/viewTournamentsToList/' + userData.id).then(res => res.json()).then(data => {
            console.log(data);
            this.setState({ tournaments: data });
        })
    }

    onClickTournament(clickType, tournamentId, tournamentName) {
        if (clickType === 'Sign Up') {
            this.onClickRegister(tournamentId);
            console.log('sign')
        } else if (clickType === 'Starting soon') {
        } else if (clickType === 'Enter') {
            console.log(tournamentName);
            this.setState({ redirect: { now: true, path: '/compete/' + textToLowerCaseNoSpaces(tournamentName) } })
        }
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
                        {new Date(t.tournament.startingDate).getTime() > new Date().getTime() ? <p className="green-text">Starting at {dateWithHoursAndDay(t.tournament.startingDate)}</p>
                            : <p className="red-text">Finishing in {getTimeHoursMins(new Date(t.tournament.endingDate).getTime() - new Date().getTime())}</p>}
                    </div>
                    <div className="col-sm-2 col-md-2 col-xs-4 button-container-tournaments" >
                        <input type="submit" className="btn btn-codeflex" value={
                            t.registered ? (new Date(t.tournament.startingDate).getTime() >= new Date().getTime() ? 'Starting soon' : 'Enter') : 'Sign Up'
                        }
                            onClick={(e) => this.onClickTournament(e.target.value, t.tournament.id, t.tournament.name)} />
                    </div>
                </div>
            ))
        }

        if (typeof tournaments.archivedTournaments !== 'undefined') {
            console.log('teste')
            archivedTournaments = tournaments.archivedTournaments.map(t => (
                <div className="tournament-container">
                    <div key={t.tournament.id} className="col-sm-10 col-md-10 col-xs-12">
                        <p>{t.tournament.name}</p>
                        <p>{t.tournament.description}</p>
                    </div>
                    <div className="col-sm-2 col-md-2 col-xs-4 button-container-tournaments" >
                        <input type="submit" className="btn btn-codeflex" value="View Problems" />
                    </div>
                </div>
            ))
        }

        const PopupInformation = () => (
            <div>
                <h2 style={{ color: 'white', margin: 'auto' }}>teste</h2>
                <p style={{ color: 'white', margin: 'auto', textAlign: 'center' }}>fasfasfafs</p>
            </div>
        );


        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Tournaments" />
                    {/*<Popup timeoutClose={1250}>
                        <PopupInformation />
        </Popup>*/}
                    <div className="col-sm-12 both-categories-container">
                        <div style={{ float: 'right', textAlign: 'right', marginTop: '-10px' }}>
                            <Link to="/compete/create-tournament" > <p>Create Tournament</p></Link>
                            <Link to="/compete/manage-tournaments"><p >Manage Tournaments</p></Link>
                        </div>
                        <h2 style={{ fontFamily: 'Roboto Condensed' }}>Available</h2>
                        <hr style={{ borderWidth: '2px', borderTop: 'none', borderLeft: 'none', borderRight: 'none' }} />
                        <div className="tournaments-container">
                            {availableTournaments}
                        </div>
                        <h2 style={{ fontFamily: 'Roboto Condensed' }}>Finished</h2>
                        <hr style={{ borderWidth: '2px', borderTop: 'none', borderLeft: 'none', borderRight: 'none' }} />
                        <div className="tournaments-container">
                            {archivedTournaments}
                        </div>
                    </div>

                    {this.state.redirect.now ? <Redirect to={this.state.redirect.path} /> : ''}
                </div>
            </div >
        )
    }

}

export default ListTournaments;