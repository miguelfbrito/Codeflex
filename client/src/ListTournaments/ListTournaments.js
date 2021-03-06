import React from 'react'
import PathLink from '../PathLink/PathLink';
import { Link } from 'react-router-dom';
import Popup from '../Popup/Popup';

import { Redirect } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { dateWithHoursAndDay, textToLowerCaseNoSpaces, getTimeHoursMins, parseLocalJwt, getAuthorization, getTimeHoursMinsText } from '../commons/Utils';

import './ListTournaments.css';

class ListTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tournaments: [],
            redirect: { now: false, path: '/' },
            displayInputCode: false,
            privateCode: ''
        }

        this.onClickRegister = this.onClickRegister.bind(this);
        this.onClickTournament = this.onClickTournament.bind(this);
    }

    componentDidMount() {
        this.fetchTournamentList();
        window.updateEveryMinute = setInterval(() => {
            this.fetchTournamentList();
        }, 10000);
    }

    componentWillUnmount() {
        clearInterval(window.updateEveryMinute);
    }

    fetchTournamentList() {
        fetch(URL + '/api/database/Tournament/viewTournamentsToList/' + parseLocalJwt().username, {
            headers: {...getAuthorization()}
        }).then(res => res.json()).then(data => {
            console.log(data);
            this.setState({ tournaments: data });
        })
    }

    onInputChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
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
        const data = {
            user: { username : parseLocalJwt().username},
            tournament: { id: tournamentId }
        }

        this.registerUser(data);
    }

    registerUser = (data) => {

        fetch(URL + '/api/database/Tournament/registerUser', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log("logging tournaments")
            console.log(data);
            this.setState({ tournaments: data });
        })
    }

    onClickPrivateTournament = () => {
        console.log('private')
        this.setState({ displayInputCode: !this.state.displayInputCode });
    }

    onClickEnterPrivateTournament = () => {
        const data = {
            user: {username: parseLocalJwt().username},
            tournament: { code: this.state.privateCode }

        }

        this.registerUser(data);
        this.setState({displayInputCode : false});
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
                        <p style={{fontSize:'15pt'}}>{t.tournament.name}</p>
                        <p>{t.tournament.description}</p>
                        {new Date(t.tournament.startingDate).getTime() > new Date().getTime() ? <p style={{fontSize:'11pt'}} className="green-text">Starting at {dateWithHoursAndDay(t.tournament.startingDate)}</p>
                            : <p style={{fontSize:'11pt'}} className="red-text">Finishing in {getTimeHoursMinsText(new Date(t.tournament.endingDate).getTime() - new Date().getTime())}</p>}
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
                        <p style={{fontSize:'15pt'}}>{t.tournament.name}</p>
                        <p>{t.tournament.description}</p>
                    </div>
                    <div className="col-sm-2 col-md-2 col-xs-4 button-container-tournaments" >
                        <Link to={"/compete/" + textToLowerCaseNoSpaces(t.tournament.name)}> <input type="submit" className="btn btn-codeflex" value="View Problems" /></Link>
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
                        <br />
                        <div style={{ float: 'right', textAlign: 'right', marginTop: '-30px' }}>
                            {this.state.displayInputCode ? <div className="private-code">
                                <input type="text" className="textbox-no-radius" style={{ height: '25px', marginBottom: '7px' }} placeholder="Tournament Private Code"
                                    name="privateCode" onChange={(e) => this.onInputChange(e)} value={this.state.privateCode} />
                                <input style={{maxHeight:'25px'}} type="button" className="" value="Register" onClick={this.onClickEnterPrivateTournament} />
                            </div> :
                                <a>
                                    <p onClick={this.onClickPrivateTournament} style={{cursor:'pointer'}}>Register in private tournament</p>
                                </a>}
                            <Link to="/compete/create-tournament"> <p>Create tournament</p></Link>
                            <Link to="/compete/manage-tournaments"><p >Manage tournaments</p></Link>
                        </div>
                        <h2 style={{ fontFamily: 'Roboto Condensed' }}>Available</h2>
                        <hr style={{ border: '0 none', height: '2px', color: '#6a44f', backgroundColor:'#6a44ff'}} />
                        <div className="tournaments-container">
                            {availableTournaments}
                        </div>
                        <h2 style={{ fontFamily: 'Roboto Condensed' }}>Finished</h2>
                        <hr style={{ border: '0 none', height: '2px', color: '#6a44f', backgroundColor:'#6a44ff'}} />
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