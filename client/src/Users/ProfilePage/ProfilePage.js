import React from 'react';
import PathLink from '../../PathLink/PathLink';
import $ from 'jquery';
import GithubCalendar from './GithubCalendar/GithubCalendar';

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
            submissions: [],
            categories: []
        }
    }

    componentDidMount() {
        this.fetchSubmissionsByUsername();
        this.fetchPractiseCategories();
    }

    fetchSubmissionsByUsername = () => {

        fetch(URL + '/api/database/Submissions/viewByUsername/' + this.props.match.params.username, {
            headers: { ...getAuthorization() }
        }).then(res => res.json())
            .then(data => {
                console.log('Submissions')
                console.log(data);
                this.setState({ submissions: data });
            })
    }

    fetchPractiseCategories = () => {
        fetch(URL + '/api/database/PractiseCategory/view', { headers: { ...getAuthorization() } }).then(res => res.json()).then(data => { this.setState({ categories: data }) });
    }

    customTitleForValue(value) {
        return value ? console.log('hi there') : null;
    }


    getCategoryForProblem = (problem) => {
        let categories = this.state.categories;
        let category = '';
        categories.map(c => {
            c.problem.filter(p => {
                if (p.name === problem.name) {
                    category = c;
                    return true;
                }
            })
        })

        console.log('Category');
        console.log(category.name);
        return category.name;
    }

    linkToProblem = (submission) => {
        if (submission.problem.tournament != null) {
            return <Link to={'/compete/' + textToLowerCaseNoSpaces(submission.problem.tournament.name) + '/' +
                textToLowerCaseNoSpaces(submission.problem.name)}>{submission.problem.name}</Link>
        }

        this.getCategoryForProblem(submission.problem);

        return <Link to={'/practise/' + textToLowerCaseNoSpaces(this.getCategoryForProblem(submission.problem)) + '/' +
            textToLowerCaseNoSpaces(submission.problem.name)}>{submission.problem.name}</Link>

    }

    render() {

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
                            <h3>Activity</h3>
                            <GithubCalendar />

                        </div>
                        <br />
                        <br />
                        <div className="profile-page-border">
                            <h3>Recent Submissions</h3>
                            {this.state.submissions.length > 0 && this.state.categories.length > 0 ?
                                this.state.submissions.slice(0, Math.min(20, this.state.submissions.length)).map(s => (
                                    <div className="profile-page-subtramission">

                                        <p>Solution to {this.linkToProblem(s)} submitted on {dateWithHoursAndDay(s.date)} with a total score of {s.score.toFixed(2)}
                                            &nbsp;({s.score != 0 ? (s.score / s.problem.maxScore * 100).toFixed(2) : '0'}%).</p>
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