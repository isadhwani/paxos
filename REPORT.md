My paxos implmentation follows the examples given in the lecture slides. After reading from a configuration file, each peer creates objects for each of its assigned role with the specified `actionIndex`. For each proposer, its acceptors are pre programmed. 

Roles are created in a builder function. Roles are stored in a local arraylist for each peer.

When the system begins, a `configure` method is called on every `role` in the system. This method finds each role's corresponding acceptor/proposer and stores it in a local arraylist.

Next, a `createConnections` method is called on each role. This builds TCP connections between each role and its corresponding acceptors/proposers according to my port rule.

Finally, `startListeners` is called followed 2 seconds later by `startTalkers`. These methods start each TCP thread and breaking them up ensures a peer is listening before trying to set up a talker. 

### Testcase 1
Peer q is given the role of `proposer1` and peer 2, 3 and 4 are given the role `acceptor1`. Peer 1 proposes value `X` and messages follow the given paxos algorithm. With only one proposal sent to quorum, value `X` is always decided by a quorum.

### Testcase 2
Peer 1 is given the role of `proposer1` and peer 2, 3 and 4 are given the role `acceptor1`.   
Peer 5 is given the role of `proposer2` and peers 2, 3 and 4 are given the role `acceptor2`.

Peer 1 proposes value `X` and peer 5 proposes value `Y`. Messages follow the given paxos algorithm. A quorum will alwyas decide `X` as peer 5 will be delayed by 10 seconds. 
When peer 5 proposes value `Y`, it will hear from peers 2, 3, and 4 that `X` has already been decided. It will then send a `decided` message to peer 1. Peer 1 will then send a `ACCEPTED_PROPOSAL` message to peer 5.