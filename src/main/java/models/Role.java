package models;

import messaging.TCPConnection;

import java.util.List;
import java.util.Map;

public abstract class Role {
    // Represents when chronologically this action should be executed. For example, proposer 1 will have
    // an action index of 1.
    int actionIndex;
    int myPeerIndex;

    final int START_PORT = 5000;
    // Port rules: Outgoing port = myPeerIndex * 10 + START_PORT + targetPeerIndex

    String myHostname;

    // Represents the type of role this is, e.g. proposer, acceptor, learner

    public Role(int actionIndex, int myPeerIndex) {
        this.actionIndex = actionIndex;
        this.myPeerIndex = myPeerIndex;
        this.myHostname = "peer" + myPeerIndex;
    }

    /**
     * Configures this role based on the information in the hostsfile.
     * Proposers must know who their acceptors are and vice versa.
     */
    public abstract void configure(Map<String, String> peersToRole);

    /**
     * Starts the TCP connections for this role. For example, a proposer will start connections to all of its acceptors.
     * @return
     */
    public abstract List<TCPConnection> createConnections(StateValues state);

    /**
     * Starts all listener threads
     */
    public abstract void startListeners();

    /**
     * Starts all talker threads
     */
    public abstract void startTalkers();

    public abstract void sendProposal();

    /**
     * Sends an accept message from all acceptors to proposer, depending on the current min proposal number
     */
    public abstract void sendPrepareAck();

    /**
     * If this peer is a proposer, broadcast an accept of the current value to all peers in system
     */
    public abstract void sendAccept();

    /**
     * If this peer is an acceptor, send an accept ack to the proposer
     */
    public abstract void sendAcceptAck();

}
