import React from 'react';
import PathLink from '../PathLink/PathLink';
import DatePicker from 'react-datepicker';
import moment from 'moment';

import { textToLowerCaseNoSpaces} from '../commons/Utils';
import { Link } from 'react-router-dom';
import { URL } from '../commons/Constants';

import '../commons/style.css';
import '../../node_modules/react-datepicker/dist/react-datepicker.css';
import './CreateTournament.css';

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

    createTournament() {
        console.log('Creating tournament');
        console.log(this.state);
        const data = {
            name: this.state.name,
            description: this.state.description,
            startingDate: this.state.startDate,
            endingDate: this.state.endDate,
            code: this.state.privateCode,
            owner: { id: JSON.parse(localStorage.getItem('userData')).id }
        }

        fetch(URL + '/api/database/tournament/add', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => res.json()).then(data => {
            console.log(data);
        });

    }


    render() {

        const dateFormat = "HH:mm DD/MM/YYYY";

        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Create tournament" />
                    <div className="tournament-creation-container">
                        <div className="tournament-creation-top">
                            <h3 className="page-subtitle">Host your own tournament at Codeflex and compete with your friends. You may as well suggest a tournament to be featured on the Tournament page.</h3>
                            <p className="page-subtitle">Start by filling the data below.</p>
                            <br />
                        </div>
                        <form className="form-horizontal">
                            <div className="form-group">
                                <label for="tournamentName" className="col-sm-1 control-label" >Name</label>
                                <div className="col-sm-5">
                                    <input type="tournamentName" className="form-control" id="name" onChange={this.handleChange} placeholder="Tournament name" />
                                </div>
                            </div>
                            <div className="form-group">
                                <label for="tournamentDescription" className="col-sm-1 control-label">Description</label>
                                <div className="col-sm-5">
                                    <textarea className="form-control" id="description" rows="5" onChange={this.handleChange} placeholder="Short tournament description"></textarea>
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
                                <div className="col-sm-5" style={{ display: 'inline-block' }}>
                                    <input type="code" className="form-control" id="privateCode" onChange={this.handleChange} placeholder="Private code" />
                                    <p className='tournament-extra-info'>Add a private code that you can share with your friends to register on the tournament.</p>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-offset-1 col-sm-10">
                                    <Link to={"/compete/manage-tournaments/" + textToLowerCaseNoSpaces(this.state.name)}><input type="button" id="btn-create" className="btn btn-codeflex" onClick={this.createTournament} value="Create" /></Link>
                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        )
    }
}



export default CreateTournament;