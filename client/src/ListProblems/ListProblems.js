import React, { Component } from 'react';


class ListProblems extends Component {

    constructor(props) {
        super(props);
        this.state = {
        }

    }

    onChange(newValue) {
        console.log(newValue);
    }


    render() {

        return (
            <div>
                <p>List all the problems here!</p>
            </div>
        );
    }
}

export default ListProblems;
