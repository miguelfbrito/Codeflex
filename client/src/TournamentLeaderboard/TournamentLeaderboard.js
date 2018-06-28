import React from 'react';

import PathLink from '../PathLink/PathLink';

class TournamentLeaderboard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {
        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Leaderboard" />
                </div>
            </div>
        )
    }
}

export default TournamentLeaderboard;