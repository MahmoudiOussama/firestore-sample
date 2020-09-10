# firestore-sample-android

This project is created for testing purposes.

This project runs a Firestore realtime query which should be triggered each time the **document** is updated.


In this First screen : 
  * Iam updating the **status** field within "test_order" document.
  * Each time the query return a document snapshot, the displayed number will be incremented.

![alt text](https://github.com/MahmoudiOussama/firestore-sample/blob/master/images/Screen%20Shot%202020-09-07%20at%204.04.32%20PM.png)

In this second screen : 
  * Iam updating the **status** field within "ZYaJKmQF9n1RknziD1tj" document
  * The UI may get updated once or more, then it stops updating, because the realtime query isn't working anymore.
  * P.S : in another test, I tried not to update the order tracking UI, just logging a number each time the query returns a snapshot (onEvent triggered)
  
  
