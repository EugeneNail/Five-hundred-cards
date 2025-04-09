import java.util.*;
import java.util.concurrent.*;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;


public class CardGameServer {
    private static final int WHITE_CARDS_COUNT = 500;
    private static final int RED_CARDS_COUNT = 100;
    private static final int CARDS_PER_PLAYER = 10;

    private static Map<String, Player> players = new ConcurrentHashMap<>();
    private static List<String> whiteCards = new ArrayList<>();
    private static List<String> redCards = new ArrayList<>();
    private static GameState gameState = new GameState();

    public static void main(String[] args) throws Exception {
        // Инициализация карт
        for (int i = 0; i < WHITE_CARDS_COUNT; i++) {
            whiteCards.add("Белая карта #" + (i + 1));
        }
        for (int i = 0; i < RED_CARDS_COUNT; i++) {
            redCards.add("Красная карта #" + (i + 1));
        }

        Collections.shuffle(whiteCards);
        Collections.shuffle(redCards);

        // Настройка HTTP сервера
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/state", new StateHandler());
        server.createContext("/api/play", new PlayHandler());
        server.createContext("/api/choose", new ChooseHandler());
        server.createContext("/api/start", new StartHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class GameState {
        boolean gameStarted = false;
        String currentRedCard = "";
        String currentRoundLeader = "";
        Map<String, String> playedCards = new HashMap<>();
        String winningCard = "";
        String winningPlayer = "";
        List<String> playerOrder = new ArrayList<>();
        int currentRound = 0;
    }

    static class Player {
        String name;
        List<String> cards = new ArrayList<>();
        int score = 0;
        boolean isLeader = false;

        Player(String name) {
            this.name = name;
        }

        void dealCards(List<String> deck, int count) {
            for (int i = 0; i < count; i++) {
                if (!deck.isEmpty()) {
                    cards.add(deck.remove(0));
                }
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        // Обработка OPTIONS-запроса (preflight)
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 200, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String name = new Scanner(exchange.getRequestBody()).nextLine();
            String response;

            if (players.containsKey(name)) {
                // Игрок существует
                response = "{\"status\":\"existing\", \"name\":\"" + name + "\"}";
            } else {
                // Новый игрок
                Player player = new Player(name);
                player.dealCards(whiteCards, CARDS_PER_PLAYER);
                players.put(name, player);
                response = "{\"status\":\"new\", \"name\":\"" + name + "\"}";
            }

            sendResponse(exchange, 200, response);
        }
    }

    static class StateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
                // Обработка OPTIONS-запроса (preflight)
                    if ("OPTIONS".equals(exchange.getRequestMethod())) {
                        sendResponse(exchange, 200, "");
                        return;
                    }

            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // Подготовка состояния игры для отправки клиенту
            Map<String, Object> state = new HashMap<>();
            state.put("gameStarted", gameState.gameStarted);
            state.put("currentRedCard", gameState.currentRedCard);
            state.put("currentRoundLeader", gameState.currentRoundLeader);
            state.put("playedCards", gameState.playedCards);
            state.put("winningCard", gameState.winningCard);
            state.put("winningPlayer", gameState.winningPlayer);

            // Информация об игроках
            Map<String, Integer> scores = new HashMap<>();
            players.forEach((name, player) -> scores.put(name, player.score));
            state.put("scores", scores);

            // Информация о текущем игроке (из параметра запроса)
            String playerName = exchange.getRequestURI().getQuery().split("=")[1];
            Player player = players.get(playerName);
            if (player != null) {
                state.put("myCards", player.cards);
                state.put("isLeader", player.isLeader);
                state.put("myName", player.name);
            }

            String response = new Gson().toJson(state);
            sendResponse(exchange, 200, response);
        }
    }

    static class PlayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
                // Обработка OPTIONS-запроса (preflight)
                    if ("OPTIONS".equals(exchange.getRequestMethod())) {
                        sendResponse(exchange, 200, "");
                        return;
                    }

            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String[] parts = new Scanner(exchange.getRequestBody()).nextLine().split(":");
            String playerName = parts[0];
            String cardText = parts[1];

            Player player = players.get(playerName);
            if (player != null && player.cards.contains(cardText) && !player.isLeader) {
                player.cards.remove(cardText);
                gameState.playedCards.put(playerName, cardText);
                sendResponse(exchange, 200, "{\"status\":\"ok\"}");
            } else {
                sendResponse(exchange, 400, "{\"status\":\"error\"}");
            }
        }
    }

    static class ChooseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
                // Обработка OPTIONS-запроса (preflight)
                    if ("OPTIONS".equals(exchange.getRequestMethod())) {
                        sendResponse(exchange, 200, "");
                        return;
                    }

            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String[] parts = new Scanner(exchange.getRequestBody()).nextLine().split(":");
            String leaderName = parts[0];
            String winningCard = parts[1];

            if (gameState.currentRoundLeader.equals(leaderName)) {
                // Находим игрока, который сыграл эту карту
                String winningPlayer = "";
                for (Map.Entry<String, String> entry : gameState.playedCards.entrySet()) {
                    if (entry.getValue().equals(winningCard)) {
                        winningPlayer = entry.getKey();
                        break;
                    }
                }

                if (!winningPlayer.isEmpty()) {
                    players.get(winningPlayer).score++;
                    gameState.winningCard = winningCard;
                    gameState.winningPlayer = winningPlayer;

                    // Раздаем новую карту игроку
                    if (!whiteCards.isEmpty()) {
                        players.get(winningPlayer).cards.add(whiteCards.remove(0));
                    }

                    // Переход к следующему раунду
                    nextRound();
                    sendResponse(exchange, 200, "{\"status\":\"ok\"}");
                    return;
                }
            }

            sendResponse(exchange, 400, "{\"status\":\"error\"}");
        }
    }

    static class StartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            if (players.size() >= 2) {
                startGame();
                sendResponse(exchange, 200, "{\"status\":\"ok\"}");
            } else {
                sendResponse(exchange, 400, "{\"status\":\"not enough players\"}");
            }
        }
    }

    private static void startGame() {
        gameState.gameStarted = true;
        gameState.playerOrder = new ArrayList<>(players.keySet());
        Collections.shuffle(gameState.playerOrder);
        gameState.currentRound = 0;
        nextRound();
    }

    private static void nextRound() {
        gameState.currentRound++;

        // Сброс состояния раунда
        gameState.playedCards.clear();
        gameState.winningCard = "";
        gameState.winningPlayer = "";

        // Выбор нового ведущего
        int leaderIndex = (gameState.currentRound - 1) % gameState.playerOrder.size();
        String newLeader = gameState.playerOrder.get(leaderIndex);
        gameState.currentRoundLeader = newLeader;

        // Сброс флага ведущего у всех и установка новому
        players.values().forEach(p -> p.isLeader = false);
        players.get(newLeader).isLeader = true;

        // Выбор красной карты
        if (!redCards.isEmpty()) {
            gameState.currentRedCard = redCards.remove(0);
        } else {
            gameState.currentRedCard = "Игра завершена!";
            gameState.gameStarted = false;
        }

        // Раздача карт игрокам, у которых меньше 10
        players.values().forEach(p -> {
            if (p.cards.size() < CARDS_PER_PLAYER && !whiteCards.isEmpty()) {
                p.dealCards(whiteCards, CARDS_PER_PLAYER - p.cards.size());
            }
        });
    }

    private static void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        // Настройки CORS
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        
        // Для preflight запросов
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Основные заголовки
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}