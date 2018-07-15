import React from 'react';
import { Redirect } from 'react-router-dom';
import { isRegistered } from '../commons/Utils';

const IsUserRegisteredOnTournament = Page => {

    return props =>
        <div>
            {isRegistered() ? <Redirect to="/404" /> :
                <Page {...props} />
            }
        </div>
};

export default IsUserRegisteredOnTournament;