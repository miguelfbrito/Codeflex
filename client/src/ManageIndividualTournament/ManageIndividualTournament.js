import React from 'react';
import PathLink from '../PathLink/PathLink'
import {splitUrl} from '../commons/Utils';

class ManageIndividualTournament extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        return ( 
            <div className="container">
            <div className="row">
                <PathLink path={this.props.location.pathname} title={splitUrl(this.props.match.url)[2]}/>
            </div> 
            </div>
         )
    }
}

export default ManageIndividualTournament;