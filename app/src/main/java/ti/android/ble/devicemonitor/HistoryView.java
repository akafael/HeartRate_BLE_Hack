package ti.android.ble.devicemonitor;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.app.ActionBar.LayoutParams;

//in order to implement the ClickListener
//implements onClickListener must be added :)
public class HistoryView extends Fragment implements OnClickListener{

	public final String ACTION_DATA_STORED="your.tutorial.graph.DATA_STORED";
	int u=10;
	private MainActivity mActivity = null;
	
 
	private static String TAG = "DynamicGraphActivity";
	private Context mContext;
	
	//Deals with charting (Look at the sliding drawer app)
	private GraphicalView mChartView;	
    public LinearLayout Container;	
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize line 1
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
    private XYSeries series = new XYSeries("PPG");
    int i=0;
    
	/** Called when the activity is first created. */
//This is the same onCreateView function is used when we want to inflate another view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.history_layout, container, false);
		mActivity = (MainActivity) getActivity();
		mContext = mActivity.getApplicationContext();
		final Button graph=(Button)v.findViewById(R.id.button1);
		graph.setOnClickListener(this);
		Container = (LinearLayout) v.findViewById(R.id.chart_container1); 
		Graph();
        return v;
	}
	
	
	public void onClick(View v) 
	{	switch (v.getId()) 
		{
		case R.id.button1:
		{	Random random = new Random();
	    int someInt = random.nextInt(50000);
			AddnewPoint(someInt);
			i++;
		}
			break;
	    default:
			break;
		// TODO Auto-generated method stub
		}
	}
	 
	
	public void Graph()
	{	//This function only creates the features of the graph
   		  mDataset.addSeries(series);
	      //  mRenderer.setXAxisMin(0);
	      //  mRenderer.setXAxisMax(50);
	      //  mRenderer.setYAxisMin(0);
	    //    mRenderer.setYAxisMax(90);   

            mRenderer.setXTitle("Time");				
		    mRenderer.setShowGrid(true);
		    
			mRenderer.setYTitle("Heart Rate (bpm)");
			mRenderer.setLabelsTextSize(8*getResources().getDisplayMetrics().density);

			mRenderer.setAxisTitleTextSize(12*getResources().getDisplayMetrics().density);

            mRenderer.setLegendTextSize(12*getResources().getDisplayMetrics().density);

	    //    mRenderer.addSeriesRenderer(renderer);
	        // set some renderer properties
	        renderer.setPointStyle(PointStyle.CIRCLE);
	        renderer.setFillPoints(true);
	    //    renderer.setDisplayChartValues(true);
	    // Customization time for line 1!
			renderer.setColor(Color.RED);
			renderer.setPointStyle(PointStyle.DIAMOND);
			renderer.setFillPoints(true);
		// Enable Zoom
		//	mRenderer.setZoomButtonsVisible(true);	
		    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		    mRenderer.setXTitle(currentDateTimeString);
		
		    mRenderer.setShowGrid(true);
		    mRenderer.setYTitle("Heart Rate (bpm)");
			// Add single renderer to multiple renderer
			mRenderer.addSeriesRenderer(renderer);	
	//		mRenderer.setZoomEnabled(true, true);
			mRenderer.setPanEnabled(true, true); //use to lock or unlock the x-axis and the y-axis
			mRenderer.setScale((float) 1);
			mRenderer.setYLabels(15);
	        mRenderer.setYLabelsAlign(Align.RIGHT);
	        mRenderer.setXLabels(15);
	        mRenderer.setXLabelsAlign(Align.CENTER);
	        //The 3 lines below set the margin sizes depending on the screen density
	        float f= 10*getResources().getDisplayMetrics().density;
            int size = Math.round(f);
            //margins - an array containing the 
            //margin size values, in this order: top, left, bottom, right
            /*in this case for top= 10*getResources....
             * left=30=4*getResources
             * bottom=30=4*getResources
            */
            
            //In our actual code I must add set this command to true whenever the 
            //the user clicks start and when he hits stop
            //it will be false
            
            //When it is false, once the user touches the plot,
            //the scrolling will be disabled
            mRenderer.setClickEnabled(true);
            mRenderer.setMargins(new int[]{ size,4*size, size, size });	
               
		 
	        mChartView = ChartFactory.getLineChartView(getActivity().getApplicationContext(),mDataset , mRenderer);		      
	       Container.addView(mChartView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

void AddnewPoint(int someInt)
{   if(i==1)
	{Toast.makeText(getActivity().getApplicationContext(), "Button is working", Toast.LENGTH_LONG )
    .show();
	}
	
   // double y= (double)someInt;
 	series.add(i, someInt);
	mChartView.repaint();		
	
	
}	

	
}