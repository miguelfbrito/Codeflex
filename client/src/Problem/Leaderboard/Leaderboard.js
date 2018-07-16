import React from 'react';
import ReactTable from 'react-table';
import './Leaderboard.css';

import { URL } from '../../commons/Constants';
import { splitUrl, msToTime, getAuthorization, parseLocalJwt } from '../../commons/Utils';
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
        const problemName = splitUrl(this.props.pathname)[2];
        fetch(URL + '/api/database/Leaderboard/viewByProblemName/' + problemName, { headers: { ...getAuthorization() } }).then(res => res.json())
            .then(data => { this.setState({ problemName: problemName, leaderboard: data }) })
    }

    render() {

        let toRender = '<div></div>';
        if (this.state.problemName != null) {

            let leaderboard = this.state.leaderboard.sort((a, b) => { return (b.score - a.score || a.durationMilliseconds - b.durationMilliseconds) });
            let currentUsername = parseLocalJwt().username;

            console.log('leaderboard')
            console.log(this.state.leaderboard);

            toRender =
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
                                        backgroundColor: (rowInfo.row.username === currentUsername ? '#aaa' : '')
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
                            accessor: l => l.score.toFixed(2)
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
                    }}
                    showPagination={false}
                    className="-highlight"
                />
        }

        return (
            <div>

                {this.state.leaderboard.length > 0 ?
                    <div>
                        {toRender}
                    </div>
                    : <h3 className="no-data-h3">There are no users on the leaderboard.</h3>
                }

            </div>
        );
    }
}

export default Leaderboard;