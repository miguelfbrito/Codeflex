import React, { Component } from 'react';
import { URL } from '../commons/Constants';
import './ListCategories.css';
import { Link } from 'react-router-dom';
import { textToLowerCaseNoSpaces, parseLocalJwt, getAuthorization} from '../commons/Utils';
import PathLink from '../PathLink/PathLink';

class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
            categories: []
        }

    }


    componentDidMount() {
        fetch(URL + '/api/database/PractiseCategory/listwithstats/' + parseLocalJwt().username , {
            headers: {
                ...getAuthorization()
            }
        }).then(res => res.json()).then(data => {
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
                    <PathLink path={this.props.location.pathname} title="Categories" />
                    {this.state.categories.length > 0 && this.state.categories.map((category, index) => (
                        <div key={category.id} className="col-sm-6 category-container">
                            <h2 style={{fontFamily: "'Roboto Condensed', sans-serif", fontSize:'24pt'}}>{category.name}</h2>
                            <div className="progress-bar">
                                <div className="bar" style={{ width: (category.finishedProblems / category.totalProblems * 100) + '%' }}></div>
                                <p className="p-small-text">You have completed {category.finishedProblems} ({(category.finishedProblems / category.totalProblems * 100).toFixed(2)}%) out of the {category.totalProblems} available problems.</p>
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
