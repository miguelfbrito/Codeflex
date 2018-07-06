import React from 'react';
import ReactTable from 'react-table';

import { URL } from '../commons/Constants';
import { msToTime, parseLocalJwt, getAuthorization } from '../commons/Utils';

import PathLink from '../PathLink/PathLink';

class TournamentLeaderboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            leaderboard: []
        }
    }

    componentDidMount() {
        fetch(URL + '/api/database/Tournament/viewTournamentLeaderboard/' + this.props.match.params.tournamentName, {
            headers: { ...getAuthorization() }
        }).then(res => res.json()).then(data => this.setState({ leaderboard: data })).catch((e) => {
            console.log("error " + e);
        });
    }

    render() {

        let leaderboardData = this.state.leaderboard;
        console.log('ASDASDAD')
        console.log(leaderboardData)
        if (JSON.stringify(leaderboardData) != '[]') {
            leaderboardData = leaderboardData.sort((a, b) => { return b.score - a.score || a.totalMilliseconds - b.totalMilliseconds });
        }
        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Leaderboard" />

                    <ReactTable
                        noDataText="There is no one on the leaderboard"
                        data={leaderboardData}
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
                                            backgroundColor: (rowInfo.row.username === parseLocalJwt().username ? '#aaa' : '')
                                        }
                                    })
                                )

                            },
                            {
                                Header: "Score",
                                id: "score",
                                accessor: l => l.score.toFixed(2)
                            },
                            {
                                Header: "Time",
                                id: "time",
                                accessor: l => {
                                    if (l.totalMilliseconds == -1) {
                                        return '--/--'
                                    } else {
                                        return msToTime(l.totalMilliseconds);
                                    }
                                }
                            }

                        ]}
                        defaultPageSize={25}
                        pageSize={Math.min(this.state.leaderboard.length, 25)}
                        style={{
                        }}
                        showPagination={false}
                        className="-highlight"
                    />
                </div>
            </div>
        )
    }
}

export default TournamentLeaderboard;