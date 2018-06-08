import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";

import NavBar from './Navbar/Navbar';
import ListCategories from './ListCategories/ListCategories'
import ListProblems from './ListProblems/ListProblems'
import Login from '../src/Users/Login/Login';
import PageNotFound from '../src/PageNotFound/PageNotFound';
import Problem from '../src/Problem/Problem';
import Footer from '../src/Footer/Footer';
import ViewResults from '../src/ViewResults/ViewResults';
import ListTournaments from './ListTournaments/ListTournaments';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <NavBar />
          <Switch>
            <Route exact path="/login" component={Login} />
            <Route exact path="/problem" component={Problem} />

            <Route exact path="/practise/:categoryName/:problemName" component={Problem} />
            <Route exact path="/practise/:categoryName/:problemName/view-results" component={ViewResults} />
            <Route exact path="/practise/:categoryName" component={ListProblems} />
            <Route exact path="/practise" component={ListCategories} />

            <Route exact path="/compete" component={ListTournaments} />


            <Route component={PageNotFound} />
          </Switch>
          <Footer />
        </div>
      </Router>
    );
  }
}

export default App;
