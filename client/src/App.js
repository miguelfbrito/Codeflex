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
import TournamentLeaderboard from './TournamentLeaderboard/TournamentLeaderboard';
import ProfilePage from './Users/ProfilePage/ProfilePage';
import PageWrapper from './PageWrapper/PageWrapper';
import LandingPage from './LandingPage/LandingPage';
import IsUserRegisteredOnTournament from './Permissions/IsUserRegisteredOnTournament';

import './PageWrapper/PageWrapper.css';
import { parseLocalJwt, splitUrl, getAuthorization } from './commons/Utils';

class App extends Component {

    manageSectionControl = () => {
        if (this.userLoggedIn()) {
            let jwt = parseLocalJwt();
            if (typeof jwt.role != 'undefined' && jwt.role === 'CONTENT_MANAGER') {
                return true;
            }
        }
        return false;
    }

    componentDidUpdate() {
        console.log("UPDATING")
    }

    isUserTournamentOwner = () => {
        let url = splitUrl(window.location.pathname);
        if (typeof url[2] != "undefined") {
            fetch(URL + '/api/database/tournament/isUserTournamentOwner/' + url[2] + "/" + parseLocalJwt().username, {
                headers: new Headers({
                    ...getAuthorization(),
                    'Content-Type': 'application/json'
                })
            }).then(res => {
                if (res.status === 200) {
                    return true;
                }
                return false;
            })
        }
    }

    userLoggedIn = () => {
        return localStorage.getItem('token') != null ? true : false;
    }

    render() {


        if (this.userLoggedIn()) {
            return (
                <Router>
                    <div style={{ margin: '0', padding: '0' }}>
                        <NavBar />
                        <div style={{ minHeight: '100%' }}>
                            <Switch>
                                {
                                    console.log(this.props)}
                                <Route exact path="/" component={PageWrapper(LandingPage)} />

                                <Route exact path="/login" component={PageWrapper(Login)} />
                                <Route exact path="/leaderboard" component={PageWrapper(GlobalLeaderboard)} />

                                <Route exact path="/user/:username" component={PageWrapper(ProfilePage)} />

                                <Route exact path="/practise/:categoryName/:problemName/view-results" component={PageWrapper(ViewResults)} />
                                <Route exact path="/practise/:categoryName/:problemName" component={PageWrapper(Problem)} />
                                <Route exact path="/practise/:categoryName" component={PageWrapper(ListProblems)} />
                                <Route exact path="/practise" component={PageWrapper(ListCategories)} />

                                <Route exact path="/compete/create-tournament" component={PageWrapper(CreateTournament)} />

                                {/*verify if user is the owner */}
                                {this.isUserTournamentOwner() ? <Route exact path="/compete/manage-tournaments/:tournamentName/:problemName/test-cases" component={PageWrapper(ManageTestCases)} /> : ''}
                                {this.isUserTournamentOwner() ? <Route exact path="/compete/manage-tournaments/:tournamentName/edit/:problemName" component={PageWrapper(AddProblem)} /> : ''}
                                {this.isUserTournamentOwner() ? <Route exact path="/compete/manage-tournaments/:tournamentName/edit" component={PageWrapper(CreateTournament)} /> : ''}
                                {this.isUserTournamentOwner() ? <Route exact path="/compete/manage-tournaments/:tournamentName/add" component={PageWrapper(AddProblem)} /> : ''}
                                {this.isUserTournamentOwner() ? <Route exact path="/compete/manage-tournaments/:tournamentName" component={PageWrapper(ManageProblems)} /> : ''}

                                <Route exact path="/compete/manage-tournaments" component={PageWrapper(ManageTournaments)} />

                                <Route exact path="/compete" component={PageWrapper(ListTournaments)} />

                                {/*verify if user is registered in the tournament */}
                                <Route exact path="/compete/:tournamentName/leaderboard" component={IsUserRegisteredOnTournament(PageWrapper(TournamentLeaderboard))} />
                                <Route exact path="/compete/:tournamentName/edit" component={IsUserRegisteredOnTournament(PageWrapper(CreateTournament))} />
                                <Route exact path="/compete/:tournamentName/:problemName" component={IsUserRegisteredOnTournament(PageWrapper(Problem))} />
                                <Route exact path="/compete/:tournamentName" component={IsUserRegisteredOnTournament(PageWrapper(ListProblems))} />
                                <Route exact path="/compete/:tournamentName/:problemName/view-results" component={IsUserRegisteredOnTournament(PageWrapper(ViewResults))} />


                                {console.log(window.location.href)}
                                {/* REACT.FRAGMENT is breaking the switch */}
                                {this.manageSectionControl() ? <Route exact path="/manage" component={PageWrapper(ManageContent)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/problems" component={PageWrapper(ManageProblems)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/problems/add" component={PageWrapper(AddProblem)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/problems/edit/:problemName" component={PageWrapper(AddProblem)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/problems/:problemName/test-cases" component={PageWrapper(ManageTestCases)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/tournaments" component={PageWrapper(ManageTournaments)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/tournaments/add" component={PageWrapper(CreateTournament)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/tournaments/:tournamentName/edit" component={PageWrapper(CreateTournament)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/tournaments/:tournamentName" component={PageWrapper(ManageProblems)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/tournaments/:tournamentName/:problemName" component={PageWrapper(ManageProblems)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/tournaments/:tournamentName/:problemName/test-cases" component={PageWrapper(ManageTestCases)} /> : ''}
                                {this.manageSectionControl() ? <Route exact path="/manage/categories" component={PageWrapper(ManageCategories)} /> : ''}


                                <Route exact path="/404" component={PageWrapper(PageNotFound)} />
                                <Route component={PageWrapper(PageNotFound)} />
                            </Switch>
                        </div>
                        <div style={{ marginBottom: '75px' }}></div>
                    </div>
                </Router >
            )
        } else {
            return (

                <Router>
                    <div style={{ margin: '0', padding: '0' }}>
                        <NavBar />
                        <div style={{ minHeight: '100%' }}>
                            <Switch>
                                <Route exact path="/" component={PageWrapper(LandingPage)} />
                                <Route exact path="/login" component={PageWrapper(Login)} />
                                <Route exact path="/user/:username" component={PageWrapper(ProfilePage)} />
                                <Route component={PageWrapper(PageNotFound)} />
                            </Switch>
                        </div>
                        <div style={{ marginBottom: '75px' }}></div>
                    </div>
                </Router>
            )
        }
    }
}

export default App;
