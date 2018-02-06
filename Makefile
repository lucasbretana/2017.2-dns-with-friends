TARGET = dns-with-friends
mainPack = dnswithfriends
VERSION = v0.1
# Common commands
JC = javac
JC_FLAG = -Xlint:unchecked -Xdiags:verbose
JD = javadoc
JD_FLAG =
JR = jar
JR_FLAG =
RM = rm
RM_FLAG = -rf

# Basic directories
BIN = bin
SRC = src
DOC = doc

# Colors
RED= \033[1;31m
YELLOW = \033[1;33m
GREEN = \033[1;32m
BLUE = \033[1;34m
NC = \033[0m

ERROR = $(RED)
WARNING = $(YELLOW)
NICE = $(GREEN)
NOTE = $(BLUE)

# Include files
JR_MANIFEST = META-INF/MANIFEST.MF
README = README.md
LICENSE = META-INF/LICENSE
INCLUDE = $(JR_MANIFEST) $(README) $(LICENSE) $(DOC)

# Libs
#DEBUG = debug_v2.1.jar
#LIBRARIES = $(LIB)/$(DEBUG)

################
# Packages:
# serverside
serverside = dnswithfriends.serverside
dns = $(serverside).dns
config = $(dns).config
friend = $(dns).friend
reply = $(dns).reply
request = $(dns)request
ui = $(serverside).ui


################################################################################
# serverside.dns


# TOO LAZY & SAD right now
all: 
	$(JC) $(JC_FLAG) $(SRC)/$(mainPack)/**/*.java -d $(BIN)


#######################################################################################################################################################
#documentation:
	#$(JD) $(JD_FLAG) -cp $(LIBRARIES):.:$(BIN) -d $(DOC)/

jarfile: clean $(DROPBOX) $(LINDA) documentation
	$(JR) $(JR_FLAG)cfm $(TARGET)_$(VERSION).jar $(JR_MANIFEST) -C $(BIN)/ $(DROPBOX) -C $(BIN)/ $(LINDA) -C $(BIN)/ com -C $(BIN)/ debug $(INCLUDE)

jarfile_test: clean $(DROPBOX) $(LINDA $(TEST) # documentation
	$(JR) $(JR_FLAG)cfme $(TARGET)_$(VERSION)_test.jar $(JR_MANIFEST) $(Main-Class) -C $(BIN) . $(INCLUDE)

clean:
	@$(RM) $(RM_FLAG) $(DOC)/* 
	@$(RM) $(RM_FLAG) $(BIN)/**/*.class 
	@$(RM) -f $(SRC)/***/*.class 
	@$(RM) $(RM_FLAG) $(TARGET)_$(VERSION)*.jar 
