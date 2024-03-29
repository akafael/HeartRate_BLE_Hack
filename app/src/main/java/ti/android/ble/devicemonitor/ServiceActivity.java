/**************************************************************************************************
  Filename:       ServiceActivity.java
  Revised:        $Date: 2014-01-07 16:17:34 +0100 (ti, 07 jan 2014) $
  Revision:       $Revision: 28784 $

  Copyright 2013 Texas Instruments Incorporated. All rights reserved.

  IMPORTANT: Your use of this Software is limited to those specific rights
  granted under the terms of a software license agreement between the user
  who downloaded the software, his/her employer (which must be your employer)
  and Texas Instruments Incorporated (the "License").  You may not use this
  Software unless you agree to abide by the terms of the License. 
  The License limits your use, and you acknowledge, that the Software may not be 
  modified, copied or distributed unless used solely and exclusively in conjunction 
  with a Texas Instruments Bluetooth device. Other than for the foregoing purpose, 
  you may not use, reproduce, copy, prepare derivative works of, modify, distribute, 
  perform, display or sell this Software and/or its documentation for any purpose.

  YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE
  PROVIDED �AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED,
  INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
  NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL
  TEXAS INSTRUMENTS OR ITS LICENSORS BE LIABLE OR OBLIGATED UNDER CONTRACT,
  NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER
  LEGAL EQUITABLE THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES
  INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT, PUNITIVE
  OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT
  OF SUBSTITUTE GOODS, TECHNOLOGY, SERVICES, OR ANY CLAIMS BY THIRD PARTIES
  (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.

  Should you have any questions regarding your right to use this Software,
  contact Texas Instruments Incorporated at www.TI.com

 **************************************************************************************************/
package ti.android.ble.devicemonitor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ti.android.ble.common.BluetoothLeService;
import ti.android.ble.common.GattInfo;
import ti.android.ble.common.HelpView;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ServiceActivity extends ViewPagerActivity 
{
	// Inter-activity communication
	public final static String EXTRA_NAME = "EXTRA_NAME";

	// Log
	private static String TAG = "ServiceActivity";

	// Constants
	private static final String mReadMsg = "Characteristic read";
	private static final String mWriteMsg = "Characteristic written";
	private static final String mNotifMsg = "Notification";

	// GUI
	private ServiceView mServiceView = null;
	private HelpView mHelpView = null;
	private Display mDisplay=null;

	// BLE
	//This is the variable that stores the service which was selected
	private BluetoothGattService mService = null;
	
	private List<BluetoothGattCharacteristic> mCharList;
	private String mServiceName = "unknown service";
	private static String mUuidDataStr = "";

	
	int i=0; //will serve as a counter while plotting
	public ServiceActivity() 
	{
		Log.d(TAG, "construct");
		mResourceFragmentPager = R.layout.fragment_pager;
		mResourceIdPager = R.id.pager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		// Service information
		//This function gives us the instance of the active service (the service that 
		//was selected) :)
		mServiceName = intent.getStringExtra(EXTRA_NAME);
		//We are calling the functions getInstance and getActiveService
		//which are defined within the DeviceActivity class :)
		
		mService = DeviceActivity.getInstance().getActiveService();

	//When we call the getInstance function, we are storing the instance of the service 
	//within the object mService
	//What is the getInstance() function?

/*If a remote device offers multiple services
 * with the same UUID (eg. multiple battery services for different batteries)
 * ,the instance ID is used to distinguish services.
 * Returns the instance ID for this service :)
*/
		// Characteristics list
		mCharList = new ArrayList<BluetoothGattCharacteristic>();

		// Create multiple views and add them to the view pager and tabs
		mServiceView = new ServiceView();
		mHelpView = new HelpView("help_device.html", R.layout.fragment_help,
		    R.id.webpage);
		mDisplay=new Display();
		//These tabs are added here since, they need to be displayed once the user
		//clicks on one of the services
		mSectionsPagerAdapter.addSection(mServiceView, "Characteristics");
		mSectionsPagerAdapter.addSection(mHelpView, "Help");
		mSectionsPagerAdapter.addSection(mDisplay, "PPG Wave Form");
	}

	@Override
	public void onBackPressed() {
		// Hide the keyboard if it is open
		if (mServiceView != null) {
			if (!mServiceView.onBackPressed()) {
				super.onBackPressed();
			}
		}
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		mServiceView = null;
		mService = null;
		mHelpView = null;
		mDisplay=null;
		super.onDestroy();
	}

	@Override
	protected void onResume() 
	{
		Log.d(TAG, "onResume");
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}
//What is makeGattUpdateIntentFilter
	
	private static IntentFilter makeGattUpdateIntentFilter() 
	{   final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_READ);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE);
		return intentFilter;
	}
//This class is receiving the message from BluetoothLeService
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			Log.d(TAG, "action = " + action);

			if (mServiceView != null) 
			{
				//Gets from the BluetoothLeService
				int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
						BluetoothGatt.GATT_SUCCESS);
				if (status == BluetoothGatt.GATT_SUCCESS) 
				{
					byte[] value = intent
					    .getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
					String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);

					if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) 
					{
		//Why are we checking this condition?
						
						if (uuidStr.equals(mUuidDataStr))
						{
				  mServiceView.displayNotif(value, (byte) value.length);
							
	/* To check if heart rate can be displayed in another textview 
	of another fragment*/


    /*We can check if the graph will plot by plotting random values				 
     * every time we get a notification
     * it adds a random value on the plot
	 * 
	 */
				  int l= new BigInteger(value).intValue();

				  mDisplay.AddnewPoint(l);
					
				mServiceView.setStatus(mNotifMsg);
						}
					} else if (BluetoothLeService.ACTION_DATA_READ.equals(action))
					{
						if (value != null) {
							mServiceView.displayData(value, (byte) value.length);
							mServiceView.setStatus(mReadMsg);
						} else
							mServiceView.setError("No data");
					} else if  (BluetoothLeService.ACTION_DATA_WRITE.equals(action))
					{
						mServiceView.setStatus(mWriteMsg);
					}
				} else 
				{
					setError(status);
				}

			} else 
			{
				Log.e(TAG, "ServiceView not ready");
			}
		}
	};
	
	//This function is called when the ServiceView is inflated :)
	
	void onServiceViewReady(View v) {
		// Display service information
		setTitle(mServiceName);

		// GUI
		TextView uuidView = (TextView) v.findViewById(R.id.txt_uuid);
		uuidView.setText(mService.getUuid().toString().toUpperCase());

		// Update GATT view
		
		if (mServiceView != null) {
			updateView();
		}
	}

	void onInfoViewReady(View v) {
		// Update GATT view
		if (mServiceView != null) {
			mServiceView.selectCurrentItem();
		}
	}

	List<BluetoothGattCharacteristic> getCharList() 
	{
		return mCharList;
	}

	void updateView() {
		if (mServiceView != null) {
			mCharList = mService.getCharacteristics();
			mServiceView.notifyDataSetChanged();
		}
	}

	void onSelectionUpdate(BluetoothGattCharacteristic charac) {
		UUID uuid = charac.getUuid();
		mUuidDataStr = uuid.toString();
		String name = GattInfo.uuidToName(uuid);
		String descr = GattInfo.getDescription(uuid);
		String html = "<body><h1>" + name + "</h1><p><u>"
		    + mUuidDataStr.toUpperCase() + "</u></p>";

		if (descr != null) {
			html += descr;
		}
		html += "</body>";
	}

	private void setError(int status) {
		
		if (mServiceView != null) {
			String txt = String.format("Error: %d", status);
			mServiceView.setError(txt);
		}
	}

}