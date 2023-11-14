package models;

import messaging.TCPConnection;
import messaging.TCPListener;
import messaging.TCPTalker;
import local.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Proposer extends Role {
    List<String> acceptorIds = new ArrayList<>();
    Utils u = new Utils();
    public char value;

    List<TCPConnection> acceptorConnections = new ArrayList<>();
    StateValues state;

    public Proposer(int actionIndex, int myPeerIndex) {
        super(actionIndex, myPeerIndex);
    }


    @Override
    public void configure(Map<String, String> peersToRole) {
        for(String peer : peersToRole.keySet()) {
            String[] roles = peersToRole.get(peer).split(",");
            for(String role : roles) {
                if(role.equals("acceptor" + actionIndex)) {
                    acceptorIds.add(peer);
                }
            }
        }
        System.out.println("Acceptor ids: " + acceptorIds);
    }

    @Override
    public List<TCPConnection> createConnections(StateValues state) {
        this.state = state;
        state.setIsProposer(true);

        for (String acceptorId : acceptorIds) {
            System.out.println("Processing acceptor id: " + acceptorId);
            int acceptorIndex = u.extractNumberFromTarget(acceptorId, "peer");
            int talkerPort = myPeerIndex * 10 + START_PORT + acceptorIndex;
            int listenPort = acceptorIndex * 10 + START_PORT + myPeerIndex;

            TCPListener listen = new TCPListener(state, myHostname, acceptorId, listenPort);
            TCPTalker talk = new TCPTalker(state, acceptorId, acceptorId, talkerPort);
            TCPConnection conn = new TCPConnection(talk, listen);
            acceptorConnections.add(conn);
        }
        return acceptorConnections;
    }

    @Override
    public void startListeners() {
        for (TCPConnection conn : acceptorConnections) {
            conn.listener.start();
        }
    }

    @Override
    public void startTalkers() {
        for (TCPConnection conn : acceptorConnections) {
            conn.talker.start();
        }
    }

    @Override
    public void sendProposal() {
        System.out.println("Sending proposal!");

        for (TCPConnection conn : acceptorConnections) {
            conn.talker.sendProposal = true;
            conn.talker.proposalNumber = actionIndex;
        }
    }

    @Override
    public void sendPrepareAck() {
        throw new IllegalStateException("Proposer cannot send accept");
    }

    @Override
    public void sendAccept() {
        for(TCPConnection conn : acceptorConnections) {
            conn.talker.sendAccept = true;
        }
    }

    @Override
    public void sendAcceptAck() {
        throw new IllegalStateException("Proposer cannot send accept ack");
    }

    @Override
    public String toString() {
        return "PROPOSER" + actionIndex;
    }
}
