import React from 'react';
import ReactTable from 'react-table';
import { Router, Link } from 'react-router-dom';
import { URL } from '../../commons/Constants';
import { splitUrl, dateWithHoursAndDay, parseLocalJwt, getAuthorization } from '../../commons/Utils';
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
        fetch(URL + '/api/database/Submissions/viewByProblemNameByUsername/' + problemName + '/' + parseLocalJwt().username, {
            headers: { ...getAuthorization() }
        }).then(res => res.json()).then(data => {
            this.setState({ results: data });
            console.log('results');
            console.log(data);
        })
    }

    render() {

        return (
            <div>
                <div className="row">

                    {this.state.results.length > 0 ?
                        <ReactTable
                            noDataText="You haven't submitted solutions to this problem"
                            data={this.state.results.sort((a, b) => new Date(b.date) - new Date(a.date))}
                            columns={[
                                {
                                    Header: "Result",
                                    id: "result",
                                    accessor: r => (
                                        r.result != null ? <p className={r.result.name === 'Correct' ? 'green-text' : 'red-text'}>{r.result.name} </p> : '----'
                                    )
                                },
                                {
                                    Header: "Score",
                                    id: "score",
                                    accessor: r => (
                                        <p>{r.score.toFixed(2)}</p>
                                    )
                                },
                                {
                                    Header: "Language",
                                    id: "language",
                                    accessor: r => (
                                        <p>{r.language.name}</p>
                                    )
                                },
                                {
                                    Header: "Date",
                                    id: "Date",
                                    accessor: r => (
                                        <p>{dateWithHoursAndDay(r.date)}</p>
                                    )
                                },

                                {
                                    Header: "",
                                    id: "buttons",
                                    accessor: r => (
                                        <Link to={{ pathname: this.props.pathname + '/view-results', state: { submissionId: r.id } }}><p>View Results</p></Link>
                                    )
                                }
                            ]}
                            defaultPageSize={25}
                            pageSize={Math.min(this.state.results.length, 25)}
                            style={{
                            }}
                            showPagination={false}
                            className="-highlight"
                        /> : <p>You have not submitted solutions to this problem</p>}


                </div>

            </div>
        );
    }
}

export default Submissions;