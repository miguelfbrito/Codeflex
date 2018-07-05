import React from 'react';
import ReactTable from 'react-table';
import PathLink from '../PathLink/PathLink';

import { URL } from '../commons/Constants';
import { parseLocalJwt, getAuthorization } from '../commons/Utils';
import '../../node_modules/react-table/react-table.css';

class GlobalLeaderboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            users: []
        }
    }

    componentDidMount() {
        fetch(URL + '/api/database/Users/viewAllWithLessInfo', {
            headers : {...getAuthorization()}
        }).then(res => res.json())
            .then(data => {
                console.log("Dataaaa")
                console.log(data);
                this.setState({ users: data })
            })
    }

    render() {

        const orderedUsers = this.state.users.sort((a, b) => { return b.rating - a.rating });
        const renderTable = <div>
            <ReactTable
                noDataText="There is no one on the leaderboard"
                data={orderedUsers}
                columns={[
                    {
                        Header: "Position",
                        id: "position",
                        accessor: u => this.state.users.indexOf(u) + 1
                    },
                    {
                        Header: "Username",
                        id: "username",
                        accessor: u => u.username,
                        getProps: (
                            (state, rowInfo, row) => ({
                                style: {
                                    backgroundColor: (rowInfo.row.username === parseLocalJwt().username ? '#aaa' : '')
                                }
                            })
                        )

                    },
                    {
                        Header: "Rating",
                        id: "rating",
                        accessor: u => Math.round(u.rating)
                    }


                ]}
                defaultPageSize={25}
                pageSize={Math.min(this.state.users.length, 25)}
                style={{
                }}
                showPagination={false}
                className="-highlight"
            />
        </div>
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Overall Leaderboard" />
                    {renderTable}
                </div>
            </div>
        )
    }
}

export default GlobalLeaderboard;