/*
*
    COPYRIGHT LICENSE: This information contains sample code provided in source code form. You may copy, modify, and distribute
    these sample programs in any form without payment to IBM® for the purposes of developing, using, marketing or distributing
    application programs conforming to the application programming interface for the operating platform for which the sample code is written.
    Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES,
    EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
    FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT,
    INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE.
    IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.
*/

package com.sample.tagnotificationsandroid;

import org.json.JSONException;
import org.json.JSONObject;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLNotificationListener;
import com.worklight.wlclient.api.WLOnReadyToSubscribeListener;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

/** This class handles all callback functions for various actions such as 
 * a. onReadyToSubscribe
 * b. callback from connect
 * c. callback from subscribe
 * d. callback from unsubscribe
 * e. callback when a notification is received
 */
public class MyListener implements WLOnReadyToSubscribeListener, WLResponseListener, WLNotificationListener{
	
	/* The mode defines what action the MyListener object will do */
	public static final int MODE_CONNECT = 0; 
	public static final int MODE_SUBSCRIBE = 1;
	public static final int MODE_UNSUBSCRIBE = 2;
	
	private int mode; 
	
	public MyListener(int mode){
		this.mode = mode; 
	}
	
	/* This function is called when the registration with GCM is successful. 
	 * We are now ready to subscribe / unsubscribe 
	 */
	@Override
	public void onReadyToSubscribe() {		
		MainActivity.showAlertDialog("Tag Notifications" , "Ready to subscribe");
		MainActivity.enableSubscribeButtons();
	}
	
	@Override
	public void onSuccess(WLResponse response) {
		switch (mode){
		case MODE_CONNECT:
			//MainActivity.showAlertDialog("Tag Notifications", "Successfully connected to server.");
			break;
		
		case MODE_SUBSCRIBE:
			MainActivity.showAlertDialog("Tag Notifications", "Subscribed to tag");
			break;
			
		case MODE_UNSUBSCRIBE:
			MainActivity.showAlertDialog("Tag Notifications" , "Unsubscribed from tag");
			break;
			
		}				
	}

	@Override
	public void onFailure(WLFailResponse response) {
		switch (mode){
		case MODE_CONNECT:
			MainActivity.showAlertDialog("Tag Notifications", "Unable to connect to server: " + response.getErrorMsg());
			break;
		
		case MODE_SUBSCRIBE:
			MainActivity.showAlertDialog("Tag Notifications", "Failed subscribing to tag: " + response.getErrorMsg());
			break;
			
		case MODE_UNSUBSCRIBE:
			MainActivity.showAlertDialog("Tag Notifications", "Failed unsubscribing from tag: " + response.getErrorMsg());
			break;
			
		}
	}
	
	@Override
	public void onMessage(String props, String payload) {
		try {
			JSONObject payloadObject = new JSONObject(payload);
			
			if(payloadObject.has("tag")) {
				String tag = payloadObject.getString("tag");
				if(tag.equals("Push.ALL")) {
					MainActivity.showAlertDialog("Tag Notifications", "Broadcast notification received:  " + props);	
				} else {
					MainActivity.showAlertDialog("Tag Notifications", "Notification received for tag " + tag +": " + props);	
				}
			}
		}catch (JSONException e) {
			MainActivity.showAlertDialog("Receive Message Error", e.getMessage());
		}

		
	}
}
