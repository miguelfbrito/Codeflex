import React from 'react';
import { Redirect } from 'react-router-dom';
import { isRegistered, splitUrl, getAuthorization, parseLocalJwt } from '../commons/Utils';


const IsUserRegisteredOnTournament = Page => {
    console.log("123123 A VERIFICAR REGISTO DO UTILIZADOR ")
    let url = splitUrl(window.location.pathname);
    console.log(url);
    let registered = false;
    if (typeof url[1] != "undefined") {
        fetch(URL + '/api/database/rating/isUserRegisteredInTournamentByNameRE/' + parseLocalJwt().username + "/" + url[1], {
            headers: new Headers({
                ...getAuthorization(),
                'Content-Type': 'application/json'
            })
        }).then(res => {
            if (res.status === 200) {
                console.log("REGISTADO")
                return props => (
                    <Page {...props} />
                );
            } else {
                console.log("NAO ESTA REGISTADo")
                return props => (
                    <h1>Não entras</h1>
                );
                return false;
            }
        })
    }
};

export default IsUserRegisteredOnTournament;