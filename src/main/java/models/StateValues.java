package models;

import java.util.List;

public class StateValues{
    // TODO Apply mutex lock to this object

    // Value to be sent by a proposer, null if not a proposer
    private char valueToPropose;
    
    private char valueDecided;

    private boolean isDecided = false;
    
    private boolean isProposer = false;
    private boolean sendAccept = false;
    private int prepareAckCount = 0;

    private int acceptAckCount = 0;
    private boolean sendPrepareAck;
    // Represents all the roles that this peer has, e.g. proposer1, proposer2, acceptor1, learner2...
    // Seperated by commas (as sent in from hostsfile)
    private List<Role> roles;

    int minProposal = 1;
    private boolean sentProposal = false;
    private int acceptedProposal = -1;
    private boolean setSendAcceptAck = false;
    private boolean sendAcceptedProposal = false;

    public synchronized char getValueToPropose() {
        return valueToPropose;
    }
    public synchronized void setValueToPropose(char valueToPropose) {
         this.valueToPropose = valueToPropose;
    }


    public synchronized char getValueDecided() {
        return valueDecided;
    }


    public synchronized boolean isDecided() {
        return isDecided;
    }

    public synchronized void setIsDecided(boolean isDecided) {
        this.isDecided = isDecided;
    }

    public synchronized boolean isProposer() {
        return isProposer;
    }

    public synchronized void isProposer(boolean isProposer) {
        this.isProposer = isProposer;
    }


    public synchronized void setPrepareAckCount(int prepareAckCount) {
        this.prepareAckCount = prepareAckCount;
    }

    public synchronized int getPrepareAckCount() {
        return prepareAckCount;
    }


    public synchronized boolean getIsDecided() {
        return isDecided;
    }

    public synchronized void setSendPrepareAck(boolean b) {
        this.sendPrepareAck = b;
    }

    public synchronized boolean getIsProposer() {
        return this.isProposer;
    }

    public synchronized boolean getSendPrepareAck() {
        return this.sendPrepareAck;
    }

    public synchronized void setIsProposer(boolean b) {
        this.isProposer = b;
    }

    public synchronized boolean getSentProposal() {
        return this.sentProposal;
    }

    public synchronized void setSentProposal(boolean b) {
        this.sentProposal = b;
    }

    public synchronized int getMinProposal() {
        return this.minProposal;
    }

    public synchronized int setMinProposal(int minProposal) {
        return this.minProposal = minProposal;
    }

    public synchronized void setAcceptedPropsal(int proposalNum) {
        this.acceptedProposal = proposalNum;
    }

    public synchronized void setSendAcceptAck(boolean b) {
        this.setSendAcceptAck = b;
    }

    public synchronized boolean getSendAcceptAck() {
        return this.setSendAcceptAck;
    }

    public synchronized int getAcceptAckCount() {
        return acceptAckCount;
    }

    public synchronized void setAcceptAckCount(int acceptAckCount) {
        this.acceptAckCount = acceptAckCount;
    }

    public synchronized void setValueDecided(char value) {
        this.valueDecided = value;
    }

    public synchronized char getValueDecided(char value) {
        return this.valueDecided;
    }

    public synchronized void setSendAcceptedProposal(boolean b) {
        this.sendAcceptedProposal = b;
    }

    public synchronized boolean getSendAcceptedProposal() {
        return this.sendAcceptedProposal;
    }

    public synchronized int getAcceptedProposal() {
        return this.acceptedProposal;
    }
}
