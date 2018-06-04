import React from 'react'
import { Link } from 'react-router-dom'
import './PathLink.css';
import { splitUrl } from '../commons/Utils';

class PathLink extends React.Component {
    constructor(props) {
        super(props);
    }

    createPath(path, index) {
        return path.map((p, i) => {
            if (i <= index) {
                p + "/"
            }
        });
    }

    render() {
        let pathname = splitUrl(this.props.path);
        console.log('Logging pathname ')
        console.log(pathname);
       
        let title = this.props.title;
        if(typeof title !== 'undefined'){
            title.replace('-', ' ');
        }

        return (
            <div>
                <div className="header-link-container">
                    {pathname.map((p, index) => (
                        <div key={index}>
                            <Link to={{ pathname: '/' }}>
                                <p style={{ display: 'inline-block' }}>
                                    {p}
                                </p>
                            </Link>
                            {index < pathname.length - 1 ? <i class="material-icons">keyboard_arrow_right</i> : ''}
                        </div>
                    ))}
                    <div>
                    </div>
                </div>
                <div className="page-title-container">
                    <h2 className="page-title">{title}</h2>
                    <hr />
                </div>
            </div>
        )

    }
}

PathLink.defaultProps = {
    removeHyphen: true
}

export default PathLink;