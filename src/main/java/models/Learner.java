package models;

import messaging.TCPConnection;

import java.util.List;
import java.util.Map;

public class Learner extends Role{
    public Learner(int actionIndex, int myPeerIndex) {
        super(actionIndex, myPeerIndex);
    }

    @Override
    public void configure(Map<String, String> peersToRole) {

    }

    @Override
    public List<TCPConnection> createConnections(StateValues state) {
        return null;
    }

    @Override
    public void startListeners() {
    }

    @Override
    public void startTalkers() {

    }

    @Override
    public void sendProposal() {
        throw new IllegalStateException("Learner cannot send proposal");
    }

    @Override
    public void sendPrepareAck() {
        throw new IllegalStateException("Learner cannot send accept");
    }

    @Override
    public void sendAccept() {
        throw new IllegalStateException("Learner cannot broadcast accept");
    }

    @Override
    public void sendAcceptAck() {
        throw new IllegalStateException("Learner cannot send accept ack");
    }

    @Override
    public String toString() {
        return "LEARNER" + actionIndex;
    }
}
