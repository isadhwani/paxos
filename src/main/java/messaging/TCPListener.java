package messaging;

import models.StateValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TCPListener extends Thread {


    StateValues state;
    int port;
    String myHostname;
    String targetHostname;


    public TCPListener(StateValues s, String myHostname, String targetHostname, int port) {
        this.port = port;
        this.myHostname = myHostname;
        this.state = s;
        this.targetHostname = targetHostname;
    }


    @Override
    public void run() {
//        System.out.println("Starting listener from " + targetHostname + " on port " + port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            //System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                //System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

                // Create a new thread to handle the client communication
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String receivedMessages;
            while ((receivedMessages = reader.readLine()) != null) {
                //System.out.println("Received from " + clientSocket.getInetAddress().getHostName() + ": " + receivedMessages);
                System.out.println("Received: " + receivedMessages);

                String[] messages = receivedMessages.split("\\}\\{");

                // if multiple messages are received at once, iterate through them
                for (String message : messages) {

                    Map<String, String> decoded = decodeJSON(message);

                    String msgType = decoded.get("message");

                    if (msgType.equals("PREPARE")) {
                        //System.out.println("Received proposal, determining if i can send prepare ack...");
                        if(!state.getIsDecided()) {
                            state.setSendPrepareAck(true);
                            state.setValueToPropose(decoded.get("value").charAt(0));
                        } else {
                            // TODO: determine what to do if we've already accepted...
                            System.out.println("Already accepted! Sending accepted proposal...");
                            state.setMinProposal(Integer.parseInt(decoded.get("proposalNumber")));
                            state.setSendAcceptedProposal(true);
                        }

                    } else if (state.getIsProposer() && msgType.equals("PREPARE_ACK")) {
                        int temp = state.getPrepareAckCount();
                        state.setPrepareAckCount(temp + 1);
                        System.out.println("Received PREPARE_ACK, count is now: " + state.getPrepareAckCount());

                    } else if (msgType.equals("ACCEPT")) {
                        int proposalNum = Integer.parseInt(decoded.get("proposalNumber"));
                        //System.out.println("Received accept, determining if i can send accept ack...");
                        if(!state.getIsDecided() && proposalNum >= state.getMinProposal()) {
                            state.setMinProposal(proposalNum);
                            state.setAcceptedPropsal(proposalNum);
                            state.setIsDecided(true);
                            state.setSendAcceptAck(true);
                            state.setValueDecided(decoded.get("value").charAt(0));
                            System.out.println("Decided on value " + decoded.get("value"));
                        } else {
                            // TODO: return min proposal to proposer, the given proposal num is invalid
                        }
                    } else if(state.getIsProposer() && msgType.equals("ACCEPT_ACK")) {
                        int temp = state.getAcceptAckCount();
                        state.setAcceptAckCount(temp + 1);
                        System.out.println("Received ACCEPT_ACK, count is now: " + state.getAcceptAckCount());
                    } else if(state.getIsProposer() && msgType.equals("ACCEPTED_PROPOSAL")) {
                        System.out.println("System has already accepted a proposal, deciding value: " + decoded.get("acceptedValue"));
                        state.setIsDecided(true);
                        state.setValueDecided(decoded.get("acceptedValue").charAt(0));
                        state.setAcceptedPropsal(Integer.parseInt(decoded.get("acceptedProposal")));
                    }

                }
                sleep(1);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static Map<String, String> decodeJSON(String jsonString) {
        Map<String, String> resultMap = new HashMap<>();

        // Remove curly braces from the JSON string
        jsonString = jsonString.substring(1, jsonString.length() - 1);

        // Split the string into key-value pairs
        String[] keyValuePairs = jsonString.split(",");

        for (String pair : keyValuePairs) {
            // Split each pair into key and value
            String[] entry = pair.split(":");

            // Trim whitespace from key and value
            String key = entry[0].trim();
            String value = entry[1].trim();

            String stringWithoutSpaces = value.replaceAll("\\s", "");

            resultMap.put(key, stringWithoutSpaces);
        }

        return resultMap;
    }
}


