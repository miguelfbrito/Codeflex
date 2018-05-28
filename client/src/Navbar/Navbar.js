import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

class Navbar extends Component {
    render() {
        return (
            <nav className="navbar navbar-inverse">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a className="navbar-brand" href="#">Codeflex</a>
                    </div>

                    <ul className="nav navbar-nav navbar-right nav-menus">
                        <li className="active"><a href="#">Home</a></li>
                        <li className="dropdown"><a className="dropdown-toggle" data-toggle="dropdown" href="#">Page 1 <span className="caret"></span></a>
                            <ul className="dropdown-menu">
                                <li><a href="#">Page 1-1</a></li>
                                <li><a href="#">Page 1-2</a></li>
                                <li><a href="#">Page 1-3</a></li>
                            </ul>
                        </li>
                        <li><Link to="/problem">Problems</Link></li>
                        <li id="item-signup"><Link to="/signup"><span className="glyphicon glyphicon-user"></span>Sign Up</Link></li>
                        <li id="item-login" ><Link to="/login"><span className="glyphicon glyphicon-log-in"></span>Login</Link></li>
                    </ul>
                </div>
            </nav>
        );
    }
}

export default Navbar;