import React from "react";
import { Route, Switch } from 'react-router-dom';
import Card from './Card';
import AddPlacePopup from './AddPlacePopup';
import ImagePopup from './ImagePopup';


const App = () => (
    <div>
        <Switch>
            <Route path="/Card" component={Card} />
            <Route path="/AddPlacePopup" component={AddPlacePopup} />
            <Route path="/ImagePopup" component={ImagePopup} />
        </Switch>
    </div>
);

export default App;


