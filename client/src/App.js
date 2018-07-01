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
import ManageProblems from './ManageProblems/ManageProblems';
import AddProblem from './Problem/AddProblem/AddProblem';
import GlobalLeaderboard from './GlobalLeaderboard/GlobalLeaderboard';
import ManageTestCases from './ManageTestCases/ManageTestCases';
import ManageContent from './ManageContent/ManageContent';
import ManageCategories from './ManageCategories/ManageCategories';
import Leaderboard from './Problem/Leaderboard/Leaderboard';
import TournamentLeaderboard from './TournamentLeaderboard/TournamentLeaderboard';
import ProfilePage from './Users/ProfilePage/ProfilePage';
import PageWrapper from './PageWrapper/PageWrapper';

import './PageWrapper/PageWrapper.css';

class App extends Component {
  render() {
    return (
      <Router>
        <div style={{ margin: '0', padding: '0' }}>
          <NavBar />
          <div style={{minHeight:'100%'}}>
            <Switch>
              <Route exact path="/login" component={PageWrapper(Login)} />
              <Route exact path="/problem" component={PageWrapper(Problem)} />

              <Route exact path="/user/:username" component={PageWrapper(ProfilePage)} />

              <Route exact path="/manage" component={PageWrapper(ManageContent)} />
              <Route exact path="/manage/problems" component={PageWrapper(ManageProblems)} />
              <Route exact path="/manage/problems/add" component={PageWrapper(AddProblem)} />
              <Route exact path="/manage/problems/edit/:problemName" component={PageWrapper(AddProblem)} />
              <Route exact path="/manage/problems/:problemName/test-cases" component={PageWrapper(ManageTestCases)} />

              <Route exact path="/manage/tournaments" component={PageWrapper(ManageTournaments)} />
              <Route exact path="/manage/categories" component={PageWrapper(ManageCategories)} />

              <Route exact path="/practise/:categoryName/:problemName" component={PageWrapper(Problem)} />
              <Route exact path="/practise/:categoryName/:problemName/view-results" component={PageWrapper(ViewResults)} />
              <Route exact path="/practise/:categoryName" component={PageWrapper(ListProblems)} />
              <Route exact path="/practise" component={PageWrapper(ListCategories)} />

              <Route exact path="/compete/create-tournament" component={PageWrapper(CreateTournament)} />
              <Route exact path="/compete/manage-tournaments/:tournamentName/:problemName/test-cases" component={PageWrapper(ManageTestCases)} />
              <Route exact path="/compete/manage-tournaments/:tournamentName/edit/:problemName" component={PageWrapper(AddProblem)} />
              <Route exact path="/compete/manage-tournaments/:tournamentName/add" component={PageWrapper(AddProblem)} />
              <Route exact path="/compete/manage-tournaments/:tournamentName" component={PageWrapper(ManageProblems)} />
              <Route exact path="/compete/manage-tournaments" component={PageWrapper(ManageTournaments)} />

              <Route exact path="/compete" component={PageWrapper(ListTournaments)} />
              <Route exact path="/compete/:tournamentName/leaderboard" component={PageWrapper(TournamentLeaderboard)} />
              <Route exact path="/compete/:tournamentName/:problemName" component={PageWrapper(Problem)} />
              <Route exact path="/compete/:tournamentName" component={PageWrapper(ListProblems)} />
              <Route exact path="/compete/:categoryName/:problemName/view-results" component={PageWrapper(ViewResults)} />

              <Route exact path="/leaderboard" component={PageWrapper(GlobalLeaderboard)} />

              <Route component={PageWrapper(PageNotFound)} />
            </Switch>
          </div>
        </div>
      </Router>
    );
  }
}

export default App;
