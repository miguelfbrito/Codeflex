import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';
import '../commons/style.css';
import { parseLocalJwt, splitUrl } from '../commons/Utils';
class Navbar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            page: '',
            count: 0
        }
        this.url = splitUrl(window.location.pathname)[0];
    }

    // TODO : consts para os uri
    componentWillUpdate() {
        this.url = splitUrl(window.location.pathname)[0];
        console.log("URL " + this.url);
    }

    componentDidMount() {
    }

    userLoggedOut() {
        return (
            <div>

                <div className="collapse navbar-collapse" id="myNavbar">
                    <ul className="nav navbar-nav">

                    </ul>
                    <ul className="nav navbar-nav navbar-right">
                        <li><Link to="/login"><p>Login</p></Link></li>
                    </ul>
                </div>
            </div >);
    }

    userLoggedIn() {

        return (
            <div>
                <div className="collapse navbar-collapse" id="myNavbar">
                    <ul className="nav navbar-nav">

                    </ul>
                    <ul className="nav navbar-nav navbar-right ">
                        <li className={this.url === 'practise' ? 'active-page' : ''}><Link to="/practise"><p>Practise</p></Link></li>
                        <li className={this.url === 'compete' ? 'active-page' : ''}><Link to="/compete"><p>Compete</p></Link></li>
                        <li className={this.url === 'leaderboard' ? 'active-page' : ''}><Link to="/leaderboard"><p>Leaderboard</p></Link></li>
                        {/*<li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                            <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>*/}
                        <li className="dropdown">
                            <Link to="/" className="dropdown-toggle" data-toggle="dropdown">
                                <p>
                                    {parseLocalJwt().username}&nbsp;
                                    <span class="caret"></span>
                                </p>
                            </Link>

                            <ul className="dropdown-menu">
                                <li><Link to={"/user/" + parseLocalJwt().username}><p>Profile</p></Link></li>
                                {parseLocalJwt().role === "CONTENT_MANAGER" ?
                                    <li><Link to="/manage"><p>Manage Content</p></Link></li> : ''
                                }
                                <li><Link to="/"><p>Settings</p></Link></li>
                                <li><Link to="/" onClick={this.logoutUser}><p>Logout</p></Link></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>);
    }

    logoutUser() {
        localStorage.clear();
        console.log('Logging out')
    }

    render() {

        let items;

        if (localStorage.getItem('token') != null) {
            items = this.userLoggedIn();
        } else {
            items = this.userLoggedOut();
        }

        return (
            <nav className="navbar navbar-inverse">
                <div className="container">
                    <div className="navbar-header">
                        <button type="button" className="navbar-toggle" data-toggle="collapse" data-target="#myNavbar" style={{border:'2px solid white'}}>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                        </button>
                        <a className="navbar-brand" href="/" style={{ fontFamily: 'Roboto Condensed, sans-serif' }}>codeflex</a>
                    </div>
                    {items}
                </div>
            </nav>
        );
    }
}

export default Navbar;