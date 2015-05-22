package ti.android.ble.devicemonitor;

import java.math.BigInteger;

import ti.android.util.Conversion;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Heart_Rate extends Fragment
{
	
	View rootView;
	private static String TAG= "Heart Rate";
	public void setValue(byte[] value){
		try{
			TextView txtvi = (TextView) rootView.findViewById(R.id.textView2);
			//TODO: convert bytes[] to decimal
		int l = new BigInteger(value).intValue();
		//	int l = value.length;
			txtvi.setText(Integer.toString(l));
			
		}
		catch (Exception e)
		{
			Log.e(TAG,"Conversion failure");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.heart_rate_display, container, false);
		return rootView;
	}
}
