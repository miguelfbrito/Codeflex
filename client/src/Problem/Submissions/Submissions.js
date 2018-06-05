import React from 'react';

import { URL } from '../../commons/Constants';

class Submissions extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            results: []
        }
    }

    componentDidMount() {
        fetch(URL + '/api/database/Submissions/viewByProblemName/problem-number-1').then(res => res.json()).then(data => {
            console.log('submi');
            console.log(data);
            this.setState({ results: data });
        })
    }


    render() {

        return (
            <div>
                <div className="row">
                    {this.state.results.map(r => (
                        <div>
                            <div className="col-sm-2">{r.result.name}</div>
                            <div className="col-sm-2">{r.score}</div>
                            <div className="col-sm-2">{r.language.name}</div>
                            <div className="col-sm-2">{
                                new Date(r.date).getDate() + "/" + new Date(r.date).getMonth() + "/" + new Date(r.date).getFullYear()
                            }</div>
                            <div className="col-sm-2">
                                <p>View Results</p>
                            </div>
                            <div className="col-sm-2">
                                <p>View Submissions</p>    
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}

export default Submissions;