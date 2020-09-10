# firestore-sample-android

This project is created for testing purposes.

This Android app runs a Firestore realtime query which should be triggered each time the **document** is updated.
To update the firestore document, i created a web app only for this purpose, see below link :

[Web app for updating Firestore documents (status field)](https://my-first-project-d447c.web.app)
Use it on chromium based browsers like Brave, Chrome ...

In this First screen : 
  * Iam updating the **status** field within "test_order" document.
  * Each time the query return a document snapshot, the displayed number will be incremented.

![alt text](https://raw.githubusercontent.com/MahmoudiOussama/firestore-sample/master/images/Firestore-sample1.gif)

In this second screen : 
  * Iam updating the **status** field within "ZYaJKmQF9n1RknziD1tj" document
  * The UI may get updated once or more, then it stops updating, because the realtime query isn't working anymore.
  * P.S : in another test, I tried not to update the order tracking UI, just logging a number each time the query returns a snapshot (onEvent triggered)
  
![alt text](https://raw.githubusercontent.com/MahmoudiOussama/firestore-sample/master/images/Firestore-sample2.gif)
