import React from 'react';
import PathLink from '../PathLink/PathLink';
import DatePicker from 'react-datepicker';
import moment from 'moment';

import { textToLowerCaseNoSpaces, splitUrl, parseLocalJwt, getAuthorization } from '../commons/Utils';
import { Link } from 'react-router-dom';
import { URL } from '../commons/Constants';
import { ToastContainer, toast } from 'react-toastify';

import 'react-toastify/dist/ReactToastify.css';
import '../commons/style.css';
import '../../node_modules/react-datepicker/dist/react-datepicker.css';
import './CreateTournament.css';
import { areStringEqual, validateEmail, validateLength, validateStringChars, isStringEmpty } from '../commons/Validation';

class CreateTournament extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            startDate: moment(),
            endDate: moment(),
            name: '',
            description: '',
            privateCode: ''
        }

        this.handleChangeStart = this.handleChangeStart.bind(this);
        this.handleChangeEnd = this.handleChangeEnd.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.createTournament = this.createTournament.bind(this);
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

            if(validateLength(data.description, 50, 1000)){
                toast.error("Description must be between 50 and 1000 characters")
                return false;
            }

            if (new Date(data.endingDate).getTime() - new Date(data.startingDate).getTime() < 60 * 5 * 1000) {
                toast.error("Tournament has to last for at least 5 minutes")
                return false;
            }
        }

        return true;
    }

    createTournament() {
        console.log('Creating tournament');
        const data = {
            name: this.state.name,
            description: this.state.description,
            startingDate: this.state.startDate,
            endingDate: this.state.endDate,
            code: this.state.privateCode,
            owner: { username: parseLocalJwt().username }
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
            } else if(res.status === 200){
                return res.json();
            }
        }).then(data => {
            console.log(data);
            window.location.href = "/compete/manage-tournaments/" + textToLowerCaseNoSpaces(this.state.name);
        }).catch((e) => {
            console.log('errro' + e)
        })

    }


    render() {

        const dateFormat = "HH:mm DD/MM/YYYY";

        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Create tournament" />
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
                                    <input type="tournamentName" className="form-control" id="name" onChange={this.handleChange} placeholder="Tournament name" />
                                    <small className="fill-info">Length between 5 and 50 characters. No special characters except ':' and '_'</small>
                                </div>
                            </div>
                            <div className="form-group">
                                <label for="tournamentDescription" className="col-sm-1 control-label">Description</label>
                                <div className="col-sm-10">
                                    <textarea className="form-control" id="description" rows="6" onChange={this.handleChange} placeholder="Short tournament description"></textarea>
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
                            <div className="form-group">
                                <label htmlFor="tournamentName" className="col-sm-1 control-label">Private Code</label>
                                <div className="col-sm-4" style={{ display: 'inline-block' }}>
                                    <input type="code" className="form-control" id="privateCode" onChange={this.handleChange} placeholder="Private code" />
                                    <small className="fill-info"> Add a private code that you can share with your friends to register on the tournament</small>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-offset-1 col-sm-10">
                                    <input type="button" id="btn-create" className="btn btn-codeflex" onClick={this.createTournament} value="Create" />
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