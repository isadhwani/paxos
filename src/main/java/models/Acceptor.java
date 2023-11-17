package models;

import messaging.TCPConnection;
import messaging.TCPListener;
import messaging.TCPTalker;
import local.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Acceptor extends Role {
    String proposerId;
    Utils u = new Utils();

    public Acceptor(int actionIndex, int myPeerIndex) {
        super(actionIndex, myPeerIndex);
    }

    List<TCPConnection> proposerConnections = new ArrayList<>();

    @Override
    public void configure(Map<String, String> peersToRole) {
        for (String peer : peersToRole.keySet()) {
            String[] roles = peersToRole.get(peer).split(",");
            for (String role : roles) {
                if (role.equals("proposer" + actionIndex)) {
                    proposerId = peer;
                }
            }
        }


//        System.out.println("Creating acceptor corresponding to proposal id: " + proposerId);
    }

    @Override
    public List<TCPConnection> createConnections(StateValues state) {
        int proposerIndex = u.extractNumberFromTarget(proposerId, "peer");
        int talkerPort = myPeerIndex * 10 + START_PORT + proposerIndex;
        int listenPort = proposerIndex * 10 + START_PORT + myPeerIndex;

        TCPListener listen = new TCPListener(state, myHostname, proposerId, listenPort);
        TCPTalker talk = new TCPTalker(state, proposerId, proposerId, talkerPort);
        TCPConnection conn = new TCPConnection(talk, listen);
        proposerConnections.add(conn);

        // TODO - copy proposer connections into new list. Does this need to be returned?
        return proposerConnections;
    }

    @Override
    public void startListeners() {
        for (TCPConnection conn : proposerConnections) {
            conn.listener.start();
        }
    }

    @Override
    public void startTalkers() {
        for (TCPConnection conn : proposerConnections) {
            conn.talker.start();
        }
    }

    @Override
    public void sendProposal(float seconds) {
        throw new IllegalStateException("Acceptor cannot send proposal");
    }

    @Override
    public void sendPrepareAck(int minProposalNumber) {
        // TODO: Is this the correct logic? Do I need another counter to track this?
        if (this.actionIndex == minProposalNumber) {
            TCPConnection currentProposer = proposerConnections.get(0);
            currentProposer.talker.sendPrepareAck = true;
        }
    }

    @Override
    public void sendAccept() {
        throw new IllegalStateException("Acceptor cannot broadcast accept");
    }

    @Override
    public void sendAcceptAck(int minProposalNumber) {
        if (this.actionIndex == minProposalNumber) {
            TCPConnection currentProposer = proposerConnections.get(0);
            currentProposer.talker.sendAcceptAck = true;
        }
    }

    @Override
    public void sendAcceptedProposal(int minProposalNumber) {
        if (this.actionIndex == minProposalNumber) {
            TCPConnection currentProposer = proposerConnections.get(0);
            currentProposer.talker.sendAcceptedProposal = true;
        }
    }


    @Override
    public String toString() {
        return "ACCEPTOR" + actionIndex;
    }

}
