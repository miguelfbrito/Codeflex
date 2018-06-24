import React from 'react';
import PathLink from '../PathLink/PathLink';
import {Link} from 'react-router-dom';

import './ManageContent.css';

class ManageContent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Manage Content" />
                    <h3 className="page-subtitle">Click on the respective section to edit the data.</h3>
                    <div className="col-sm-4 manage">
                        <div className="col-sm-12 manage-topic">
                            <i className="material-icons manage-icons">category</i>
                            <h3>Categories</h3>
                            <p className="page-subtitle">Manage the categories shown on practise.</p>
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <Link to="/manage/problems">
                            <div className="col-sm-12 manage-topic">
                                <i className="material-icons manage-icons">polymer</i>
                                <h3>Problems</h3>
                                <p className="page-subtitle">Manage the list of available problems. Test cases are also take care of here.</p>
                            </div>
                        </Link>
                    </div>
                    <div className="col-sm-4">
                        <div className="col-sm-12 manage-topic">
                            <i className="material-icons manage-icons">insert_chart_outlined</i>
                            <h3>Tournaments</h3>
                            <p className="page-subtitle">Manage tournaments and all the aspects associated with them.</p>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default ManageContent;