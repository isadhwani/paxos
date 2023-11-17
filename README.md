This is a leaderless implementation of single paxos that handles two simple cases of the algorithm. 

There is a single value the system must decide on. The fastest proposal will always come win. It can be run by building docker image `prj4` with command:
``` docker build . -t  prj4```

Then compose the docker image with command:
``` docker-compose -f docker-compose-testcases-and-hostsfiles-lab4/docker-compose-testcase-1.yml up```

The test cases are descripted in the `REPORT.md` file. 