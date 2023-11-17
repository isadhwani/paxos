FROM amazoncorretto:17


WORKDIR /app/

ADD src/main/java/messaging/TCPConnection.java /app/messaging/
ADD src/main/java/messaging/TCPListener.java /app/messaging/
ADD src/main/java/messaging/TCPTalker.java /app/messaging/

ADD src/main/java/models/Acceptor.java /app/models/
ADD src/main/java/models/Proposer.java /app/models/
ADD src/main/java/models/Learner.java /app/models/
ADD src/main/java/models/Role.java /app/models/
ADD src/main/java/models/StateValues.java /app/models/

ADD src/main/java/local/Main.java /app/local/
ADD src/main/java/local/Utils.java /app/local/


ADD /docker-compose-testcases-and-hostsfiles-lab4/hostsfile-testcase1.txt /app/
ADD /docker-compose-testcases-and-hostsfiles-lab4/hostsfile-testcase2.txt /app/

RUN javac /app/models/StateValues.java
RUN javac /app/messaging/TCPListener.java
RUN javac /app/messaging/TCPTalker.java
RUN javac /app/messaging/TCPConnection.java
RUN javac /app/models/Role.java
RUN javac /app/models/Acceptor.java
RUN javac /app/models/Learner.java
RUN javac /app/models/Proposer.java
RUN javac /app/local/Main.java

ENTRYPOINT ["java", "local/Main"]

