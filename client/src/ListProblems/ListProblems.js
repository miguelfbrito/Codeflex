import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import './ListProblems.css';

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
                <p id="texto">List all the problems here!</p>
                <div className="row">
                    {this.state.categories.map((category, index) => (
                        <div key={category.id} className="col-sm-5 category-container">
                            <h2>{category.name}</h2>
                            <div className="progress-bar">
                                <div className="bar" style={{ width: (category.finishedProblems / category.totalProblems * 100) + '%' }}></div>
                                <p style={{ color: '#ccc', textAlign: 'left', paddingTop: '5px', fontSize: '10pt' }}>You have completed {category.finishedProblems} ({category.finishedProblems / category.totalProblems * 100}%) out of the {category.totalProblems} available problems.</p>
                            </div>
                            <div className="button-container">
                                <input type="submit" className="btn btn-primary" value="Explore problems" />
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

export default ListProblems;
