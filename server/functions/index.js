/*const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello from Firebase!");
 });

https://firebase.google.com/docs/functions/http-events	
https://stackoverflow.com/questions/43569825/send-fcm-to-all-android-devices-using-cloud-functions


 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();




/**
 * Triggers when http is sent
 */
exports.sendMsg = functions.https.onRequest((req, res) => {

	var payload = {notification: {
					    title: 'New event added',
					    body: 'test'
					  }
					};

	/*if (event.data.previous.exists()) {
        if (event.data.previous.numChildren() < event.data.numChildren()) {
            return admin.messaging().sendToTopic("latest_events", payload);
        } else {
            return;
        }
    }

    if (!event.data.exists()) {
        return;
    }*/

    return admin.messaging().send( payload);

  
});

