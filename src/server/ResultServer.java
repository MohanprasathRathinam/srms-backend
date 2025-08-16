package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ResultServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Set up API endpoint BEFORE the catch-all static handler
        server.createContext("/api/calculate", new CalculateHandler());
        
        // Set up static file handler for everything else
        server.createContext("/", new StaticHandler());

        server.setExecutor(null);
        System.out.println("Server running at http://localhost:" + PORT);
        System.out.println("API endpoint: http://localhost:" + PORT + "/api/calculate");
        server.start();
    }

    static class StaticHandler implements HttpHandler {
        private final Path baseDir;

        StaticHandler() {
            String userDir = System.getProperty("user.dir");
            this.baseDir = Path.of(userDir, "public");
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Only handle GET requests for static files
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                sendText(exchange, 405, "Method Not Allowed");
                return;
            }

            String rawPath = exchange.getRequestURI().getPath();
            if (rawPath.equals("/")) rawPath = "/index.html";

            Path target = baseDir.resolve(rawPath.substring(1)).normalize();
            if (!target.startsWith(baseDir) || !Files.exists(target) || Files.isDirectory(target)) {
                sendText(exchange, 404, "Not Found");
                return;
            }

            String contentType = contentTypeFor(target.toString());
            byte[] bytes = Files.readAllBytes(target);
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
        }
    }

    static class CalculateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("API request received: " + exchange.getRequestMethod() + " " + exchange.getRequestURI().getPath());

            String method = exchange.getRequestMethod();

            // Handle CORS preflight
            if (method.equalsIgnoreCase("OPTIONS")) {
                addCors(exchange);
                exchange.sendResponseHeaders(204, -1); // No content
                exchange.close();
                return;
            }

            try {
                Map<String, String> form;
                if (method.equalsIgnoreCase("POST")) {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println("Request body: " + body);
                    form = parseFormEncoded(body);
                } else if (method.equalsIgnoreCase("GET")) {
                    String query = exchange.getRequestURI().getRawQuery();
                    System.out.println("Query: " + query);
                    form = parseFormEncoded(query == null ? "" : query);
                } else {
                    System.out.println("Method not allowed: " + method);
                    sendJson(exchange, 405, "{\"error\":\"Method Not Allowed. Use POST or GET.\"}");
                    return;
                }

                String name = form.getOrDefault("name", "").trim();
                String roll = form.getOrDefault("roll", "").trim();

                int math = parseInt0to100(form.get("math"));
                int physics = parseInt0to100(form.get("physics"));
                int chemistry = parseInt0to100(form.get("chemistry"));
                int english = parseInt0to100(form.get("english"));
                int cs = parseInt0to100(form.get("cs"));

                int subjectsCount = 5;
                int total = ResultCalculator.calculateTotal(math, physics, chemistry, english, cs);
                double percentage = ResultCalculator.calculatePercentage(total, subjectsCount);
                String grade = ResultCalculator.calculateGrade(percentage);

                String json = "{" +
                        "\"name\":\"" + escapeJson(name) + "\"," +
                        "\"rollNumber\":\"" + escapeJson(roll) + "\"," +
                        "\"total\":" + total + "," +
                        "\"percentage\":" + String.format(java.util.Locale.US, "%.2f", percentage) + "," +
                        "\"grade\":\"" + grade + "\"," +
                        "\"subjectsCount\":" + subjectsCount + "," +
                        "\"maxMarksPerSubject\":100}";

                System.out.println("Sending response: " + json);
                sendJson(exchange, 200, json);

            } catch (Exception e) {
                System.err.println("Error processing request: " + e.getMessage());
                e.printStackTrace();
                sendJson(exchange, 500, "{\"error\":\"Internal Server Error: " + e.getMessage() + "\"}");
            }
        }
    }

    private static void sendText(HttpExchange ex, int code, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        addCors(ex);
        ex.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        addCors(ex);
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static String contentTypeFor(String path) {
        String p = path.toLowerCase();
        if (p.endsWith(".html")) return "text/html; charset=utf-8";
        if (p.endsWith(".css")) return "text/css; charset=utf-8";
        if (p.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (p.endsWith(".png")) return "image/png";
        if (p.endsWith(".jpg") || p.endsWith(".jpeg")) return "image/jpeg";
        if (p.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }

    private static Map<String, String> parseFormEncoded(String body) {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.isEmpty()) return map;
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx < 0) continue;
            String key = urlDecode(pair.substring(0, idx));
            String val = urlDecode(pair.substring(idx + 1));
            map.put(key, val);
        }
        return map;
    }

    private static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private static String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    private static int parseInt0to100(String s) {
        try {
            int v = Integer.parseInt(s);
            if (v < 0) return 0;
            if (v > 100) return 100;
            return v;
        } catch (Exception e) { return 0; }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int)c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}


