import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

class Navbar extends Component {

    // TODO : consts para os uri

    userLoggedOut() {
        return (<ul className="nav navbar-nav navbar-right nav-menus">
            <li><Link to="/practise">Practise</Link></li>
            {/*<li className="dropdown"><a className="dropdown-toggle" data-toggle="dropdown" href="#">Page 1 <span className="caret"></span></a>
                <ul className="dropdown-menu">
                    <li><a href="#">Page 1-1</a></li>
                    <li><a href="#">Page 1-2</a></li>
                    <li><a href="#">Page 1-3</a></li>
                </ul>
    </li>*/}
            <li><Link to="/practice">Compete</Link></li>
            <li><Link to="/practice">Leaderboard</Link></li>
            <li id="item-login" ><Link to="/login"><span className="glyphicon glyphicon-log-in"></span>Login</Link></li>
        </ul>);
    }

    userLoggedIn() {
        return (<ul className="nav navbar-nav navbar-right nav-menus">
            <li><Link to="/problem">Problems</Link></li>
            {/*  <li id="item-signup"><Link to="/signup"><span className="glyphicon glyphicon-user"></span>Sign Up</Link></li>`*/}
            <li id="item-login" onClick={this.logoutUser}><Link to="/"><span className="glyphicon glyphicon-log-in"></span>Logout</Link></li>
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
                <div className="container-fluid">
                    <div className="navbar-header">
                        <p id="codeflex">codeflex</p>
                        {/* <img id="logo-img" src={require('../images/brain.png')} alt=""/>*/}
                    </div>

                    {items}

                </div>
            </nav>
        );
    }
}

export default Navbar;