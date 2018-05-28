import React, { Component } from 'react';
import './Login.css'

import { URL } from '../../commons/constants';

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
        this.setState({ [e.target.className] : e.target.value });
    }

    login() {
        
        const data = { username: this.state.username, password: this.state.password };

        fetch(URL + ':8080/api/login', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => res.json)

    }

    register(){

        if(this.state.passwordConfirmation !== this.state.password){
            console.log('Passwords do not match'); // TODO : add notification 
            return;
        }
        
        const data = { username: this.state.username, email : this.state.email, password: this.state.password };

        fetch(URL + ':8080/api/register', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => res.json).then(data => {
            console.log(data);
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
                <p>Password</p>
                <input className="password" type="password" onChange={this.handleChange} placeholder="Password" required /> <br />
            </div>);
        } else if (this.state.isSigninUp) {
            return (<div>
                <p>Email</p>
                <input key="1" className="email" type="email" onChange={this.handleChange} placeholder="Email" required /> <br />

                <p>Password</p>
                <input key="2" className="password" type="password" onChange={this.handleChange} placeholder="Password" required /> <br />

                <p>Confirm Password</p>
                <input key="3" className="passwordConfirmation" type="password" onChange={this.handleChange} placeholder="Confirm password" required /> <br />
            </div>);
        }
    }

    render() {

        const loginOrSignup = this.renderLoginOrSignup();

        return (

            <div className="row ">
                <div className="col-md-1"></div>
                <div className="col-md-10">
                    <form className="login-container">
                        <img id="img-user" src={require('../../images/login_icon.png')} alt="User flat image" />
                        <p>Username</p>
                        <input className="username" type="text" onChange={this.handleChange} placeholder="Username" required />

                        {loginOrSignup}

                        <div className="buttons-container">
                            <input type="button" className="btn btn-success" value="Login" onClick={this.handleSubmit} />
                            <input type="button" className="btn btn-success" value="Sign Up" onClick={this.handleSubmit} />
                        </div>
                    </form>
                </div>

                <div className="col-md-1"></div>
            </div>











        );
    }
}

export default Login;