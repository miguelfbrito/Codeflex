import React, { Component } from 'react';
import './Login.css'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { URL, URL_FRONT } from '../../commons/Constants';
import { areStringEqual, validateEmail, validateLength, validateStringChars, isStringEmpty } from '../../commons/Validation';

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            email: '',
            passwordConfirmation: '',
            isLoggingIn: true,
            isSigninUp: false,
            showErrors: false
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }




    handleChange(e) {
        this.setState({ [e.target.className]: e.target.value });
    }

    login() {

        const data = { username: this.state.username, password: this.state.password };

        if (isStringEmpty(data.username) || isStringEmpty(data.password)) {
            toast.error("Fill in all the fields", { autoClose: 2500 })
            return;
        }

        fetch(URL + '/api/account/login', {
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(data),
            headers: new Headers({
                'Content-Type': 'application/json',
                "Cache-Control": "no-cache",
            })
        }).then(res => {
            if (res.status === 401 || res.status === 403) {
                console.log("ERRRROR")
                toast.error("Invalid credentials", { autoClose: 2500 })
                return;
            } else {
                return res.json();
            }
        }).then(data => {

            if (data) {
                console.log(data)
                window.location.href = '/';
                localStorage.setItem('token', data.token);
                localStorage.setItem('userData', JSON.stringify({ username: data.username }));

            }

        }).catch((e) => {
            console.log(e)
        })

    }

    validateRegistration = (data) => {
        console.log(data);
        this.setState({ showErrors: true });

        if (isStringEmpty(data.username) || isStringEmpty(data.email) || isStringEmpty(data.password) || isStringEmpty(data.passwordConfirmation)) {
            toast.error("Fill in all the fields", { autoClose: 2500 })
        } else {

            if (!validateLength(data.username, 3, 25)) {
                toast.error("Username must be between 3 and 25 characters");


            } else if (!validateStringChars(data.username)) {
                toast.error("Your username can only contain letters, numbers and _");
            }


            if (!validateEmail(data.email)) {
                toast.error("Wrong email format");
            }

            if (!areStringEqual(data.password, data.passwordConfirmation)) {
                toast.error("Passwords do not match");
            } else {
                if (!validateLength(data.password, 5, 64)) {
                    toast.error("Your password must be between 5 and 64 characters");
                }
            }


        }
    }

    register() {

        const sentData = { username: this.state.username, email: this.state.email, password: this.state.password };
        const validateData = { ...sentData, passwordConfirmation: this.state.passwordConfirmation }
        console.log(this.validateRegistration(validateData));

        fetch(URL + '/api/account/register', {
            method: 'POST',
            body: JSON.stringify(sentData),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(res => {
            if (res.status === 409 || res.status === 403) {
                toast.error("Username already in use.", 2500);
                return;
            } else {
                this.setState({
                    isLoggingIn: true,
                    isSigninUp: false
                })
                toast.success("Account created with success!", { autoClose: 2500 });
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


                        <ToastContainer
                            position="top-right"
                            autoClose={5500}
                            hideProgressBar={false}
                            closeOnClick
                            rtl={false}
                            pauseOnVisibilityChange
                            draggable
                            pauseOnHover
                            style={{ fontFamily: "'Roboto', sans-serif", fontSize: '12pt', letterSpacing: '1px' }}
                        />

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