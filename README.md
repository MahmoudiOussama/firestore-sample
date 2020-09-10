# firestore-sample-android

This project is created for testing purposes.

This project runs a Firestore realtime query which should be triggered each time the **document** is updated.


In this First screen : 
  * Iam updating the **status** field within "test_order" document.
  * Each time the query return a document snapshot, the displayed number will be incremented.

![alt text](https://raw.githubusercontent.com/MahmoudiOussama/firestore-sample/master/images/Firestore-sample1.gif)

In this second screen : 
  * Iam updating the **status** field within "ZYaJKmQF9n1RknziD1tj" document
  * The UI may get updated once or more, then it stops updating, because the realtime query isn't working anymore.
  * P.S : in another test, I tried not to update the order tracking UI, just logging a number each time the query returns a snapshot (onEvent triggered)
  
![alt text](https://raw.githubusercontent.com/MahmoudiOussama/firestore-sample/master/images/Firestore-sample2.gif)
