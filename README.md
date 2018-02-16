# Welcome to "dns-with-friends"
A DNS implementation for RC 2017/2 class

This aplication simulates a DNS server.

# Server side
The server can answer ------ requestion  
  - DNS CONSULT : the client requests the name/IP of a given IP/name  
  - DNS UPDATE : the client is acctually another server with an IP/name update  
  - FRIEND ADD : another server wants to present himself as a DNS friend  
  - FRIEND RM : remove a server from the DNS friends
  - ...


--------------------------------------------------
# General request formats
"
<request-type><SPACE><space-separeted-flags><ENTER>  
<enter-separated-paramateres><ENTER>  
"

"
# DNS CONSULT request format
DNS CONSULT -ip -d 5 -n 2  
lucasbretana.servebeer.com
menescraft.serveminecraft.net  
"

The above example of request does the following:
Makes DNS consult by IP using depth search on others server, using at most 5 other servers, and tells the server that it's making two consults.


