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
import ManageCategory from './ManageCategory/ManageCategory';
import Leaderboard from './Problem/Leaderboard/Leaderboard';
import TournamentLeaderboard from './TournamentLeaderboard/TournamentLeaderboard';
import ProfilePage from './Users/ProfilePage/ProfilePage';

class App extends Component {
  render() {
    return (
      <Router>
        <div style={{ margin: '0', padding: '0' }}>
          <NavBar />
          <div style={{minHeight:'100%'}}>
            <Switch>
              <Route exact path="/login" component={Login} />
              <Route exact path="/problem" component={Problem} />

              <Route exact path="/user/:username" component={ProfilePage} />

              <Route exact path="/manage" component={ManageContent} />
              <Route exact path="/manage/problems" component={ManageProblems} />
              <Route exact path="/manage/problems/add" component={AddProblem} />
              <Route exact path="/manage/problems/edit/:problemName" component={AddProblem} />
              <Route exact path="/manage/problems/:problemName/test-cases" component={ManageTestCases} />

              <Route exact path="/manage/tournaments" component={ManageTournaments} />
              <Route exact path="/manage/categories" component={ManageCategory} />

              <Route exact path="/practise/:categoryName/:problemName" component={Problem} />
              <Route exact path="/practise/:categoryName/:problemName/view-results" component={ViewResults} />
              <Route exact path="/practise/:categoryName" component={ListProblems} />
              <Route exact path="/practise" component={ListCategories} />

              <Route exact path="/compete/create-tournament" component={CreateTournament} />
              <Route exact path="/compete/manage-tournaments/:tournamentName/:problemName/test-cases" component={ManageTestCases} />
              <Route exact path="/compete/manage-tournaments/:tournamentName/edit/:problemName" component={AddProblem} />
              <Route exact path="/compete/manage-tournaments/:tournamentName/add" component={AddProblem} />
              <Route exact path="/compete/manage-tournaments/:tournamentName" component={ManageProblems} />
              <Route exact path="/compete/manage-tournaments" component={ManageTournaments} />

              <Route exact path="/compete" component={ListTournaments} />
              <Route exact path="/compete/:tournamentName/leaderboard" component={TournamentLeaderboard} />
              <Route exact path="/compete/:tournamentName/:problemName" component={Problem} />
              <Route exact path="/compete/:tournamentName" component={ListProblems} />
              <Route exact path="/compete/:categoryName/:problemName/view-results" component={ViewResults} />

              <Route exact path="/leaderboard" component={GlobalLeaderboard} />

              <Route component={PageNotFound} />
            </Switch>
          </div>
          <Footer />
        </div>
      </Router>
    );
  }
}

export default App;
