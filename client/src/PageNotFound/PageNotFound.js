import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './PageNotFound.css';

class PageNotFound extends Component {
    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm-12 col-md-12 col-xs-12 not-found">
                        <h1>404</h1>
                        <h2>Nothing to see here</h2>
                        <Link to="/"><input type="button" className="btn btn-codeflex" value="Back to Homepage" /></Link>
                    </div>
                </div>
            </div>
        )
    }
}

export default PageNotFound;
