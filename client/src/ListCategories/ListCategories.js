import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import './ListCategories.css';
import { Link } from 'react-router-dom';
import { textToLowerCaseNoSpaces } from '../commons/Utils';

class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
            categories: []
        }

    }


    componentDidMount() {
        fetch(URL + ':8080/api/database/PractiseCategory/listwithstats/' + JSON.parse(localStorage.getItem('userData')).id //, {
            //headers: new Headers({
            // 'Content-Type': 'application/json',
            //'Authorization': 'Token ' + localUser.token
            // })
        ).then(res => res.json()).then(data => {
            this.setState({ categories: data })
            console.log(this.state.categories);
        })
    }

    onChange(newValue) {
        console.log(newValue);
    }



    render() {
        return (
            <div className="container">
                <div className="row">
                    <h2 className="page-title">Choose your category</h2>
                    <hr style={{ width: '100%', height: '10px' }} />
                    {this.state.categories.map((category, index) => (
                        <div key={category.id} className="col-sm-6 category-container">
                            <h2>{category.name}</h2>
                            <div className="progress-bar">
                                <div className="bar" style={{ width: (category.finishedProblems / category.totalProblems * 100) + '%' }}></div>
                                <p className="p-small-text">You have completed {category.finishedProblems} ({category.finishedProblems / category.totalProblems * 100}%) out of the {category.totalProblems} available problems.</p>
                            </div>
                            <div className="button-container">
                                <Link to={{ pathname: "/practise/" + textToLowerCaseNoSpaces(category.name), state: { categoryId: category.id } }}><input type="submit" className="btn btn-primary" value="Explore problems" /></Link>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

export default ListProblems;