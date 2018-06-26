import React from 'react';
import PathLink from '../PathLink/PathLink';

import { Link } from 'react-router-dom'
import { URL } from '../commons/Constants';
import { textToLowerCaseNoSpaces } from '../commons/Utils';

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
                    <h3 className="page-subtitle">Categories with at least one problem will be shown on 'Practise' section.</h3>

                    <div className="col-sm-3 col-xs-12 test-case-wrapper tc add-test-case" style={{ marginBottom: '25pt' }}>
                        <i className="material-icons manage-tournament-icon" id="add-test-case" onClick={this.onClickAdd}>add_circle_outline</i>
                    </div>

                    {this.state.categories.map((c, i) => (
                        <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                            <div >
                                <h3 style={{ display: 'inline-block' }}>{c.name}</h3>
                                <i style={{ position: 'absolute', top: '20px', right: '15px' }} className="material-icons">delete</i>
                            </div>
                            <hr style={{ borderBottom: 'none', borderLeft: 'none', borderRight: 'none' }} />
                            {c.problem.map((p, i) => (
                                <div>
                                    <Link to={"/manage/problems/edit/" + textToLowerCaseNoSpaces(p.name)}>
                                        <p style={{ fontFamily: 'Roboto Condensed', fontSize: '10pt' }}>{p.name}</p>
                                    </Link>
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