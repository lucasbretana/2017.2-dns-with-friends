# To run the tests
  - make tests

# To run the server apart from the client
  - make serverside
  - enter the bin directorie
  - java -cp . dnswithfriends.serverside.ui.UIServer <port>
  - the <port> option is optional, the default is set in the code
  - now the server is waiting for one of the commands
    - /quit : closes the server
    - /load-friends <file> : witch loads the know friend from a file
    - /load-hosts <file> : witch loads the know hosts from a file
    - /help : show a little help message
    
# To Run the client apart from the server
  - make clientside
  - enter bin directorie
  - java -cp . dnswithfriends.clientside.ui.UIClient <ip> <port>
  - the <ip> and <port> parameters are optional, the default ones are in the code
  - now the client waits for a file name, that must be a request to the server

# Some examples of requests and responses are available in the resources folder

