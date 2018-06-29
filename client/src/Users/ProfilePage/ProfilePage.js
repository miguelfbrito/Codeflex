import React from 'react';
import PathLink from '../../PathLink/PathLink';
import CalendarHeatmap from 'react-calendar-heatmap';
import $ from 'jquery';
import ReactTooltip from 'react-tooltip'

import { getDatesRange, getRndInteger } from '../../commons/Utils';

import Moment from 'moment';
import { extendMoment } from 'moment-range';

//const moment = extendMoment(Moment);

import './ProfilePage.css';
// https://github.com/patientslikeme/react-calendar-heatmap
class ProfilePage extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
        }
    }

    componentDidMount() {

    }


    customOnClick = (e) => {
        console.log(e.target);
        console.log(e.target.className);
        console.log('hi');
    }

    customTitleForValue(value) {
        return value ? console.log('hi there') : null;
    }

    customTooltipDataAttrs = (value) => {
        return { 'data-tip': `${value.count} submissions on ${value.date}` };
    }

    render() {
        // const customTooltipDataAttrs = { 'data-tip': 'hi there' };
        const data = {
            date: new Date().getTime(),
            total: 1000
        }

        let dates = getDatesRange(new Date() - (24 * 60 * 60 * 1000 * 365), new Date());
        const heatmapValues = {
        }

        let totalCount = 0;
        let obj = []
        console.log(dates.length)
        for (let i = 1; i < dates.length; i++) {
            let inObj = {};

            inObj['date'] = dates[i].getFullYear() + "-" + (dates[i].getMonth()+1) + "-" + dates[i].getUTCDate();
            let rng = getRndInteger(0,7);
            inObj['count'] = rng;

            totalCount+=rng;
            obj.push(inObj);
        }

        console.log(obj)



        return (
            <div className="container" >
                <div className="row">
                    <PathLink path={this.props.location.pathname} title="Profile page" />

                </div>
                <div className="col-sm-2">
                    <p>username</p>
                </div>

                <div className="col-sm-10">
                    <p className="page-subtitle">{totalCount} submissions in the last year</p>
                    <CalendarHeatmap
                        values={
                            [...obj]
                        }
                        onMouseOver={(e) => this.customOnClick(e)}
                        titleForValue={this.customTitleForValue}
                        gutterSize={2}
                        startDate={new Date() - (24 * 60 * 60 * 1000 * 365)}
                        endDate={new Date()}
                        name='hiiiiiii'
                        tooltipDataAttrs={this.customTooltipDataAttrs}
                        classForValue={(value) => {
                            if (!value) {
                                return `color-github-0`;
                            }
                            return `color-github-${value.count} date-${value.date} count-${value.count}`;
                        }}
                    />
                    <ReactTooltip place="top" type="dark" effect="float" />
                </div>
            </div>
        )
    }
}

export default ProfilePage;