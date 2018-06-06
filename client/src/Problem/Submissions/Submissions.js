import React from 'react';
import { Router, Link } from 'react-router-dom';
import { URL } from '../../commons/Constants';
import { splitUrl, dateWithHoursAndDay } from '../../commons/Utils';
import './Submissions.css';

class Submissions extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            results: []
        }
    }

    componentDidMount() {
        let problemName = splitUrl(this.props.pathname)[2];
        let user = JSON.parse(localStorage.getItem('userData'));
        fetch(URL + '/api/database/Submissions/viewByProblemNameByUserId/' + problemName+'/'+user.id).then(res => res.json()).then(data => {
            this.setState({ results: data });
            console.log('results');
            console.log(data);
        })

    }

    render() {

        // TODO : corrigir esta representação de dados para MOBILE

        return (
            <div>
                <div className="row">
                    <div className="submission-title-container">
                        <div className="col-sm-2">
                            <p>Result</p>
                        </div>
                        <div className="col-sm-2">
                            <p>Score</p>
                        </div>
                        <div className="col-sm-2">
                            <p>Language</p>
                        </div>
                        <div className="col-sm-2">
                            <p>Date</p>
                        </div>
                        <div className="col-sm-2">
                            <p> &nbsp;</p>
                        </div>
                        <div className="col-sm-2">
                            <p> &nbsp;</p>
                        </div>
                    </div>
                </div>
                <div className="row">
                    {this.state.results.sort((a, b) => new Date(b.date) - new Date(a.date)).map(r => (
                        <div className="submission-container">
                            <div className="col-sm-2">
                                <p className={r.result.name === 'Correct' ? 'green-text' : 'red-text'}>{r.result.name}</p>
                            </div>
                            <div className="col-sm-2">
                                <p>
                                    {r.score}
                                </p>
                            </div>
                            <div className="col-sm-2"><p>
                                {r.language.name}
                            </p>
                            </div>
                            <div className="col-sm-2">
                                <p>
                                    {dateWithHoursAndDay(r.date)}
                                </p>
                            </div>
                            <div className="col-sm-2">
                                <Link to={{pathname : this.props.pathname +'/view-results', state : {submissionId : r.id}}}><p>View Results</p></Link>
                            </div>
                            <div className="col-sm-2">
                                <p>View Submissions</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

export default Submissions;