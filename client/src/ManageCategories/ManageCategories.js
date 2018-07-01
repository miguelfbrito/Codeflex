import React from 'react';
import PathLink from '../PathLink/PathLink';
import Popup from '../Popup/Popup';

import { Link } from 'react-router-dom'
import { URL } from '../commons/Constants';
import { textToLowerCaseNoSpaces, parseLocalJwt, getAuthorization} from '../commons/Utils';

import './ManageCategories.css';

class ManageCategories extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categories: [],
            categoryName: ''
        }

        this.modalAdd = React.createRef();
        this.inputCategoryName = React.createRef();
    }

    componentDidMount() {
        this.fetchCategories();
    }

    fetchCategories = () => {
        fetch(URL + '/api/database/PractiseCategory/view', {headers : {...getAuthorization()}}).then(res => res.json()).then(data => {
            console.log('Fetching categories')
            console.log(data)
            this.setState({ categories: data })
        })
    }

    deleteCategory = (pc) => {
        console.log('Deleting category')
        fetch(URL + '/api/database/PractiseCategory/delete/' + pc.id, {
            method: 'DELETE',
            ...getAuthorization()
        }).then(() => {
            this.fetchCategories();
        });
    }

    onAddCategory = () => {
        const data = { name: this.inputCategoryName.current.value }
        fetch(URL + '/api/database/PractiseCategory/add', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json',
                ...getAuthorization()
            })
        }).then(res => res.json()).then(data => {
            console.log("New Category")
            console.log(data);

            let newCategories = this.state.categories;
            newCategories.push(data);
            this.setState({ categories: newCategories});

            this.modalAdd.current.closeModal();

        })
    }

    render() {

        const PopupAddCategory = () => (
            <div className="">
                <h2 style={{ color: 'black', marginTop: '-5px', marginBottom: '5px' }}>Add category</h2>
                <div className="row">
                    <input autofocus style={{ margin: '15px' }} name="categoryName" className="textbox" id="input-add-category"
                        ref={this.inputCategoryName} type="text" placeholder="Category name" />
                    <input type="button" className="btn btn-codeflex" id="input-save-category" onClick={this.onAddCategory} value="Save" />
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

                    <Popup ref={this.modalAdd} >
                        <PopupAddCategory />
                    </Popup>

                </div>
            </div>
        )
    }
}

export default ManageCategories;