import React, { Component } from 'react';
import './Login.css'

import { URL, URL_FRONT } from '../../commons/Constants';

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            email: '',
            passwordConfirmation: '',
            isLoggingIn: true,
            isSigninUp: false
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(e) {
        this.setState({ [e.target.className]: e.target.value });
    }

    login() {

        const data = { username: this.state.username, password: this.state.password };
        fetch('http://localhost:8080/api/account/login', {
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json',
                "Cache-Control": "no-cache",
            })
        }).then(res => res.json()).then(data => {

            if (data) {
                console.log(data)
                window.location.href = '/';
                localStorage.setItem('token', data.token);
                localStorage.setItem('userData', JSON.stringify({ username : data.username}));

            }

        }).catch((e) => {
            console.log(e)
        })

    }

    register() {

        if (this.state.passwordConfirmation !== this.state.password) {
            console.log('Passwords do not match'); // TODO : add notification 
            return;
        }

        const data = { username: this.state.username, email: this.state.email, password: this.state.password };

        fetch(URL + '/api/account/register', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => {
            res.json()
            console.log('Response')
            console.log(res);

        }).then(data => {
            console.log(data);
            if (data.object != null) {
                window.location.href = URL_FRONT;
                const userData = { id: data.object.id, username: data.object.username, email: data.object.email };
                localStorage.setItem('userData', JSON.stringify(userData));
            }
        })

    }


    handleSubmit(e) {
        if (e.target.value === 'Login') {
            if (this.state.isLoggingIn) {
                this.login();
            } else if (this.state.isSigninUp) {
                this.setState({ isLoggingIn: true, isSigninUp: false });
            }
        } else if (e.target.value === 'Sign Up') {
            if (this.state.isSigninUp) {
                this.register();
            } else if (this.state.isLoggingIn) {
                this.setState({ isLoggingIn: false, isSigninUp: true });
            }
        }
    }

    renderLoginOrSignup() {
        if (this.state.isLoggingIn) {
            return (<div>
                <input className="password" type="password" onChange={this.handleChange} placeholder="Password" required />
            </div>);
        } else if (this.state.isSigninUp) {
            return (<div>
                <input key="1" className="email" type="email" onChange={this.handleChange} placeholder="Email" required />

                <input key="2" className="password" type="password" onChange={this.handleChange} placeholder="Password" required />

                <input key="3" className="passwordConfirmation" type="password" onChange={this.handleChange} placeholder="Confirm password" required />
            </div>);
        }
    }

    render() {

        const loginOrSignup = this.renderLoginOrSignup();

        return (

            <div className="row ">
                <div className="col-md-12 cont">
                    <h2 id="create-account-text">Login or create your Codeflex account today!</h2>
                    <form className="login-container">
                        <img id="img-user" src={require('../../images/login_icon.png')} alt="User flat image" />
                        <div>
                            <h4>Account Details</h4>
                            <input key="0" className="username" type="text" onChange={this.handleChange} placeholder="Username" required />
                        </div>

                        {loginOrSignup}

                        <div className="buttons-container">
                            <input type="button" className="btn btn-success" value="Login" onClick={this.handleSubmit} />
                            <input type="button" className="btn btn-success" value="Sign Up" onClick={this.handleSubmit} />
                        </div>
                    </form>
                </div>

            </div>
        );
    }
}

export default Login;