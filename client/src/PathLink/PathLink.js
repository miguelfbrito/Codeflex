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

    buildPath(pathname, index) {
        let finalPath = '/';
        for (let i = 0; i <= index; i++) {
            finalPath += pathname[i];

            if(this.props.removePath.includes[i]){
                return "";
            }

            if (i < index) {
                finalPath += '/';
            }
        }
        return finalPath;
    }


    render() {
        let pathname = splitUrl(this.props.path);

        let title = this.props.title;
        if (typeof title !== 'undefined') {
            title.replace('-', ' ');
        }

        return (
            <div>
                <div className="header-link-container">
                    {pathname.map((p, index) => (
                        <div key={index}>
                            <Link key={index} to={{ pathname: this.buildPath(pathname, index) }}>
                                <p style={{ display: 'inline-block', fontFamily:"'Roboto Condensed', sans-serif"}}>
                                    {p}
                                </p>
                            </Link>
                            {index < pathname.length - 1 ? <i className="material-icons">keyboard_arrow_right</i> : ''}
                        </div>
                    ))}
                    <div>
                    </div>
                </div>
                <div className="page-title-container">
                    <h2 className="page-title">{title}</h2>
                    <hr id="pathlink-hr" />
                </div>
            </div>
        )

    }
}

PathLink.defaultProps = {
    removeHyphen: true,
    removePath : []
}

export default PathLink;