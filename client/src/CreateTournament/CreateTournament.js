import React from 'react';
import PathLink from '../PathLink/PathLink';
import DatePicker from 'react-datepicker';
import moment from 'moment';

import '../../node_modules/react-datepicker/dist/react-datepicker.css';
import './CreateTournament.css';

class CreateTournament extends React.Component {
    constructor(props) {
        super(props);
        this.state = { startDate: moment(), endDate: moment() }

        this.handleChangeStart = this.handleChangeStart.bind(this);
        this.handleChangeEnd = this.handleChangeEnd.bind(this);
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
    render() {

        const dateFormat = "HH:mm DD/MM/YYYY";

        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Create tournament" />
                    <p>Host your own tournament at Codeflex and compete with your friends.</p>
                    <p>Start by filling the data below.</p>
                    <br />
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label for="tournamentName" class="col-sm-1 control-label">Name</label>
                            <div class="col-sm-5">
                                <input type="tournamentName" class="form-control" id="tournamentName" placeholder="Tournament name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tournamentDescription" class="col-sm-1 control-label">Description</label>
                            <div class="col-sm-5">
                                <textarea class="form-control" rows="5" placeholder="Short tournament description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="startingDate" className="col-sm-1 control-label">Starting Date</label>
                            <div class="col-sm-5 date-picker">
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
                        <div class="form-group">
                            <label for="endingDate" class="col-sm-1 control-label">Ending Date</label>
                            <div class="col-sm-5 date-picker">
                                <DatePicker
                                    dateFormat={dateFormat}
                                    selected={this.state.endDate}
                                    selectsEnd
                                    showTimeSelect
                                    timeIntervals={15}
                                    startDate={this.state.startDate}
                                    endDate={this.state.endDate}
                                    onChange={this.handleChangeEnd}
                                    minDate={this.state.startDate}
                                    maxDate={moment().add(365, "days")}
                                />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-1 col-sm-10">
                                <button type="submit" class="btn btn-default">Sign in</button>
                            </div>
                        </div>
                    </form>


                </div>
            </div>
        )
    }
}



export default CreateTournament;