import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

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
            <ul className="nav navbar-nav navbar-right nav-menus">
                <li><Link to="/practise">Practise</Link></li>
                <li><Link to="/compete">Compete</Link></li>
                {/*  <li id="item-signup"><Link to="/signup"><span className="glyphicon glyphicon-user"></span>Sign Up</Link></li>`*/}
                <li id="item-login" >
                    <Link to="/">
                        {JSON.parse(localStorage.getItem('userData')).username}
                        <i className="material-icons">keyboard_arrow_right</i>
                    </Link>

                </li>

                <li className="dropdown">
                    <a className="dropbtn">Dropdown</a>
                    <div className="dropdown-content">
                        <a href="#">Link 1</a>
                        <a href="#">Link 2</a>
                        <a href="#">Link 3</a>
                    </div>
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
            <nav className="navbar navbar-inverse container-fluid">
                <div className="navbar-header">
                    <p id="codeflex">codeflex</p>
                    {/* <img id="logo-img" src={require('../images/brain.png')} alt=""/>*/}
                </div>
                {items}
            </nav>
        );
    }
}

export default Navbar;