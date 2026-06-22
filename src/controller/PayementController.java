package controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import dao.OrderDAO; // Importing your existing OrderDAO
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;

public class PayementController {
    
    // 1. EXIT POINT: Start this server when the application boots up
    public static void startPaymentServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            
            // Handles successful payments
            server.createContext("/success", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String query = exchange.getRequestURI().getQuery();
                    System.out.println("Payment Success Query Data: " + query);
                    
                    // TODO: Extract transaction_uuid from query if needed
                    // 2. DATABASE UPDATE: Call your existing OrderDAO to update the DB status
                    OrderDAO orderDao = new OrderDAO();
                    // Example: orderDao.updatePaymentStatus(transactionUuid, "Paid");
                    
                    String response = "<html><body style='text-align:center;font-family:sans-serif;padding-top:50px;'>"
                            + "<h1 style='color:green;'>Payment Successful!</h1>"
                            + "<p>Re-WEAR has updated your order status. You can close this browser window now.</p>"
                            + "</body></html>";
                    sendResponse(exchange, response);
                }
            });
            
            // Handles cancelled/failed payments
            server.createContext("/failure", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String response = "<html><body style='text-align:center;font-family:sans-serif;padding-top:50px;'>"
                            + "<h1 style='color:red;'>Payment Cancelled</h1>"
                            + "<p>Please return to the Re-WEAR app and try again.</p>"
                            + "</body></html>";
                    sendResponse(exchange, response);
                }
            });
            
            server.start();
            System.out.println("Payment server running safely on port 8000...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. ENTRY POINT: Call this method when the user clicks 'Checkout' with eSewa selected
    public void processEsewaPayment(String totalAmount) {
        try {
            String transactionUuid = UUID.randomUUID().toString(); // Unique Order Reference ID
            String signature = EsewaUtils.generateSignature(totalAmount, transactionUuid);
            
            String successUrl = "http://localhost:8000/success";
            String failureUrl = "http://localhost:8000/failure";
            
            // Hidden HTML form that auto-submits to eSewa gateway portal
            String htmlForm = "<html><body onload='document.forms[0].submit()'>"
                + "<form action='https://rc-epay.esewa.com.np/api/epay/main/v2/form' method='POST'>"
                + "<input type='hidden' name='amount' value='" + totalAmount + "'>"
                + "<input type='hidden' name='tax_amount' value='0'>"
                + "<input type='hidden' name='total_amount' value='" + totalAmount + "'>"
                + "<input type='hidden' name='transaction_uuid' value='" + transactionUuid + "'>"
                + "<input type='hidden' name='product_code' value='" + EsewaUtils.MERCHANT_CODE + "'>"
                + "<input type='hidden' name='product_service_charge' value='0'>"
                + "<input type='hidden' name='product_delivery_charge' value='0'>"
                + "<input type='hidden' name='success_url' value='" + successUrl + "'>"
                + "<input type='hidden' name='failure_url' value='" + failureUrl + "'>"
                + "<input type='hidden' name='signed_field_names' value='total_amount,transaction_uuid,product_code'>"
                + "<input type='hidden' name='signature' value='" + signature + "'>"
                + "</form></body></html>";
                
            File tempFile = File.createTempFile("esewa_trigger", ".html");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(htmlForm);
            }
            
            Desktop.getDesktop().browse(tempFile.toURI()); // Opens user's browser
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}