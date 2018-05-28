import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";

import NavBar from './Navbar/Navbar';
import Problem from './Problem/Problem';
import Login from '../src/Users/Login/Login';
import Signup from '../src/Users/Signup/Signup';
import PageNotFound from '../src/PageNotFound/PageNotFound';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <NavBar />
          <Switch>
            <Route path="/problem" component={Problem} />
            <Route path="/login" component={Login} />
            <Route path="/signup" component={Signup} />
            <Route component={PageNotFound} />
            
          </Switch>
        </div>

      </Router>
    );
  }
}

export default App;
