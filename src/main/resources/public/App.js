// En App.js
import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import SalaDeSubastas from './SalaDeSubastas';

function App() {
    return (
        <Router>
            <div className="App">
                <header className="App-header">
                    <h1>¡Bienvenido a mi aplicación de apuestas!</h1>
                </header>
                <main>
                    <Switch>
                        <Route path="/sala" component={SalaDeSubastas} />

                    </Switch>
                </main>
            </div>
        </Router>
    );
}

export default App;
