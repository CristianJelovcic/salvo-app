/*    fetch('/api/games/players/'+gpId +'/ships', {
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(app.placedShips)
    })
        .then(function (response) {
            if (response.status == 201)
                location.reload();
            else {
                return response.json();
            }
        })
        .then(function (json) {
            console.log(json);
        })
        .catch(function (er) {
            console.log(er);
        })*/