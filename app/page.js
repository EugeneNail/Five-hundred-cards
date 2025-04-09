"use client"

import React, { useState, useEffect } from 'react';

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
      fetch(`http://localhost:8080/api/state?player=${name}`)
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

    fetch('http://localhost:8080/api/login', {
      method: 'POST',
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
    fetch('http://localhost:8080/api/start', {
      method: 'POST'
    })
        .catch(err => console.error(err));
  };

  // Сыграть карту
  const handlePlayCard = (card) => {
    if (gameState.isLeader) return;

    fetch('http://localhost:8080/api/play', {
      method: 'POST',
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
    fetch('http://localhost:8080/api/choose', {
      method: 'POST',
      body: `${name}:${card}`,
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
          <h1>Карточная игра</h1>
          <button onClick={handleLogin}>Войти в игру</button>
        </div>
    );
  }

  if (!gameState) {
    return <div>Загрузка...</div>;
  }

  return (
      <div className="game-container">
        <h1>Карточная игра</h1>
        <p>Вы вошли как: {name}</p>
        <p>Очки: {gameState.scores?.[name] || 0}</p>

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
              <div className="red-card">
                <h3>Красная карта:</h3>
                <p>{gameState.currentRedCard}</p>
                <p>Ведущий: {gameState.currentRoundLeader}</p>
              </div>

              {gameState.isLeader ? (
                  <div className="leader-view">
                    <h3>Вы - ведущий!</h3>
                    <p>Выберите лучшую карту:</p>
                    <div className="played-cards">
                      {Object.entries(gameState.playedCards || {}).map(([player, card]) => (
                          <div
                              key={card}
                              className={`card ${winnerCard === card ? 'selected' : ''}`}
                              onClick={() => handleChooseWinner(card)}
                          >
                            {card}
                            <small>(от {player})</small>
                          </div>
                      ))}
                    </div>
                  </div>
              ) : (
                  <div className="player-view">
                    <h3>Ваши карты:</h3>
                    <div className="hand">
                      {(gameState.myCards || []).map((card, index) => (
                          <div
                              key={index}
                              className={`card ${selectedCard === card ? 'played' : ''}`}
                              onClick={() => !selectedCard && handlePlayCard(card)}
                          >
                            {card}
                          </div>
                      ))}
                    </div>
                    {selectedCard && <p>Вы сыграли карту: {selectedCard}</p>}
                  </div>
              )}

              <div className="scoreboard">
                <h3>Таблица очков:</h3>
                <ul>
                  {Object.entries(gameState.scores || {}).map(([player, score]) => (
                      <li key={player}>
                        {player}: {score} {gameState.winningPlayer === player && '★'}
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