package wst.client;

import wst.entity.Shop;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import org.glassfish.jersey.client.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class WebServiceClient {
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final String URL = "http://localhost:8080/app/shops";
    private static Client client = ClientBuilder.newClient( new ClientConfig() );

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("Enter command (1 - find/2 - update/3 - delete/4 - insert):");

            int command = 0;
            try {
                command = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                continue;
            }
            
            switch (command) {
                case 1:
                    read();
                    break;
                case 2:
                    update();
                    break;
                case 3:
                    delete();
                    break;
                case 4:
                    create();
                    break;
                default:
                    break;
            }
        }
    }

    public static void read() {
        String name = getColumn("Name: ");
        String city = getColumn("City: ");
        String address = getColumn("Address: ");
        Boolean isActive = getBooleanColumn("isActive (y/n): ");
        String type = getColumn("Type: ");

        WebTarget webTarget = client.target(URL);

        webTarget = webTarget.queryParam("name", name);
        webTarget = webTarget.queryParam("city", city);
        webTarget = webTarget.queryParam("address", address);
        webTarget = webTarget.queryParam("isActive", isActive);
        webTarget = webTarget.queryParam("type", type);

        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed\n" + response.getStatus() + "\n" + response.getEntity());
        }

        List<Shop> shops = response.readEntity(new GenericType<List<Shop>>(){});

        printList(shops);
    }

    public static int update() {
        long id = getIntColumn("ID: ");
        String name = getColumn("Name: ");
        String city = getColumn("City: ");
        String address = getColumn("Address: ");
        Boolean isActive = getBooleanColumn("isActive (y/n): ");
        String type = getColumn("Type: ");

        Shop shop = new Shop(name, address, isActive, city, type);

        WebTarget webTarget = client.target(URL);

        Response response = webTarget.path(String.valueOf(id))
            .request(MediaType.TEXT_PLAIN_TYPE)
            .put(Entity.json(shop));

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed\n" + response.getStatus() + "\n" + response.getEntity());
        }

        return Integer.parseInt(response.getEntity().toString());
    }

    public static int delete() {
        int id = getIntColumn("ID: ");

        WebTarget webTarget = client.target(URL);

        Response response = webTarget.path(String.valueOf(id))
            .request(MediaType.TEXT_PLAIN_TYPE)
            .delete();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed\n" + response.getStatus() + "\n" + response.getEntity());
        }

        return Integer.parseInt(response.getEntity().toString());
    }

    public static long create() {
        String name = getColumn("Name: ");
        String city = getColumn("City: ");
        String address = getColumn("Address: ");
        Boolean isActive = getBooleanColumn("isActive (y/n): ");
        String type = getColumn("Type: ");

        Shop shop = new Shop(name, address, isActive, city, type);

        WebTarget webTarget = client.target(URL);

        Response response = webTarget.request(MediaType.TEXT_PLAIN_TYPE)
            .post(Entity.json(shop));

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Request failed\n" + response.getStatus() + "\n" + response.getEntity());
        }

        return Long.parseLong(response.getEntity().toString());
    }

    private static void printList(List<Shop> shops) {
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    private static String checkNull(String s) {
        return s.length() == 0 ? null : s;
    }

    private static Boolean checkBool(String s) {
        if (s.length() == 0) return null;
        return s.equals("y") ? Boolean.TRUE : Boolean.FALSE;
    }

    private static String getColumn(String msg) {
        System.out.print(msg);
        try {
            return checkNull(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Boolean getBooleanColumn(String msg) {
        System.out.print(msg);
        try {
            return checkBool(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getIntColumn(String msg) {
        System.out.print(msg);
        try {
            return Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}