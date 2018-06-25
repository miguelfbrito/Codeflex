import React from 'react';
import PathLink from '../PathLink/PathLink';


import { URL } from '../commons/Constants';

import './ManageCategory.css';

class ManageCategory extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categories: []
        }
    }

    componentDidMount() {
        this.fetchCategories();
    }

    fetchCategories = () => {
        fetch(URL + '/api/database/PractiseCategory/view').then(res => res.json()).then(data => {
            console.log('Fetching categories')
            console.log(data)
            this.setState({ categories: data })
        })
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Manage Categories" />
                    <h3 className="page-subtitle">Make sure the test cases you insert cover the problem fully.</h3>
                    <p className="page-subtitle">Add new test cases or edit the current ones. To edit, click on the respective button, edit the data and the changes will be saved when leaving the window.</p>

                    <div className="col-sm-3 col-xs-12 test-case-wrapper tc add-test-case">
                        <i className="material-icons manage-tournament-icon" id="add-test-case" onClick={this.onClickAdd}>add_circle_outline</i>
                    </div>

                    {this.state.categories.map((c, i) => (
                        <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                            <div >
                                <h3 style={{ display: 'inline-block' }}>{c.name}</h3>
                                <i style={{ position: 'absolute', top: '20px', right: '15px' }} className="material-icons">delete</i>
                            </div>
                            <hr style={{ borderBottom: 'none', borderLeft: 'none', borderRight: 'none' }} />
                            {c.problem.map((p,i) => (
                                <div>
                                    <p style={{ fontFamily: 'Roboto Condensed', fontSize: '10pt' }}>{p.name}</p>
                                </div>
                            ))}
                        </div>
                    ))}


                </div>
            </div>
        )
    }
}

export default ManageCategory;