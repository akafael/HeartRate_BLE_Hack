/**************************************************************************************************
  Filename:       BleDeviceInfo.java
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
package ti.android.ble.common;

import android.bluetooth.BluetoothDevice;

public class BleDeviceInfo {
	// Data
	private BluetoothDevice mBtDevice;
	private int mRssi;

	public BleDeviceInfo(BluetoothDevice device, int rssi) {
		mBtDevice = device;
		mRssi = rssi;
	}

	public BluetoothDevice getBluetoothDevice() {
		return mBtDevice;
	}

	public int getRssi() {
		return mRssi;
	}

	public void updateRssi(int rssiValue) {
		mRssi = rssiValue;
	}

}
