package com.nexters.house.adapter;

import java.util.*;

import android.annotation.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.entity.DataObject;

public class HorzGridViewAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<DataObject> mHorzData;	
	
	//HorzGridView stuff
	private int childLayoutResourceId = R.layout.horz_gridview_child_layout;
	private int columns;//Used to set childSize in TwoWayGridView
	private int rows;//used with TwoWayGridView
	private int itemPadding;
	private int columnWidth;
	private int rowHeight;

	public HorzGridViewAdapter(Context context,List<DataObject> data,final int type){
		childLayoutResourceId = R.layout.horz_gridview_child_layout;
		this.mContext = context;
		this.mHorzData = data;
		//Get dimensions from values folders; note that the value will change
		//based on the device size but the dimension name will remain the same
		Resources res = mContext.getResources();
		itemPadding = (int) res.getDimension(R.dimen.horz_item_padding);
		int[] rowsColumns = res.getIntArray(R.array.horz_gv_rows_columns);
		rows = rowsColumns[0];
		columns = rowsColumns[1];
		
		//Initialize the layout params
		ViewTreeObserver vto;
		if(type==1){
			//HorzGridView size not established yet, so need to set it using a viewtreeobserver
			TalkWriteActivity.mTalkHorzGridView.setNumRows(rows);
			vto = TalkWriteActivity.mTalkHorzGridView.getViewTreeObserver();
		}
		else{
			InteriorWrite2Activity.mInteriorGridView2.setNumRows(rows);
			vto=InteriorWrite2Activity.mInteriorGridView2.getViewTreeObserver();
		}
		
		OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				ViewTreeObserver vto;
				//First use the gridview height and width to determine child values
				if(type == 1){
					rowHeight =(int)((float)(TalkWriteActivity.mTalkHorzGridView.getHeight()/rows)-2*itemPadding);
					columnWidth = (int)((float)(TalkWriteActivity.mTalkHorzGridView.getWidth()/columns)-2*itemPadding);				
					TalkWriteActivity.mTalkHorzGridView.setRowHeight(rowHeight);
					vto = TalkWriteActivity.mTalkHorzGridView.getViewTreeObserver();
				}
				else{
					rowHeight =(int)((float)(InteriorWrite2Activity.mInteriorGridView2.getHeight()/rows)-2*itemPadding);
					columnWidth = (int)((float)(InteriorWrite2Activity.mInteriorGridView2.getWidth()/columns)-2*itemPadding);
					InteriorWrite2Activity.mInteriorGridView2.setRowHeight(rowHeight);
					vto = InteriorWrite2Activity.mInteriorGridView2.getViewTreeObserver();
				}
				
				//Then remove the listener
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					vto.removeOnGlobalLayoutListener(this);
				}else{
					vto.removeGlobalOnLayoutListener(this);
				}
			}
		};
		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get the data for the given position in the array
		DataObject thisData = mHorzData.get(position);
		
		//Use a viewHandler to improve performance
		ViewHandler handler;
		
		//If reusing a view get the handler info; if view is null, create it
		if(convertView == null){
			//Only get the inflater when it's needed, then release it-which isn't frequently
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(childLayoutResourceId , parent, false);
			
			//User findViewById only when first creating the child view
			handler = new ViewHandler();
			handler.iv = (ImageView) convertView.findViewById(R.id.horz_gv_iv);
			
			convertView.setTag(handler);
		}else{
			handler = (ViewHandler) convertView.getTag();
		}
		//Set the data outside once the handler and view are instantiated
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize=4;
		Bitmap bmp=BitmapFactory.decodeFile(thisData.getName(),options);
		handler.iv.setImageBitmap(bmp); //이미지 그려주기
		
		FrameLayout.LayoutParams lp 
			= new FrameLayout.LayoutParams(columnWidth, rowHeight);// convertView.getLayoutParams();
		handler.iv.setLayoutParams(lp);
	
//		Log 
//		Log.d("HorzGVAdapter","Position:"+position+",children:"+parent.getChildCount());
		return convertView;
	}
	
	private class ViewHandler{
		ImageView iv;
		TextView tv;
	}

	@Override
	public int getCount() {
		return mHorzData.size();
	}

	@Override
	public Object getItem(int position) {
		return mHorzData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setHorzData(List<DataObject> horzData){
		mHorzData = horzData;
	}
}
