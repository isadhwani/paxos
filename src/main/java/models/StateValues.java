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

    int minProposal = 0;
    private boolean sentProposal = false;
    private int acceptedProposal = -1;
    private boolean setSendAcceptAck = false;

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

    public void setSendPrepareAck(boolean b) {
        this.sendPrepareAck = b;
    }

    public boolean getIsProposer() {
        return this.isProposer;
    }

    public boolean getSendPrepareAck() {
        return this.sendPrepareAck;
    }

    public void setIsProposer(boolean b) {
        this.isProposer = b;
    }

    public boolean getSentProposal() {
        return this.sentProposal;
    }

    public void setSentProposal(boolean b) {
        this.sentProposal = b;
    }

    public int getMinProposal() {
        return this.minProposal;
    }

    public int setMinProposal(int minProposal) {
        return this.minProposal = minProposal;
    }

    public void setAcceptedPropsal(int proposalNum) {
        this.acceptedProposal = proposalNum;
    }

    public void setSendAcceptAck(boolean b) {
        this.setSendAcceptAck = b;
    }

    public boolean getSendAcceptAck() {
        return this.setSendAcceptAck;
    }

    public int getAcceptAckCount() {
        return acceptAckCount;
    }

    public void setAcceptAckCount(int acceptAckCount) {
        this.acceptAckCount = acceptAckCount;
    }

    public void setValueDecided(char value) {
        this.valueDecided = value;
    }

    public char getValueDecided(char value) {
        return this.valueDecided;
    }
}
