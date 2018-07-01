import React from 'react';
import PathLink from '../../PathLink/PathLink';
import CalendarHeatmap from 'react-calendar-heatmap';
import $ from 'jquery';
import ReactTooltip from 'react-tooltip'

import { Link } from 'react-router-dom';
import { URL } from '../../commons/Constants';
import { dateWithHoursAndDay, getDatesRange, getRndInteger, textToLowerCaseNoSpaces, getAuthorization } from '../../commons/Utils';

import '../../commons/style.css';
import './ProfilePage.css';
// https://github.com/patientslikeme/react-calendar-heatmap
class ProfilePage extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            submissions: []
        }
    }

    componentDidMount() {
        fetch(URL + '/api/database/Submissions/viewByUsername/' + this.props.match.params.username, {
            headers: { ...getAuthorization() }
        }).then(res => res.json())
            .then(data => {
                console.log('Submissions')
                console.log(data);
                this.setState({ submissions: data });
            })
    }


    customOnClick = (e) => {
        console.log(e.target);
        console.log(e.target.className);
        console.log('hi');
    }

    customTitleForValue(value) {
        return value ? console.log('hi there') : null;
    }

    customTooltipDataAttrs = (value) => {
        return { 'data-tip': `${value.count} submissions on ${value.date}` };
    }


    linkToProblem = (submission) => {
        if (submission.problem.tournament != null) {
            return <Link to={'/compete/' + textToLowerCaseNoSpaces(submission.problem.tournament.name) + '/' +
                textToLowerCaseNoSpaces(submission.problem.name)}>{submission.problem.name}</Link>
        }
    }

    render() {
        // const customTooltipDataAttrs = { 'data-tip': 'hi there' };
        const data = {
            date: new Date().getTime(),
            total: 1000
        }

        let dates = getDatesRange(new Date() - (24 * 60 * 60 * 1000 * 365), new Date());
        const heatmapValues = {
        }

        let totalCount = 0;
        let obj = []
        console.log(dates.length)
        for (let i = 1; i < dates.length; i++) {
            let inObj = {};

            inObj['date'] = dates[i].getFullYear() + "-" + (dates[i].getMonth() + 1) + "-" + dates[i].getUTCDate();
            let rng = getRndInteger(0, 7);
            inObj['count'] = rng;

            totalCount += rng;
            obj.push(inObj);
        }

        console.log(obj)

        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Profile" />

                </div>
                <div className="row">
                    <div className="col-sm-3 profile-user-info no-padding no-margin">
                        <div className="profile-page-border">
                            <img id="img-profile-placeholder" src={require('../../images/user_placeholder.png')} alt="User flat image" />
                            <h3 style={{ textAlign: 'center' }}>{this.props.match.params.username}</h3>
                        </div>
                        <br />
                        <br />
                    </div>
                    <div className="col-sm-1"></div>
                    <div className="col-sm-8 no-padding profile-user-stats">
                        <div className="profile-page-border">
                            <ReactTooltip place="top" type="dark" effect="float" />
                            <h3>Activity</h3>
                            <p className="page-subtitle">{totalCount} submissions in the last year</p>
                            <CalendarHeatmap
                                values={
                                    [...obj]
                                }
                                onMouseOver={(e) => this.customOnClick(e)}
                                titleForValue={this.customTitleForValue}
                                gutterSize={2}
                                startDate={new Date() - (24 * 60 * 60 * 1000 * 365)}
                                endDate={new Date()}
                                tooltipDataAttrs={this.customTooltipDataAttrs}
                                classForValue={(value) => {
                                    if (!value) {
                                        return `color-gitlab-0`;
                                    }
                                    return `color-gitlab-${value.count} date-${value.date} count-${value.count}`;
                                }}
                            />
                        </div>
                        <br />
                        <br />
                        <div className="profile-page-border">
                            <h3>Recent Submissions</h3>
                            {this.state.submissions.length > 0 ?
                                this.state.submissions.map(s => (
                                        <div className="profile-page-subtramission">

                                        <p>Solution to {this.linkToProblem(s)} submitted on {dateWithHoursAndDay(s.date)} with a total score of {s.score}
                                            &nbsp;({s.score != 0 ? s.score / s.problem.maxScore * 100 : '0'}%).</p>
                                    </div>
                                )) : <p className="page-subtitle">No recent submissions</p>}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default ProfilePage;