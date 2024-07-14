import React from "react";
import { Route, Switch } from 'react-router-dom';
import Login from './Login';
import Register from './Register';

const App = () => (
    <div>
        <Switch>
            <Route path="/login" component={Login} />
            <Route path="/register" component={Register} />
        </Switch>
    </div>
);

export default App;
