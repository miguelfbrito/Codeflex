import React from 'react';
import PathLink from '../PathLink/PathLink';

class ManageTournaments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Manage tournaments" />
                </div>
            </div>
        )
    }
}

export default ManageTournaments;