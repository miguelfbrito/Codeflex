import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navbar2.css';

class Navbar extends Component {

    // TODO : consts para os uri

    userLoggedOut() {
        return (<ul className="nav navbar-nav navbar-right nav-menus">
            <li><Link to="/practise">Practise</Link></li>
            <li>

            </li>
            <li><Link to="/practice">Compete</Link></li>
            <li><Link to="/practice">Leaderboard</Link></li>
            <li id="item-login" ><Link to="/login"><span className="glyphicon glyphicon-log-in"></span>Login</Link></li>
        </ul>);
    }

    userLoggedIn() {
        return (
            <ul className="nav navbar-nav nav-menus navbar-right">
                <li><Link to=""><p id="codeflex">Codeflex</p></Link></li>
                <li><Link to="/practise">Practise</Link></li>
                <li><Link to="/compete">Compete</Link></li>
                <li><Link to="/leaderboard">Leaderboard</Link></li>
                {/*  <li id="item-signup"><Link to="/signup"><span className="glyphicon glyphicon-user"></span>Sign Up</Link></li>`*/}
                <li id="item-login" >
                    <Link to="/">
                        {JSON.parse(localStorage.getItem('userData')).username}
                        <i className="material-icons">keyboard_arrow_right</i>
                    </Link>

                </li>
            </ul>);
    }

    logoutUser() {
        localStorage.setItem('userData', '');
        localStorage.clear();
    }

    render() {

        let items;

        if (localStorage.getItem('userData') != null) {
            console.log('in')
            items = this.userLoggedIn();
        } else {
            console.log('out')
            items = this.userLoggedOut();
        }

        return (
            <nav className="navbar navbar-inverse">
                <div className="container">
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
                            <li><Link to="/practise"><p>Practise</p></Link></li>
                            <li><Link to="/compete"><p>Compete</p></Link></li>
                            <li><Link to=""><p>Leaderboard</p></Link></li>
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
                                    <li><a href="#">Settings</a></li>
                                    <li><a href="#">Administration</a></li>
                                    <li><a href="#"></a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        );
    }
}

export default Navbar;