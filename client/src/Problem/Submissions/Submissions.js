import React from 'react';
import ReactTable from 'react-table';
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
        fetch(URL + '/api/database/Submissions/viewByProblemNameByUserId/' + problemName + '/' + user.id).then(res => res.json()).then(data => {
            this.setState({ results: data });
            console.log('results');
            console.log(data);
        })
    }

    render() {

        return (
            <div>
                <div className="row">
                    <ReactTable
                        noDataText="You haven't submitted solutions to this problem"
                        data={this.state.results.sort((a, b) => new Date(b.date) - new Date(a.date))}
                        columns={[
                            {
                                Header: "Result",
                                id: "result",
                                accessor: r => (
                                    <p className={r.result.name === 'Correct' ? 'green-text' : 'red-text'}>{r.result.name} </p>
                                )
                            },
                            {
                                Header: "Score",
                                id: "score",
                                accessor: r => (
                                    <p>{r.score}</p>
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
                            height: Math.min(this.state.results.length * 125, 1000) + "px" // This will force the table body to overflow and scroll, since there is not enough room
                        }}
                        showPagination={false}
                        className="-highlight"
                    />


                </div>

            </div>
        );
    }
}

export default Submissions;