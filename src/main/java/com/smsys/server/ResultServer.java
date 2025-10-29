package com.smsys.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResultServer {
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // API endpoint for result calculation
        server.createContext("/api/calculate", new CalculateHandler());
        
        // Static file handler for the frontend
        server.createContext("/", new StaticHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("Server started on port " + PORT);
        System.out.println("Frontend available at: http://localhost:" + PORT);
        System.out.println("API endpoint: http://localhost:" + PORT + "/api/calculate");
    }
    
    static class CalculateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Enable CORS for web deployment
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            try {
                String method = exchange.getRequestMethod();
                Map<String, String> params = new HashMap<>();
                
                if ("POST".equals(method)) {
                    // Read POST body
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    params = parseQueryString(body);
                } else if ("GET".equals(method)) {
                    // Read GET query parameters
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null) {
                        params = parseQueryString(query);
                    }
                }
                
                // Calculate result
                ResultCalculator calculator = new ResultCalculator();
                String response = calculator.calculateResult(params);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
                
            } catch (Exception e) {
                String errorResponse = "{\"error\": \"" + e.getMessage() + "\"}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(500, errorResponse.getBytes(StandardCharsets.UTF_8).length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        
        private Map<String, String> parseQueryString(String query) {
            Map<String, String> params = new HashMap<>();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    params.put(key, value);
                }
            }
            return params;
        }
    }
    
    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            // Default to index.html for root path
            if ("/".equals(path)) {
                path = "/index.html";
            }
            
            // Serve static files from the public directory
            try {
                String content = getStaticContent(path);
                String contentType = getContentType(path);
                
                exchange.getResponseHeaders().add("Content-Type", contentType);
                exchange.sendResponseHeaders(200, content.getBytes(StandardCharsets.UTF_8).length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(content.getBytes(StandardCharsets.UTF_8));
                }
                
            } catch (Exception e) {
                // Return 404 for missing files
                String response = "File not found: " + path;
                exchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        
        private String getStaticContent(String path) throws IOException {
            // This is a simplified version - in production, you'd want to read from actual files
            // For now, we'll return the HTML content directly
            if (path.endsWith("index.html")) {
                return getIndexHtml();
            } else if (path.endsWith("styles.css")) {
                return getStylesCss();
            } else if (path.endsWith("script.js")) {
                return getScriptJs();
            }
            throw new IOException("File not found: " + path);
        }
        
        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            return "text/plain";
        }
        
        // Static content methods - in production, read from actual files
        private String getIndexHtml() {
            return "<!DOCTYPE html>\n" +
                   "<html lang=\"en\">\n" +
                   "  <head>\n" +
                   "    <meta charset=\"UTF-8\" />\n" +
                   "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                   "    <title>Student Result Management System</title>\n" +
                   "    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\" />\n" +
                   "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin />\n" +
                   "    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&display=swap\" rel=\"stylesheet\" />\n" +
                   "    <link rel=\"stylesheet\" href=\"styles.css\" />\n" +
                   "  </head>\n" +
                   "  <body>\n" +
                   "    <header class=\"navbar\">\n" +
                   "      <div class=\"container nav-inner\">\n" +
                   "        <div class=\"brand\">SRMS</div>\n" +
                   "        <div class=\"nav-quote\">Success comes from hard work.</div>\n" +
                   "        <nav>\n" +
                   "          <a href=\"#home\" class=\"nav-link\">Home</a>\n" +
                   "          <a href=\"#form\" class=\"nav-link\">Form</a>\n" +
                   "          <a href=\"#results\" class=\"nav-link\">Results</a>\n" +
                   "        </nav>\n" +
                   "      </div>\n" +
                   "    </header>\n" +
                   "    <main>\n" +
                   "      <section id=\"home\" class=\"hero\">\n" +
                   "        <div class=\"hero-bg\">\n" +
                   "          <div class=\"float float-1\"></div>\n" +
                   "          <div class=\"float float-2\"></div>\n" +
                   "          <div class=\"float float-3\"></div>\n" +
                   "        </div>\n" +
                   "        <div class=\"container hero-content\">\n" +
                   "          <h1 class=\"title\">Student Result Management System</h1>\n" +
                   "          <p class=\"subtitle\">Manage academic records, calculate marks and grades, and generate reports — all in one intuitive platform.</p>\n" +
                   "          <a href=\"#form\" class=\"btn btn-primary\">Get Started</a>\n" +
                   "        </div>\n" +
                   "      </section>\n" +
                   "      <section id=\"form\" class=\"form-section container\">\n" +
                   "        <h2 class=\"section-title\">Enter Student Details</h2>\n" +
                   "        <form id=\"result-form\" class=\"card\">\n" +
                   "          <div class=\"grid\">\n" +
                   "            <div class=\"field\">\n" +
                   "              <label for=\"name\">Student Name</label>\n" +
                   "              <input id=\"name\" name=\"name\" type=\"text\" placeholder=\"e.g., Alex Johnson\" required />\n" +
                   "            </div>\n" +
                   "            <div class=\"field\">\n" +
                   "              <label for=\"roll\">Roll Number</label>\n" +
                   "              <input id=\"roll\" name=\"roll\" type=\"text\" placeholder=\"e.g., 23CS1005\" required />\n" +
                   "            </div>\n" +
                   "          </div>\n" +
                   "          <h3 class=\"subtle-heading\">Marks (0 - 100)</h3>\n" +
                   "          <div class=\"grid\">\n" +
                   "            <div class=\"field\">\n" +
                   "              <label for=\"math\">Mathematics</label>\n" +
                   "              <input id=\"math\" name=\"math\" type=\"number\" min=\"0\" max=\"100\" placeholder=\"90\" required />\n" +
                   "            </div>\n" +
                   "            <div class=\"field\">\n" +
                   "              <label for=\"physics\">Physics</label>\n" +
                   "              <input id=\"physics\" name=\"physics\" type=\"number\" min=\"0\" max=\"100\" placeholder=\"85\" required />\n" +
                   "            </div>\n" +
                   "            <div class=\"field\">\n" +
                   "              <label for=\"chemistry\">Chemistry</label>\n" +
                   "              <input id=\"chemistry\" name=\"chemistry\" type=\"number\" min=\"0\" max=\"100\" placeholder=\"88\" required />\n" +
                   "            </div>\n" +
                   "            <div class=\"field\">\n" +
                   "              <label for=\"english\">English</label>\n" +
                   "              <input id=\"english\" name=\"english\" type=\"number\" min=\"0\" max=\"100\" placeholder=\"78\" required />\n" +
                   "            </div>\n" +
                   "            <div class=\"field\">\n" +
                   "              <div class=\"field\">\n" +
                   "                <label for=\"cs\">Computer Science</label>\n" +
                   "                <input id=\"cs\" name=\"cs\" type=\"number\" min=\"0\" max=\"100\" placeholder=\"92\" required />\n" +
                   "              </div>\n" +
                   "            </div>\n" +
                   "          </div>\n" +
                   "          <button type=\"submit\" class=\"btn btn-accent\">Calculate Result</button>\n" +
                   "        </form>\n" +
                   "      </section>\n" +
                   "      <section id=\"results\" class=\"results-section container\">\n" +
                   "        <h2 class=\"section-title\">Result</h2>\n" +
                   "        <div id=\"result-card\" class=\"card hidden\">\n" +
                   "          <div class=\"result-header\">\n" +
                   "            <div class=\"avatar\">🎓</div>\n" +
                   "            <div>\n" +
                   "              <div class=\"result-name\" id=\"res-name\">—</div>\n" +
                   "              <div class=\"result-roll\" id=\"res-roll\">—</div>\n" +
                   "            </div>\n" +
                   "          </div>\n" +
                   "          <div class=\"result-stats\">\n" +
                   "            <div class=\"stat\">\n" +
                   "              <div class=\"stat-label\">Total</div>\n" +
                   "              <div class=\"stat-value\" id=\"res-total\">—</div>\n" +
                   "            </div>\n" +
                "            <div class=\"stat\">\n" +
                "              <div class=\"stat-label\">Percentage</div>\n" +
                   "              <div class=\"stat-value\" id=\"res-percentage\">—</div>\n" +
                   "            </div>\n" +
                   "            <div class=\"stat\">\n" +
                   "              <div class=\"stat-label\">Grade</div>\n" +
                   "              <div class=\"stat-value\" id=\"res-grade\">—</div>\n" +
                   "            </div>\n" +
                   "          </div>\n" +
                   "        </div>\n" +
                   "      </section>\n" +
                   "    </main>\n" +
                   "    <footer class=\"footer\">\n" +
                   "      <div class=\"container\">Learning today, leading tomorrow. © <span id=\"year\"></span></div>\n" +
                   "    </footer>\n" +
                   "    <script src=\"script.js\"></script>\n" +
                   "  </body>\n" +
                   "</html>";
        }
        
        private String getStylesCss() {
            return "/* CSS content would go here */\n" +
                   "body { font-family: 'Inter', sans-serif; }\n" +
                   ".container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }\n" +
                   ".navbar { background: #1a1a1a; color: white; padding: 1rem 0; }\n" +
                   ".hero { padding: 4rem 0; text-align: center; }\n" +
                   ".btn { padding: 12px 24px; border-radius: 8px; text-decoration: none; display: inline-block; }\n" +
                   ".btn-primary { background: #007bff; color: white; }\n" +
                   ".card { background: white; padding: 2rem; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }";
        }
        
        private String getScriptJs() {
            return "// JavaScript content would go here\n" +
                   "document.getElementById('year').textContent = new Date().getFullYear();\n" +
                   "// Form handling and API calls would be implemented here";
        }
    }
}


