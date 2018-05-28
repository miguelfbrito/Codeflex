import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";

import NavBar from './Navbar/Navbar';
import ListProblems from './ListProblems/ListProblems'
import Login from '../src/Users/Login/Login';
import PageNotFound from '../src/PageNotFound/PageNotFound';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <NavBar />
          <Switch>
            <Route path="/practice" component={ListProblems} />
            <Route path="/login" component={Login} />
            <Route component={PageNotFound} />
            
          </Switch>
        </div>

      </Router>
    );
  }
}

export default App;
