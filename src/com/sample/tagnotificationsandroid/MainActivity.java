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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLPush;
import com.worklight.wlclient.api.WLPushOptions;

public class MainActivity extends Activity {
	private static MainActivity _this;
	private Button isPushSupportedButton = null;
	private Button subscribedTagsButton = null;
	
	private static Button subscribeTag1Button = null;
	private static Button subscribeTag2Button = null;
	private static Button unsubscribeTag1Button = null;
	private static Button unsubscribeTag2Button = null;
	public static TextView txtStatus = null;
	private MyListener listener = new MyListener(MyListener.MODE_CONNECT) ;
	private WLClient client = null;
	private WLPush push = null; 
	
	@Override
	protected void onResume() {
		super.onResume();
		if (push != null)
			push.setForeground(true);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (push != null)
			push.setForeground(false);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (push != null)
		push.unregisterReceivers();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		
		setContentView(R.layout.activity_main);
		client = WLClient.createInstance(this);	
		client.connect(listener);
		push = client.getPush();
		push.setOnReadyToSubscribeListener(listener);
		push.setWLNotificationListener(listener);	
		
		/* 
		 * Is push supported
		 */
		isPushSupportedButton = (Button) findViewById(R.id.btnIsPushSupported);
		isPushSupportedButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					boolean supported = client.getPush().isPushSupported();
					String strSupported = supported ? "true" : "false";
					showAlertDialog("isPushSupported", strSupported);
				}
				catch(Exception ex){
					showAlertDialog("Error", ex.getLocalizedMessage());
				}
			}
		});
		
		/* 
		 * Subscribed Tags
		 */
		subscribedTagsButton = (Button) findViewById(R.id.btnSubscribedTags);
		subscribedTagsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					boolean isTag1Subscribed = client.getPush().isTagSubscribed("sample-tag1");
					boolean isTag2Subscribed = client.getPush().isTagSubscribed("sample-tag2");
					showAlertDialog("Subscribed Tags", "sample-tag1: "+ isTag1Subscribed +"\nsample-tag2: "+ isTag2Subscribed);
				}
				catch(Exception ex){
					showAlertDialog("Error", ex.getLocalizedMessage());
				}
			}
		});
		
		/* 
		 * Subscribe Tag1
		 */
		subscribeTag1Button = (Button) findViewById(R.id.btnSubscribeTag1);
		subscribeTag1Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					client.getPush().subscribeTag("sample-tag1",new WLPushOptions(), new MyListener(MyListener.MODE_SUBSCRIBE));
				}
				catch(Exception ex){
					showAlertDialog("Error", ex.getLocalizedMessage());
				}
			}

		});
		
		/* 
		 * Subscribe Tag2
		 */
		subscribeTag2Button = (Button) findViewById(R.id.btnSubscribeTag2);
		subscribeTag2Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					client.getPush().subscribeTag("sample-tag2", new WLPushOptions(), new MyListener(MyListener.MODE_SUBSCRIBE));
				}
				catch(Exception ex){
					showAlertDialog("Error", ex.getLocalizedMessage());
				}
			}

		});
		
		/* 
		 * Unsubscribe Tag1
		 */
		unsubscribeTag1Button = (Button) findViewById(R.id.btnUnsubscribeTag1);
		unsubscribeTag1Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					client.getPush().unsubscribeTag("sample-tag1", new MyListener(MyListener.MODE_UNSUBSCRIBE));
				}
				catch(Exception ex){
					showAlertDialog("Error", ex.getLocalizedMessage());
				}
			}

		});
		
		
		/* 
		 * Unsubscribe Tag2
		 */
		
		unsubscribeTag2Button = (Button) findViewById(R.id.btnUnsubscribeTag2);
		unsubscribeTag2Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					client.getPush().unsubscribeTag("sample-tag2", new MyListener(MyListener.MODE_UNSUBSCRIBE));
				}
				catch(Exception ex){
					showAlertDialog("Error", ex.getLocalizedMessage());
				}
			}

		});
	}
	
	public static void showAlertDialog(final String strTitle, final String strMessage){
		Runnable run = new Runnable() {			
			public void run() {
				AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(_this);
				alertDialog2.setTitle(strTitle);		
				alertDialog2.setMessage(strMessage);
				alertDialog2.setPositiveButton(
						"OK",
		                new DialogInterface.OnClickListener() {
		            	public void onClick(DialogInterface dialog, int id) {
		            		dialog.cancel();
		            	}
		        });
				alertDialog2.show();
			}
		};
		_this.runOnUiThread(run);		
	}	
	public static void enableSubscribeButtons(){
		Runnable run = new Runnable() {			
			public void run() {
				subscribeTag1Button.setEnabled(true);
				subscribeTag2Button.setEnabled(true);	
				unsubscribeTag1Button.setEnabled(true);
				unsubscribeTag2Button.setEnabled(true);				
			}
		};
		_this.runOnUiThread(run);

	}
}
