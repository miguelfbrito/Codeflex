import React from 'react';
import PathLink from '../../PathLink/PathLink';
import CalendarHeatmap from 'react-calendar-heatmap';

import './ProfilePage.css';
// https://github.com/patientslikeme/react-calendar-heatmap
class ProfilePage extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
        }
    }
    render() {
        const customTooltipDataAttrs = { 'data-toggle': 'tooltip' };
        const data = {
            date: new Date().getTime(),
            total: 1000
        }
        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Profile page" />

                </div>
                <div className="col-sm-2">
                    <p>username</p>
                </div>

                <div className="col-sm-10">
                    <h3>Activity</h3>
                    <CalendarHeatmap
                        values={[
                            { date: '2018-03-01', count: 1 },
                            { date: '2018-05-03', count: 4 },
                            { date: '2018-04-01', count: 2 }
                        ]}
                        gutterSize={2}
                        numDays={365}
                        tooltipDataAttrs={customTooltipDataAttrs}
                        classForValue={(value) => {
                            if (!value) {
                                return 'color-github-0';
                            }
                            return `color-github-${value.count}`;
                        }}
                    />

                    <div data-toggle="tooltip">Hi there</div>
                </div>
            </div>
        )
    }
}

export default ProfilePage;