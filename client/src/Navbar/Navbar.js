import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navbar2.css';
import '../commons/style.css';
class Navbar extends Component {

    // TODO : consts para os uri

    userLoggedOut() {
        return (<div className="container">
            <div className="navbar-header">
                <button type="button" className="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                </button>
                <a className="navbar-brand" href="#">Codeflex</a>
            </div>
            <div className="collapse navbar-collapse" id="myNavbar">
                <ul className="nav navbar-nav">

                </ul>
                <ul className="nav navbar-nav navbar-right">
                    <li><Link to="/login"><p>Login</p></Link></li>
                </ul>
            </div>
        </div>);
    }

    userLoggedIn() {
        return (
            <div className="container">
                <div className="navbar-header" style={{ marginTop: '5px' }}>
                    <button type="button" className="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                    </button>
                    <a className="navbar-brand" id="codeflex">codeflex</a>
                    {/*<img id="img-user" src={require('../images/logo.png')} alt="User flat image" */}
                </div>
                <div className="collapse navbar-collapse" id="myNavbar">
                    <ul className="nav navbar-nav">

                    </ul>
                    <ul className="nav navbar-nav navbar-right">
                        <li><Link to="/practise"><p>Practise</p></Link></li>
                        <li><Link to="/compete"><p>Compete</p></Link></li>
                        <li><Link to="/leaderboard"><p>Leaderboard</p></Link></li>
                        {/*<li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                            <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>*/}
                        <li className="dropdown">
                            <Link to="/" className="dropdown-toggle" data-toggle="dropdown">
                                <p>
                                    {JSON.parse(localStorage.getItem('userData')).username}&nbsp;
                                    <span class="caret"></span>
                                </p>
                            </Link>

                            <ul className="dropdown-menu">
                                <li><Link to="/manage"><p>Manage Content</p></Link></li>
                                <li><Link to="/"><p>Settings</p></Link></li>
                                <li><Link to="/" onClick={this.logoutUser}><p>Logout</p></Link></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>);
    }

    logoutUser() {
        localStorage.setItem('userData', '');
        localStorage.clear();
        console.log('Logging out')
    }

    render() {

        let items;

        if (localStorage.getItem('userData') != null) {
            items = this.userLoggedIn();
        } else {
            items = this.userLoggedOut();
        }

        return (
            <nav className="navbar navbar-inverse">
                {items}
            </nav>
        );
    }
}

export default Navbar;