import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CalculatorServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Faqja kryesore (HTML)
        server.createContext("/", ex -> {
            byte[] html = Files.readAllBytes(Paths.get("index.html"));
            send(ex, 200, "text/html; charset=utf-8", html);
        });

        // Logjika e kalkulatorit
        server.createContext("/calculate", ex -> {
            Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
            String json;
            try {
                BigDecimal a = new BigDecimal(q.get("a"));
                BigDecimal b = new BigDecimal(q.get("b"));
                String result = calculate(a, q.get("op"), b);
                json = "{\"result\":\"" + result + "\"}";
            } catch (ArithmeticException e) {
                json = "{\"error\":\"" + e.getMessage() + "\"}";
            } catch (Exception e) {
                json = "{\"error\":\"Hyrje e pavlefshme\"}";
            }
            send(ex, 200, "application/json; charset=utf-8", json.getBytes(StandardCharsets.UTF_8));
        });

        server.start();
        System.out.println("Serveri po punon: http://localhost:8080");
    }

    static String calculate(BigDecimal a, String op, BigDecimal b) {
        BigDecimal r;
        switch (op) {
            case "add": r = a.add(b); break;
            case "sub": r = a.subtract(b); break;
            case "mul": r = a.multiply(b); break;
            case "div":
                if (b.signum() == 0) throw new ArithmeticException("Pjesëtim me 0");
                r = a.divide(b, MathContext.DECIMAL64);
                break;
            case "mod":
                if (b.signum() == 0) throw new ArithmeticException("Pjesëtim me 0");
                r = a.remainder(b);
                break;
            default:
                throw new ArithmeticException("Operator i panjohur");
        }
        r = r.stripTrailingZeros();
        return r.signum() == 0 ? "0" : r.toPlainString();
    }

    static Map<String, String> parseQuery(String raw) {
        Map<String, String> map = new HashMap<>();
        if (raw == null) return map;
        for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    static void send(HttpExchange ex, int code, String type, byte[] body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", type);
        ex.sendResponseHeaders(code, body.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body);
        }
    }
}