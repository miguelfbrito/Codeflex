import React from 'react';
import './Leaderboard.css';

import { URL } from '../../commons/Constants';
import { splitUrl } from '../../commons/Utils';

class Leaderboard extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            problemName: null,
            leaderboard: []
        }
    }

    componentDidMount() {

        console.log("Printing props")
        let problemName = splitUrl(this.props.pathname)[2];
        console.log(problemName);
        fetch(URL + '/api/database/Leaderboard/viewByProblemName/' + problemName).then(res => res.json())
            .then(data => {
                console.log(data);
                this.setState({ problemName: problemName, leaderboard: data })
            })
    }

    render() {

        let toRender = '';
        if (this.state.problemName != null) {

            let leaderboard = this.state.leaderboard.sort((a, b) => { return b.score - a.score });
            let currentUsername = JSON.parse(localStorage.getItem('userData')).username;

            toRender =
                <table className="table table-hover" id="table-leaderboard">
                    <thead>
                        <tr>
                            <th><p>Position</p></th>
                            <th><p>Username</p></th>
                            <th><p>Language</p></th>
                            <th><p>Score</p></th>
                        </tr>
                    </thead>
                    <tbody>
                        {leaderboard.map((l, index) => (
                            <tr key={index} className={currentUsername === l.username ? 'table-active' : ''}>
                                <th><p>{index + 1}</p></th>
                                <th><p>{l.username}</p></th>
                                <th><p>{l.language}</p></th>
                                <th><p>{l.score}</p></th>
                            </tr>
                        ))}
                    </tbody>
                </table>
        }

        return (
            <div>
                {toRender}
            </div>
        );
    }
}

export default Leaderboard;