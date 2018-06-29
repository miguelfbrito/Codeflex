import React from 'react';
import PathLink from '../PathLink/PathLink';
import Popup from '../Popup/Popup';

import { Link } from 'react-router-dom'
import { URL } from '../commons/Constants';
import { textToLowerCaseNoSpaces } from '../commons/Utils';

import './ManageCategories.css';

class ManageCategories extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categories: []
        }

        this.modalAdd = React.createRef();
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

    deleteCategory = (pc) => {
        console.log('Deleting category')
        fetch(URL + '/api/database/PractiseCategory/delete/' + pc.id, {
            method: 'DELETE'
        }).then(() => {
            this.fetchCategories();
        });
    }

    onAddCategory = () => {

    }

    render() {


        const PopupAddCategory = () => (
            <div className="">
                <h2 style={{ color: 'black' }}>Add category</h2>
                <div className="row">
                        <input style={{margin:'15px'}} type="text" placeholder="Category name" />
                        <input type="button" value="Save"/>
                </div>
            </div>
        );

        return (
            <div className="container">
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Manage Categories" />
                    <h3 className="page-subtitle">Categories with at least one problem will be shown on 'Practise' section.</h3>

                    <div className="col-sm-3 col-xs-12 test-case-wrapper tc add-test-case" style={{ marginBottom: '25pt' }}>
                        <i className="material-icons manage-tournament-icon" id="add-test-case" onClick={() => this.modalAdd.current.openModal()}>add_circle_outline</i>
                    </div>

                    {this.state.categories.map((c, i) => (
                        <div className="col-sm-3 col-xs-12 test-case-wrapper tc">
                            <div >
                                <h3 style={{ display: 'inline-block' }}>{c.name}</h3>
                                <i onClick={() => this.deleteCategory(c)} style={{ position: 'absolute', top: '20px', right: '15px', cursor: 'pointer' }} className="material-icons">delete</i>
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

                    <Popup ref={this.modalAdd} onModalClose={this.onAddCategory}>
                        <PopupAddCategory />
                    </Popup>

                </div>
            </div>
        )
    }
}

export default ManageCategories;