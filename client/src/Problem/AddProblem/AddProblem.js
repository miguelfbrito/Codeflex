import React from 'react';
import PathLink from '../../PathLink/PathLink'
import { URL } from '../../commons/Constants';
import { splitUrl } from '../../commons/Utils';

class AddProblem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Add problem" />
                </div>
            </div>
        )
    }
}

export default AddProblem;