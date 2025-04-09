"use client"

import "./app.sass"

import React, {useState, useEffect} from 'react';

function CardGame() {
    const [name, setName] = useState('');
    const [loggedIn, setLoggedIn] = useState(false);
    const [gameState, setGameState] = useState(null);
    const [selectedCard, setSelectedCard] = useState(null);
    const [winnerCard, setWinnerCard] = useState(null);

    // Запрос состояния игры каждые 1.5 секунды
    useEffect(() => {
        if (!loggedIn) return;

        const interval = setInterval(() => {
            fetch(`http://176.118.232.69:8080/api/state?player=${name}`)
                .then(res => res.json())
                .then(data => setGameState(data))
                .catch(err => console.error(err));
        }, 1500);

        return () => clearInterval(interval);
    }, [loggedIn, name]);

    // Обработка входа игрока
    const handleLogin = () => {
        const playerName = prompt('Введите ваше имя:');
        if (!playerName) return;

        fetch('http://176.118.232.69:8080/api/login', {
            method: 'POST',
            mode: 'cors',
            body: playerName,
            headers: {
                'Content-Type': 'text/plain'
            }
        })
            .then(res => res.json())
            .then(data => {
                setName(data.name);
                setLoggedIn(true);
            })
            .catch(err => console.error(err));
    };

    // Начать игру
    const handleStartGame = () => {
        fetch('http://176.118.232.69:8080/api/start', {
            mode: 'cors',
            method: 'POST'
        })
            .catch(err => console.error(err));
    };

    // Сыграть карту
    const handlePlayCard = (card) => {
        if (gameState.isLeader) return;

        fetch('http://176.118.232.69:8080/api/play', {
            method: 'POST',
            mode: 'cors',
            body: `${name}:${card}`,
            headers: {
                'Content-Type': 'text/plain'
            }
        })
            .then(() => setSelectedCard(card))
            .catch(err => console.error(err));
    };

    // Выбрать победившую карту (для ведущего)
    const handleChooseWinner = (card) => {
        fetch('http://176.118.232.69:8080/api/choose', {
            method: 'POST',
            body: `${name}:${card}`,
            mode: 'cors',
            headers: {
                'Content-Type': 'text/plain'
            }
        })
            .then(() => setWinnerCard(card))
            .catch(err => console.error(err));
    };

    if (!loggedIn) {
        return (
            <div className="login-screen">
                <div className="login-screen__container">
                    <img src="./logo-black.png" alt="" className="login-screen__logo"/>
                    <h1 className="login-screen__title">...злобных карт</h1>
                    <h3 className="login-screen__subtitle">Сделано лучшим в мире разработчиком -- мной</h3>
                </div>

                <div className="start-button" onClick={handleLogin}>
                    <div className="start-button__face">Войти в игру</div>
                    <div className="start-button__bottom">Войти в игру</div>
                </div>
            </div>
        );
    }

    if (!gameState) {
        return <div>Загрузка...</div>;
    }

    return (
        <div className="game-container">
            {!gameState.gameStarted && (
                <div className="lobby">
                    <h2>Лобби</h2>
                    <p>Игроков онлайн: {Object.keys(gameState.scores || {}).length}</p>
                    {Object.keys(gameState.scores || {}).length >= 2 && (
                        <button onClick={handleStartGame}>Начать игру</button>
                    )}
                </div>
            )}

            {gameState.gameStarted && (
                <div className="game-area">

                    {gameState.isLeader && (
                        <div className="leader">
                            <p className="leader__title">Вы ведущий</p>
                            <p className="leader__subtitle">Выберите наиболее забавную карту</p>
                        </div>
                    )}

                    <div className="field">
                        <div className="field-card-container">
                            <div className="card red-card">
                                <img src="./logo-white.png" alt="" className="card__logo"/>
                                <p className="card__text">{gameState.currentRedCard}</p>
                            </div>
                            <small>
                                <img src="./person.png" alt="" className="player-icon"/>
                                {gameState.currentRoundLeader}
                            </small>
                        </div>

                        {Object.entries(gameState.playedCards || {}).map(([player, card]) => (
                            <div className="field-card-container">
                                <div
                                    key={card}
                                    className={`card field-card ${gameState.isLeader ? 'pointer' : ''}`}
                                    onClick={() => handleChooseWinner(card)}
                                >
                                    {(gameState.isLeader || name === player) && (
                                        <p className="card__text">{card}</p>
                                    )}
                                    <img src="./logo-black.png" alt="" className={`card__logo ${gameState.isLeader || name === player ? 'card__logo' : 'field-card__logo'}`}/>
                                </div>
                                <small>
                                    <img src="./person.png" alt="" className="player-icon"/>
                                    {player}
                                </small>
                            </div>
                        ))}
                    </div>


                    {!gameState.isLeader && (
                        <div className="player-view">
                            <div className="player-view__hand">
                                {(gameState.myCards || []).map((card, index) => (
                                    <div
                                        key={index}
                                        className={`card player-card ${selectedCard === card ? 'played' : ''}`}
                                        onClick={() => !selectedCard && handlePlayCard(card)}
                                    >
                                        <p className="card__text">{card}</p>
                                        <img src="./logo-black.png" alt="" className="card__logo"/>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    <div className="scoreboard">
                        <ul>
                            {Object.entries(gameState.scores || {}).map(([player, score]) => (
                                <li className="player-score" key={player}>
                                    <img src="./person.png" alt="" className="player-icon"/>
                                    {player == name ? '(Я)' : ''} {player}: {score} {gameState.winningPlayer === player && '★'}
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            )}
        </div>
    );
}

export default CardGame;