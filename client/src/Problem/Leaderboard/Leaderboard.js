import React from 'react';
import ReactTable from 'react-table';
import './Leaderboard.css';

import { URL } from '../../commons/Constants';
import { splitUrl, msToTime } from '../../commons/Utils';
import '../../../node_modules/react-table/react-table.css';
import '../../commons/react-table.css';

class Leaderboard extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            problemName: null,
            leaderboard: []
        }
    }

    componentDidMount() {

        let url = '';

        if(typeof this.props.pathname !== 'undefined'){
            url = splitUrl(this.props.pathname);
            const problemName = url[2];
            fetch(URL + '/api/database/Leaderboard/viewByProblemName/' + problemName).then(res => res.json())
                .then(data => { this.setState({ problemName: problemName, leaderboard: data }) })
        } else {
            console.log('On tournament')
            console.log(this.props.match.params.tournamentName);
        }

    }

    render() {

        let toRender = '';
        if (this.state.problemName != null) {

            let leaderboard = this.state.leaderboard.sort((a, b) => { return (b.score - a.score || a.durationMilliseconds - b.durationMilliseconds) });
            let currentUsername = JSON.parse(localStorage.getItem('userData')).username;

            console.log('leaderboard')
            console.log(this.state.leaderboard);
            toRender = <div>
                <ReactTable
                    noDataText="There is no one on the leaderboard"
                    data={this.state.leaderboard}
                    columns={[
                        {
                            Header: "Position",
                            id: "position",
                            accessor: l => this.state.leaderboard.indexOf(l) + 1
                        },
                        {
                            Header: "Username",
                            id: "username",
                            accessor: l => l.username,
                            getProps: (
                                (state, rowInfo, row) => ({
                                    style: {
                                        backgroundColor: (rowInfo.row.username === JSON.parse(localStorage.getItem('userData')).username ? '#aaa' : '')
                                    }
                                })
                            )

                        },
                        {
                            Header: "Language",
                            id: "language",
                            accessor: l => l.language
                        },
                        {
                            Header: "Score",
                            id: "score",
                            accessor: l => l.score
                        },
                        {
                            Header: "Time",
                            id: "time",
                            accessor: l => {
                                if (l.durationMilliseconds == -1) {
                                    return '--/--'
                                } else {
                                    return msToTime(l.durationMilliseconds);
                                }
                            }
                        }

                    ]}
                    defaultPageSize={25}
                    pageSize={Math.min(this.state.leaderboard.length, 25)}
                    style={{
                        height: Math.min(this.state.leaderboard.length * 125, 1000) + "px" // This will force the table body to overflow and scroll, since there is not enough room
                    }}
                    showPagination={false}
                    className="-highlight"
                />
            </div>
        }

        return (
            <div>
                {toRender}

            </div>
        );
    }
}

export default Leaderboard;