----------------------------------------------------------
[DOING]
-- answer
- contains the input classes (those are gonna be created by the CPU that listen to some port)
- those classes should convert the txt to a full request, and also return the target to that request
+ haveFailed
- contains the many types of possible answers:
                                        - consult answer OK
                                        - consult answer FAIL
                                        - ask this guy
                                        - ack
[TODO]
-- logic
- this is the core part
- listen to some port
- creates a input when get some request
- uses the input to perform some action
- uses answer to create a out
- uses the out to send the text

-- request
- contains the output classes (those are gonna be created by the CPU)
- those classes should convert a answer into a txt
+ action
+ isDataUpdate
+ isConsult
- contains the many types of posible requests:
                                        - consult request (action: consult CPU's table of DNS)
                                        - friend add (action: add a friend to the  CPU's friend table)
                                        - dns update (action: update some information on the CPU's DNS table)


-- ui
- just creates a regular default server
