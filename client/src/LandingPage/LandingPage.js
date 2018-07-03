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
                    <div className="col-sm-4">
                        <div className="landing-page-text-cols">
                            <div className="lp-icon-wrapper">
                                <FaGraduationCap size="50" style={backgroundStyle} />
                            </div>
                            <div>

                            <h3>Learn!</h3>
                            <p>

                            </p>
                            </div>
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <div className="landing-page-text-cols">
                            <div className="lp-icon-wrapper">
                                <FaTrophy size="55" style={backgroundStyle} />
                            </div>

                            <h3>Compete!</h3>
                            Lorem ipsum dolor sit, amet consectetur adipisicing elit. Quibusdam esse veniam expedita provident molestias assumenda reiciendis nulla ea et, maiores pariatur modi nam magnam, dolor fugiat. Est laboriosam minus nostrum.
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <div className="landing-page-text-cols">
                            <div className="lp-icon-wrapper">
                                <GoCalendar size="55" style={backgroundStyle} />
                            </div>
                            <h3>Organize!</h3>
                            Lorem ipsum dolor sit, amet consectetur adipisicing elit. Quibusdam esse veniam expedita provident molestias assumenda reiciendis nulla ea et, maiores pariatur modi nam magnam, dolor fugiat. Est laboriosam minus nostrum.
                        </div>
                    </div>
                </div>
            </div>
        )

    }
}

export default LandingPage;