import React, { Component } from 'react';
import './ViewResults.css'
import { textToLowerCaseNoSpaces, splitUrl } from '../commons/Utils';
import { URL } from '../commons/Constants';
import PathLink from '../PathLink/PathLink';

class ViewResults extends Component {

    constructor(props) {
        super(props);
        this.state = {
        }
    }



    render() {
        return (
            <div className="container">
                <div className="row ">
                    <PathLink path={this.props.location.pathname} title="View Results"/>                   
                </div>
            </div>
        );
    }
}

export default ViewResults;