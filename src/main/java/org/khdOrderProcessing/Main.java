package org.khdOrderProcessing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;



public class Main {

    private static String SHOPIFY_STORE;
    private static String ACCESS_TOKEN;
    public static long lastProcessedID;

    public static void main(String[] args) {

        try {
            lastProcessedID = loadOrderID();
            System.out.println(lastProcessedID+"\nconfig file read.");
        }
        catch(IOException e) {
            System.out.println("unable to load last order ID. ");
            e.printStackTrace();
            lastProcessedID = 0;
        }
        String latestOrder = new String("");
        try {
            findNextOrder(lastProcessedID);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        /*TextImageGenerator.renderBowl("JEREMIAH",886);
        TextImageGenerator.renderPlate("JEREMIAH",880);
        TextImageGenerator.renderMug("JEREMIAH",801);
        TextImageGenerator.renderUtensils("JEREMIAH",801);
        TextImageGenerator.renderPlacemats("JEREMIAH",851);*/


    }
    public static String findNextOrder(Long sinceId) throws IOException, InterruptedException {

        //DEFINE HTTP REQUEST URL

        String url =
                    "https://" + SHOPIFY_STORE +
                            "/admin/api/2026-07/orders.json" +
                            "?since_id=" + sinceId +
                            "&status=any" +
                            "&fields=id,line_items" +
                            "&limit=250";

            //SEND HTTP API REQUEST AND ASSIGN RESPONSE TO VARIABLE "response"

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Shopify-Access-Token", ACCESS_TOKEN)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());


            //JACKSON MAPPER SETUP
            ObjectMapper mapper = new ObjectMapper();

            //PARSE VARIABLE

            OrdersResponse response1 =
                    mapper.readValue(response.body(), OrdersResponse.class);

            for (Order order : response1.orders) {
                for (LineItem item : order.line_items) {
                    String customName = null;
                    String sku = null;
                    boolean utensils = false;

                    sku = item.sku;

                    for (Property property : item.properties) {
                        if (property.name.equals("Personalization:") || property.name.equals("_Personalization:"))
                        {
                        customName = property.value;
                        customName = customName.replaceAll("[^a-zA-Z]", "");
                        //System.out.println(property.name + ": " + property.value);
                        }
                        if (property.name.equals("Matching fork & spoon:") || property.name.equals("_Matching fork & spoon:"))
                        {
                            utensils = true;
                        }

                    }
                    //System.out.println(item.sku);
                    System.out.println("SKU: "+sku+"\nNAME: "+customName);
                    if ((sku != null && sku != "") && (customName != null && customName != "")) {
                        int bgID = Integer.parseInt(sku.replaceAll("\\D",""));
                        String productType = sku.replaceAll("\\d","");

                        switch (productType) {
                            case "csetu":
                                TextImageGenerator.renderBowl(customName, bgID);
                                TextImageGenerator.renderPlate(customName, bgID);
                                TextImageGenerator.renderMug(customName, bgID);
                                TextImageGenerator.renderUtensils(customName, bgID);
                                TextImageGenerator.renderPlacemats(customName, bgID);
                                break;
                            case "cset":
                                TextImageGenerator.renderBowl(customName, bgID);
                                TextImageGenerator.renderPlate(customName, bgID);
                                TextImageGenerator.renderMug(customName, bgID);
                                TextImageGenerator.renderPlacemats(customName, bgID);
                                break;
                            case "fset":
                                TextImageGenerator.renderBowl(customName, bgID);
                                TextImageGenerator.renderPlate(customName, bgID);
                                TextImageGenerator.renderPlacemats(customName, bgID);
                                break;
                            case "bset":
                                TextImageGenerator.renderBowl(customName, bgID);
                                TextImageGenerator.renderPlacemats(customName, bgID);
                                break;
                            case "pbm":
                                TextImageGenerator.renderBowl(customName, bgID);
                                TextImageGenerator.renderPlate(customName, bgID);
                                TextImageGenerator.renderMug(customName, bgID);
                                break;
                            case "set":
                                TextImageGenerator.renderPlate(customName, bgID);
                                TextImageGenerator.renderPlacemats(customName, bgID);
                                break;
                            case "pb":
                                TextImageGenerator.renderBowl(customName, bgID);
                                TextImageGenerator.renderPlate(customName, bgID);
                                break;
                            case "u":
                                TextImageGenerator.renderUtensils(customName, bgID);
                                break;
                            case "mug":
                                TextImageGenerator.renderMug(customName, bgID);
                                break;
                            case "b":
                                TextImageGenerator.renderBowl(customName, bgID);
                                break;
                            case "plt":
                                TextImageGenerator.renderPlate(customName, bgID);
                                break;
                            case "p":
                                TextImageGenerator.renderPlacemats(customName, bgID);
                                break;
                            default:
                                System.out.println("Unable to create files, unknown SKU.");
                                break;
                        }
                        if (utensils) {
                            TextImageGenerator.renderUtensils(customName, bgID);
                        }

                    }
                    else {
                        System.out.println("Skipped order: Null Value.");
                    }
                }
                if (sinceId <= order.id) sinceId = order.id;
                System.out.println(order.id);
            }
            saveOrderID(sinceId+"\n"+SHOPIFY_STORE+"\n"+ACCESS_TOKEN);
            return response.body();
        }

    public static void saveOrderID(String value) throws IOException {
        FileWriter writer = new FileWriter("orderID.txt");
        writer.write(value);
        writer.close();
    }

    public static long loadOrderID() throws IOException {
        File file = new File("orderID.txt");

        if (!file.exists()) {
            return 0;
        }

        Scanner scanner = new Scanner(file);
        long returnValue = Long.parseLong(scanner.nextLine());
        SHOPIFY_STORE = scanner.nextLine();
        ACCESS_TOKEN = scanner.nextLine();
        scanner.close();
        if (SHOPIFY_STORE.equals(null)) System.out.println("STORE URL IS NULL.");
        if (ACCESS_TOKEN.equals(null)) System.out.println("STORE URL IS NULL.");
        return returnValue;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Property {
        public String name;
        public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrdersResponse {
        public List<Order> orders;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        public long id;
        public List<LineItem> line_items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LineItem {
        public String sku;
        public List<Property> properties;
    }







}



