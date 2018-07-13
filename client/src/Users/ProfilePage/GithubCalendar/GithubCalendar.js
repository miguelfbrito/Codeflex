import React from 'react';
import CalendarHeatmap from 'react-calendar-heatmap';

import ReactTooltip from 'react-tooltip'
import { getDatesRange, getRndInteger, dateSameDay } from '../../../commons/Utils';

class GithubCalendar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }


    customTooltipDataAttrs = (value) => {
        return { 'data-tip': `${value.count} submissions on ${value.date}` };
    }

    

    render() {

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
        let submissions = this.props.submissions.sort((a, b) => { return a - b });
        console.log("SUBMISSIIONS");
        console.log(submissions);

        let j = 0;
        for (let i = 1; i < dates.length; i++) {
            let inObj = {};

            inObj['date'] = dates[i].getFullYear() + "-" + (dates[i].getMonth() + 1) + "-" + dates[i].getUTCDate();
            inObj['count'] = 0;

            while(typeof submissions[j] != "undefined" && dateSameDay(dates[i], submissions[j].date)){
                console.log("HII");
                inObj['count'] = ++inObj['count'];
                totalCount++;
                j++;

            }

            obj.push(inObj);
            
        }

        submissions.map(s => {
        })


        return (
            <div>
                <ReactTooltip place="top" type="dark" effect="float" />
                <p className="page-subtitle">{totalCount} submissions in the last year</p>
                <CalendarHeatmap
                    values={
                        [...obj]
                    }
                    titleForValue={this.customTitleForValue}
                    gutterSize={2}
                    startDate={new Date() - (24 * 60 * 60 * 1000 * 365)}
                    endDate={new Date()}
                    tooltipDataAttrs={this.customTooltipDataAttrs}
                    classForValue={(value) => {
                        if (!value) {
                            return `color-gitlab-0`;
                        }
                        return `color-gitlab-${value.count} date-${value.date} count-${value.count}`;
                    }}
                />
            </div>
        )
    }
}

export default GithubCalendar;