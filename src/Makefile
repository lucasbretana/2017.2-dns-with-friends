JC = javac
JC_FLAG = -Xlint:unchecked -Xdiags:verbose

DOC = ../doc/
BIN = ../bin/
SRC = src
CP = ../bin/

all: clear clientside serverside util abc doc
#-------------------------------
#clientside
clientside: clientside.client clientside.ui

clientside.ui: clientside.client dnswithfriends/clientside/ui/UIClient.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/clientside/ui/UIClient.java -d $(BIN)

clientside.client: dnswithfriends/clientside/client/Client.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/clientside/client/Client.java -d $(BIN)
#
#-------------------------------
# serverside
serverside: serverside.server serverside.data serverside.ui

serverside.server: util serverside.data dnswithfriends/serverside/server/Server.java dnswithfriends/serverside/server/ServerThread.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/serverside/server/Server.java dnswithfriends/serverside/server/ServerThread.java -d $(BIN)

serverside.data: dnswithfriends/serverside/data/Entry.java dnswithfriends/serverside/data/Friend.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/serverside/data/Entry.java dnswithfriends/serverside/data/Friend.java -d $(BIN)

serverside.ui: util serverside.server serverside.data dnswithfriends/serverside/ui/UIServer.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/serverside/ui/UIServer.java -d $(BIN)

#-------------------------------
# util
util: dnswithfriends/util/Util.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/util/Util.java -d $(BIN)

#--------------------------------
tests: all dnswithfriends/tests/Tester.java
	$(JC) $(JC_FLAG) -cp $(CP) dnswithfriends/tests/Tester.java -d $(BIN)
	\cd $(BIN) && java -cp . dnswithfriends.test.Tester

abc:

#--------------------------------
clear: 
	\rm -rf $(DOC)/*
	\rm -rf $(BIN)/*
