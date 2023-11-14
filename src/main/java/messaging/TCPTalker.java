package messaging;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import models.StateValues;

public class TCPTalker extends Thread {

    public StateValues state;
    public int proposalNumber;
    public boolean sendAcceptAck = false;
    String targetHostname;
    int port;
    String myHostname;
    public boolean sendProposal = false;

    public boolean sendAccept = false;

    public boolean sendPrepareAck = false;




    public TCPTalker(StateValues s, String targetHostname, String myHostname, int port) {
        this.state = s;
        this.targetHostname = targetHostname;
        this.port = port;
        this.myHostname = myHostname;
    }


    @Override
    public void run() {
        System.out.println("Starting talker to " + targetHostname + " on port " + port);
        while (true) {
            //printMessagesToSend();

            //System.out.println("Should this talker on port " + this.port + " send a token? " + this.sendToken);

            try {
                //System.out.println("Send marker? : " + this.sendMarker);
                Socket socket = new Socket(targetHostname, port);

                OutputStream outputStream = socket.getOutputStream();

                if (sendProposal) {

                    String message = "{id:" + myHostname + ", message:PREPARE, value:" + state.getValueToPropose() +
                            ", proposalNumber:" + proposalNumber + "}";

                    System.out.println("Sending PROPOSAL to server: " + message);
                    byte[] messageBytes = message.getBytes();
                    outputStream.write(messageBytes);
                    outputStream.flush();
                    sendProposal = false;
                } else if(sendPrepareAck) {
                    String message = "{id:" + myHostname + ", message:PREPARE_ACK, value:" + state.getValueToPropose() + "}";

                    System.out.println("Sending PREPARE_ACK to server: " + message);
                    byte[] messageBytes = message.getBytes();
                    outputStream.write(messageBytes);
                    outputStream.flush();
                    sendPrepareAck = false;
                } else if(sendAccept) {
                    String message = "{id:" + myHostname + ", message:ACCEPT, value:" + state.getValueToPropose() +
                            ", proposalNumber:" + proposalNumber + "}";

                    System.out.println("Sending ACCEPT to server: " + message);
                    byte[] messageBytes = message.getBytes();
                    outputStream.write(messageBytes);
                    outputStream.flush();

                    sendAccept = false;
                } else if(sendAcceptAck) {
                    String message = "{id:" + myHostname + ", message:ACCEPT_ACK, value:" + state.getValueToPropose() + "}";

                    System.out.println("Sending ACCEPT_ACK to server: " + message);
                    byte[] messageBytes = message.getBytes();
                    outputStream.write(messageBytes);
                    outputStream.flush();

                    sendAcceptAck = false;
                }


                sleep(1);


                // Close the socket
                socket.close();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


