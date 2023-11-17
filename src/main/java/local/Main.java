package local;

import models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static local.Utils.*;


public class Main {
    public static void main(String[] args) {
        char value = 0;
        String hostsfile = "";
        int delay = -1;

        for (int i = 0; i < args.length; i++) {
            if ("-h".equals(args[i]) && i + 1 < args.length) {
                hostsfile = args[i + 1];
            } else if ("-t".equals(args[i]) && i + 1 < args.length) {
                try {
                    delay = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    // Handle invalid startDelay input
                    System.err.println("Invalid delay value");
                }
            } else if ("-v".equals(args[i]) && i + 1 < args.length) {
                value = args[i + 1].charAt(0);
            }
        }

        String myHostname = getMyHostname();
        StateValues state = new StateValues();

        List<String> hostsfileInfo = getPeerList(hostsfile);
        Map<String, String> peersToRoles = new HashMap<String, String>();


        // determine what my peer index is
        for (int i = 0; i < hostsfileInfo.size(); i++) {
            String current = hostsfileInfo.get(i);
            String hostname = current.split(":")[0];
            String roles = current.split(":")[1];
            peersToRoles.put(hostname, roles);
        }

        int myPeerIndex = extractNumberFromTarget(myHostname, "peer");
        List<Role> myRoles = buildRoles(peersToRoles.get(myHostname), myPeerIndex, value);
        myRoles.stream().forEach(role -> role.configure(peersToRoles));
        myRoles.stream().forEach(role -> role.createConnections(state));
        myRoles.stream().forEach(role -> role.startListeners());
        sleep(2);
        myRoles.stream().forEach(role -> role.startTalkers());


        state.setValueToPropose(value);



        startProgramLogic(myRoles, state, delay);
    }

    /**
     * Main distributed algorithm for paxos
     */
    public static void startProgramLogic(List<Role> myRoles, StateValues state, float delay) {
        while(true) {
            if(state.getIsProposer() && !state.getSentProposal()) {
                myRoles.stream().forEach(role -> role.sendProposal(delay));
                state.setSentProposal(true);
            }

            if(state.getSendPrepareAck()) {
                myRoles.stream().forEach(role -> role.sendPrepareAck(state.getMinProposal()));
                state.setSendPrepareAck(false);
            }

            if(state.getIsProposer() && state.getPrepareAckCount() >= QUORUM_SIZE) {
                myRoles.stream().forEach(role -> role.sendAccept());
                state.setPrepareAckCount(0);
            }

            if(state.getSendAcceptAck()) {
                myRoles.stream().forEach(role -> role.sendAcceptAck(state.getMinProposal()));
                state.setSendAcceptAck(false);
            }

            if(state.getAcceptAckCount() >= QUORUM_SIZE) {
                state.setIsDecided(true);
                state.setValueDecided(state.getValueToPropose());
                System.out.println("Value decided: " + state.getValueDecided());
                state.setAcceptAckCount(0);
            }

            if(state.getSendAcceptedProposal()) {
                myRoles.stream().forEach(role -> role.sendAcceptedProposal(state.getMinProposal()));
                state.setSendAcceptedProposal(false);
            }

            sleep(0.01f);
        }
    }


    public static ArrayList<String> getPeerList(String fileName) {
        ArrayList<String> peers = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                peers.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return peers;
    }

    public static String getMyHostname() {
        String ret;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            ret = localHost.getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public static List<Role> buildRoles(String roles, int myPeerIndex, char value) {
        String[] rolesList = roles.split(",");
        List<Role> ret = new ArrayList<Role>();
        Utils u = new Utils();


        for (String role : rolesList) {
            // TODO Error handling here
            String roleType = role.split("\\d")[0];
            int actionIndex = extractNumberFromTarget(role, roleType);

            Role newRole = getRoleType(roleType, actionIndex, myPeerIndex, value);
            ret.add(newRole);
        }


        return ret;
    }

    public static Role getRoleType(String s, int index, int myPeerIndex, char value) {
        String test = s.toUpperCase();

        switch (test) {
            case "PROPOSER":
                Proposer p =  new Proposer(index, myPeerIndex);
                p.value = value;
                return p;
            case "ACCEPTOR":
                return new Acceptor(index, myPeerIndex);
            case "LEARNER":
                return new Learner(index, myPeerIndex);
            default:
                throw new RuntimeException("Invalid role type");
        }

    }

}