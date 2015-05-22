package ti.android.ble.devicemonitor;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Configuration;



public class Display extends Fragment{

	
	private GraphicalView view1;
    int i=0;
	private TimeSeries dataset = new TimeSeries("PPG"); 
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// This will be used to customize line 1	
	private XYSeriesRenderer renderer = new XYSeriesRenderer(); 
	// Holds a collection of XYSeriesRenderer and customizes the graph
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	//Size of jumps when scrolling to the left
	final int CHART_SCROLL_INC = 20;	

	public LinearLayout chartContainer;
	public int u=0;
	
	ServiceActivity mActivity;
	private Context mContext;

	private static String TAG = "Display";

	public Display() {
		Log.i(TAG, "construct");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		
		// The last two arguments ensure LayoutParams are inflated properly.
		View rootView = inflater.inflate(R.layout.display_layout, container, false);
		
		mActivity = (ServiceActivity) getActivity(); //added this on 4/17
		mContext = mActivity.getApplicationContext(); //added this on 4/17
		
	     chartContainer = (LinearLayout) rootView.findViewById(R.id.chart_container);		 
	     openChart();
	
		 return rootView;

	} 
	
	
	void openChart()
	 {      
		        mDataset.addSeries(dataset);
				// Customization time for line 1!
				renderer.setColor(Color.RED);
				renderer.setFillPoints(true);
				mRenderer.setLabelsTextSize(8*getResources().getDisplayMetrics().density);
				
				//Enable external zoom buttons
			//TODO: ZOOM YET TO BE ANABLED
		    // Enable Zoom
			//	mRenderer.setZoomButtonsVisible(true);
			//	mRenderer.setZoomEnabled(true);
			//	mRenderer.setExternalZoomEnabled(true);
				
				mRenderer.setXTitle("Time");				
			    mRenderer.setShowGrid(true);
			    
				mRenderer.setYTitle("Incoming Data");	
			    
			    //The number of values that can be displayed on the respective axis
				mRenderer.setYLabels(10);
		        mRenderer.setXLabels(5);		 
				mRenderer.setAxisTitleTextSize(12*getResources().getDisplayMetrics().density);
				// Add single renderer to multiple renderer
				mRenderer.addSeriesRenderer(renderer);	
		////TODO: ZOOM FEATURES YET TO BE ADDED		
		//		mRenderer.setZoomEnabled(true, true);
				
			   mRenderer.setPanEnabled(true, true); //use to lock or unlock the x-axis and the y-axis
			   mRenderer.setScale((float) 1);
               mRenderer.setYLabelsAlign(Align.RIGHT);
               mRenderer.setXLabelsAlign(Align.CENTER);
          
   
             mRenderer.setLegendTextSize(12*getResources().getDisplayMetrics().density);
              mRenderer.setSelectableBuffer(20);
              //The 3 lines below set the margin sizes depending on the screen density
  	        float f= 10*getResources().getDisplayMetrics().density;
            int size = Math.round(f);
            /*margins - an array containing the 
             * margin size values, in this order: top, left, bottom, right
             * in this case for top= 10*getResources....
             * left=30=4*getResources
             * bottom=30=4*getResources
            */
            mRenderer.setMargins(new int[]{ size,4*size, size, size });	  
            mRenderer.setClickEnabled(true);	
		    view1 =  ChartFactory.getLineChartView(getActivity().getApplicationContext(), mDataset, mRenderer);		
		    
		    // Adding the Line Chart to the LinearLayout
		    chartContainer.addView(view1,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		        
	 }
	
//This function is called to plot the data coming from 
//characteristic #6
public void AddnewPoint(int value )
{
	i++;
	//Command to add the plot
	try{
		//so it will delete values that are identified by the index 0
		if (i>=100)
		{
		dataset.remove(0);
		}

	dataset.add(i, value);
	view1.repaint();	
	}
	catch(Exception e)
	{
		//Log.e(tag, msg)
	}
	
}	
}
