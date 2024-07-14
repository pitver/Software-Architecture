import React from "react";
import { Route, Switch } from 'react-router-dom';
import EditAvatarPopup from './EditAvatarPopup';
import EditProfilePopup from './EditProfilePopup';

const App = () => (
    <div>
        <Switch>
            <Route path="/EditAvatarPopupgin" component={EditAvatarPopup} />
            <Route path="/EditProfilePopup" component={EditProfilePopup} />
        </Switch>
    </div>
);

export default App;
