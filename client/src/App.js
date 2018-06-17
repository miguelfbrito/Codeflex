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
import CreateTournament from './CreateTournament/CreateTournament';
import ManageTournaments from './ManageTournaments/ManageTournaments';
import ManageIndividualTournament from './ManageIndividualTournament/ManageIndividualTournament';
import AddProblem from './Problem/AddProblem/AddProblem';
import GlobalLeaderboard from './GlobalLeaderboard/GlobalLeaderboard';

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

            <Route exact path="/compete/create-tournament" component={CreateTournament} />
            <Route exact path="/compete/manage-tournaments/:tournamentName/add-problem" component={AddProblem} />
            <Route exact path="/compete/manage-tournaments/:tournamentName" component={ManageIndividualTournament} />
            <Route exact path="/compete/manage-tournaments" component={ManageTournaments} />

            <Route exact path="/compete" component={ListTournaments} />
            <Route exact path="/compete/:tournamentName" component={ListProblems} />
            <Route exact path="/compete/:tournamentName/:problemName" component={Problem} />
            <Route exact path="/compete/:categoryName/:problemName/view-results" component={ViewResults} />

            <Route exact path="/leaderboard" component={GlobalLeaderboard} />

            <Route component={PageNotFound} />
          </Switch>
          <Footer />
        </div>
      </Router>
    );
  }
}

export default App;
