import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

import NavBar from './Navbar/Navbar';
import Problem from './Problem/Problem';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <NavBar />
          <div>
            <Route path="/problem" component={Problem} />
          </div>
        </div>

      </Router>
    );
  }
}

export default App;
