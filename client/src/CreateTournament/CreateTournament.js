import React from 'react';
import PathLink from '../PathLink/PathLink';
import DatePicker from 'react-datepicker';
import moment from 'moment';

import { textToLowerCaseNoSpaces, splitUrl, parseLocalJwt, getAuthorization, isContentManager } from '../commons/Utils';
import { Link } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { ToastContainer, toast } from 'react-toastify';

import 'react-toastify/dist/ReactToastify.css';
import '../commons/style.css';
import '../../node_modules/react-datepicker/dist/react-datepicker.css';
import './CreateTournament.css';
import { areStringEqual, validateEmail, validateLength, validateStringChars, isStringEmpty, validateStringCharsNoSpaces } from '../commons/Validation';
import PageNotFound from '../PageNotFound/PageNotFound';

class CreateTournament extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            startDate: moment(),
            endDate: moment(),
            name: '',
            description: '',
            privateCode: '',
            location: '',
            registered: true,
            userIsOwner: true
        }

        this.handleChangeStart = this.handleChangeStart.bind(this);
        this.handleChangeEnd = this.handleChangeEnd.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.createTournament = this.createTournament.bind(this);
    }

    componentDidMount() {

        if (!isContentManager()) {
            this.isUserTournamentOwner();
            this.isUserRegistered();
        }

        this.setState({ location: splitUrl(this.props.location.pathname)[0] });
        let url = splitUrl(this.props.location.pathname);
        if (url[3] === 'edit') {
            this.fetchTournament();
        }


    }

    isUserRegistered = () => {
        fetch(URL + '/api/database/Rating/isUserRegisteredInTournamentTest/' + parseLocalJwt().username + "/" + this.props.match.params.tournamentName, {
            headers: { ...getAuthorization() }
        }).then(res => {
            if (res.status === 200) {
                this.setState({ registered: true });
            } else {
                this.setState({ registered: false });
            }
        })

    }

    isUserTournamentOwner = () => {
        fetch(URL + '/api/database/tournament/isUserTournamentOwner/' + this.props.match.params + "/" + parseLocalJwt().username, {
            headers: new Headers({ ...getAuthorization() })
        }).then(res => { if (res.status === 200) { this.setState({ userIsOwner: true }); } else { this.setState({ userIsOwner: false }); } })
    }

    fetchTournament = () => {
        fetch(URL + '/api/database/tournament/viewByName/' + this.props.match.params.tournamentName, {
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()
        ).then(data => {
            console.log('hi');
            console.log(data);
            this.setState({
                startDate: moment(data.startingDate),
                endDate: moment(data.endingDate),
                name: data.name,
                description: data.description,
                privateCode: data.code
            })

        })
    }

    handleChangeStart(date) {
        this.setState({
            startDate: date
        });
    }

    handleChangeEnd(date) {
        this.setState({
            endDate: date
        });
    }

    handleChange(e) {
        this.setState({ [e.target.id]: e.target.value });
    }

    validateTournament = (data) => {

        if (isStringEmpty(data.name) || isStringEmpty(data.description)) {
            toast.error("Fill in all the fields", { autoClose: 2500 })
            return false;
        } else {
            if (!validateLength(data.name, 5, 50)) {
                toast.error("Tournament name must be between 5 and 50 characters")
                return false;
            } else {
                if (!validateStringChars(data.name)) {
                    toast.error("Name can only contain letters, numbers and _")
                    return false;
                }
            }

            if (validateLength(data.description, 50, 1000)) {
                toast.error("Description must be between 50 and 1000 characters")
                return false;
            }

            if (new Date(data.endingDate).getTime() - new Date(data.startingDate).getTime() < 60 * 5 * 1000) {
                toast.error("Tournament has to last for at least 5 minutes")
                return false;
            }

            if (this.state.location === 'compete' && !validateLength(data.code, 5, 50) && !validateStringCharsNoSpaces(data.code)) {
                toast.error("Invalid code, it must be between 5 and 50 in length and no special characters.")
                return false;
            }
        }

        return true;
    }

    updateTournament = () => {
        let data = {
            name: this.state.name,
            description: this.state.description,
            startingDate: this.state.startDate,
            endingDate: this.state.endDate,
            code: this.state.privateCode,
            owner: { username: parseLocalJwt().username }
        }

        fetch(URL + '/api/database/tournament/update', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(() => {
            if (this.onCompete()) {
                window.location.href = "/compete/manage-tournaments"
            } else {
                window.location.href = "/manage/tournaments"
            }
        })

    }

    createTournament() {
        console.log('Creating tournament');
        let data = {
            name: this.state.name,
            description: this.state.description,
            startingDate: this.state.startDate,
            endingDate: this.state.endDate,
            code: this.state.privateCode,
            owner: { username: parseLocalJwt().username }
        }

        if (this.state.location === 'manage') {
            data = {
                ...data,
                showWebsite: true
            }
        }

        console.log(data);
        if (!this.validateTournament(data)) {
            return;
        }

        fetch(URL + '/api/database/tournament/add', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => {
            if (res.status === 409) {
                toast.error("Tournament name already in use", { autoClose: 2500 })
                throw new Error("Name in use");
            } else if (res.status === 226) {
                toast.error("Private code already in use", { autoClose: 2500 })
                throw new Error("Code in use");
            } else if (res.status === 200) {
                return res.json();
            }
        }).then(data => {
            console.log(data);
            if (this.onCompete) {
                window.location.href = "/compete/manage-tournaments/" + textToLowerCaseNoSpaces(this.state.name);
            } else {
                window.location.href = "/manage/tournaments/" + textToLowerCaseNoSpaces(this.state.name);
            }
        }).catch((e) => {
            console.log('errro' + e)
        })

    }

    onCompete = () => {
        let url = splitUrl(this.props.location.pathname);
        if (url[0] === 'compete') {
            return true;
        }
        return false;
    }

    pathLinkTitle = () => {
        let url = splitUrl(this.props.location.pathname);
        if (url[3] === 'edit') {
            return "Edit"
        }
        return "Create Tournament"
    }

    onSubmit = () => {
        let url = splitUrl(this.props.location.pathname);
        if (typeof url[3] != "undefined" && url[3] === 'edit') {
            this.updateTournament();
        } else {
            this.createTournament();
        }

    }

    render() {

        const dateFormat = "HH:mm DD/MM/YYYY";

        if (!this.state.registered || !this.state.userIsOwner) {
            return (
                <PageNotFound />
            )
        }

        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title={this.pathLinkTitle()} />
                    <div className="tournament-creation-container">
                        <div className="tournament-creation-top">
                            <h3 className="page-subtitle">Host your own tournament at Codeflex and compete with your friends.</h3>
                            <p className="page-subtitle">Start by filling the data below.</p>
                            <br />
                        </div>
                        <form className="form-horizontal">
                            <div className="form-group">
                                <label for="tournamentName" className="col-sm-1 control-label" >Name</label>
                                <div className="col-sm-5">
                                    <input type="tournamentName" className="form-control" id="name" onChange={this.handleChange} value={this.state.name} placeholder="Tournament name" />
                                    <small className="fill-info">Length between 5 and 50 characters. No special characters except ':' and '_'</small>
                                </div>
                            </div>
                            <div className="form-group">
                                <label for="tournamentDescription" className="col-sm-1 control-label">Description</label>
                                <div className="col-sm-10">
                                    <textarea className="form-control" id="description" rows="6" onChange={this.handleChange} value={this.state.description} placeholder="Short tournament description"></textarea>
                                    <small className="fill-info">Length between 50 and 1000 characters</small>
                                </div>
                            </div>
                            <div className="form-group">
                                <p>Date in GMT+1</p>
                                <label for="startingDate" className="col-sm-1 control-label">Starting Date</label>
                                <div className="col-sm-2 date-picker">
                                    <DatePicker
                                        dateFormat={dateFormat}
                                        selected={this.state.startDate}
                                        selectsStart
                                        showTimeSelect
                                        timeIntervals={15}
                                        startDate={this.state.startDate}
                                        endDate={this.state.endDate}
                                        onChange={this.handleChangeStart}
                                        minDate={moment()}
                                        maxDate={moment().add(60, "days")}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="endingDate" className="col-sm-1 control-label">Ending Date</label>
                                <div className="col-sm-5 date-picker">
                                    <DatePicker
                                        dateFormat={dateFormat}
                                        selected={this.state.endDate}
                                        selectsEnd
                                        showTimeSelect
                                        timeIntervals={4}
                                        startDate={this.state.startDate}
                                        endDate={this.state.endDate}
                                        onChange={this.handleChangeEnd}
                                        minDate={this.state.startDate}
                                        maxDate={moment().add(365, "days")}
                                    />
                                </div>
                            </div>
                            {this.state.location === 'compete' ?
                                <div className="form-group">
                                    <label htmlFor="tournamentName" className="col-sm-1 control-label">Private Code</label>
                                    <div className="col-sm-4" style={{ display: 'inline-block' }}>
                                        <input type="code" className="form-control" id="privateCode" onChange={this.handleChange} value={this.state.privateCode} placeholder="Private code" />
                                        <small className="fill-info">Add a private code that you can share with your friends to register on the tournament</small>
                                    </div>
                                </div> : ''}
                            <div className="form-group">
                                <div className="col-sm-offset-1 col-sm-11 col-xs-12 col-md-12" style={{ textAlign: 'left' }}>
                                    <input type="button" id="btn btn-primary" className="btn btn-codeflex" onClick={this.onSubmit} value="Save" />
                                </div>
                            </div>
                        </form>

                        <ToastContainer
                            position="top-right"
                            autoClose={5500}
                            hideProgressBar={false}
                            closeOnClick
                            rtl={false}
                            pauseOnVisibilityChange
                            draggable
                            pauseOnHover
                            style={{ fontFamily: "'Roboto', sans-serif", fontSize: '12pt', letterSpacing: '1px', textAlign: 'center' }}
                        />

                    </div>
                </div>
            </div>
        )
    }
}



export default CreateTournament;