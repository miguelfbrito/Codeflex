import React from 'react';
import FaGraduationCap from 'react-icons/lib/fa/graduation-cap';
import FaTrophy from 'react-icons/lib/fa/trophy';
import GoCalendar from 'react-icons/lib/go/calendar'
import './LandingPage.css';

class LandingPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {

        const backgroundStyle = {
            color: 'white',
            width: '100px',
            height: '60px',
            padding: '5px',
        }

        return (
            <div className="container">
                <div className="row">
                    <div className="slogan">
                        <h1>Welcome to codeflex!</h1>
                        <h2>Challenge your skill to solve problems while coding</h2>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-4">
                        <div className="landing-page-text-cols">
                            <div className="lp-icon-wrapper">
                                <FaGraduationCap size="50" style={backgroundStyle} />
                            </div>
                            <div>

                                <h3>Learn!</h3>
                                <p >
                                    Practise in a wide variety of problem categories such as Algorithms, Machine Learning and Data Structures. Each problem
                                    has a difficulty associated with it so you can find a captivating challenge.
                            </p>
                            </div>
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <div className="landing-page-text-cols">
                            <div className="lp-icon-wrapper">
                                <FaTrophy size="55" style={backgroundStyle} />
                            </div>

                            <div>

                                <h3>Compete!</h3>
                                <p>Compete against your friends, peers or strangers for the top place on the leaderboard. The time you take to solve each problem
                                    is tracked, hurry up!
                            </p>
                            </div>
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <div className="landing-page-text-cols">
                            <div className="lp-icon-wrapper">
                                <GoCalendar size="55" style={backgroundStyle} />
                            </div>

                            <div>
                                <h3>Organize!</h3>
                                <p>
                                    If you are enthusiastic about creating your own problems you'll have fun here. Create private tournaments and
                                    we'll help you manage everything.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )

    }
}

export default LandingPage;