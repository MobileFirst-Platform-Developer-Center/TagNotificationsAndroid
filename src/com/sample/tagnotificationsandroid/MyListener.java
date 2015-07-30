/**
* Copyright 2015 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
