package com.nexters.house.fragment;

import java.util.*;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.AsyncTask;
import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;
import com.nexters.house.entity.*;

public class InteriorFragment extends Fragment implements OnRefreshListener {

	private final String TAG = "InteriorFragment";

	private PullToRefreshLayout mPullToRefreshLayout;

	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private ListView lv_main;
	private InteriorAdapter mListAdapter;
	private Button btn_write;
	private Boolean loading = true;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_interior, container, false);
		initResources(v);
		initEvents();

		return v;
	}


	@SuppressLint("InflateParams")
	private void initResources(View v){

		mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity())
		.allChildrenArePullable()
		.listener(this)
		.setup(mPullToRefreshLayout);

		btn_write=(Button)v.findViewById(R.id.btn_write);
		btn_write.setText("쓰기");
		btn_write.bringToFront();

		 mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.ptr_layout);
		lv_main = (ListView) v.findViewById(R.id.lv_interior_view);
		mInteriorItemArrayList = new ArrayList<InteriorEntity>();

		View footerView = ((LayoutInflater)getActivity().
				getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);

		mListAdapter = new InteriorAdapter(getActivity().
				getApplicationContext(), mInteriorItemArrayList, R.layout.custom_view_interior);

		//footerview를  listview 제일 하단에 붙임 
		lv_main.addFooterView(footerView);
		lv_main.setAdapter(mListAdapter);

		//Load the first 5 items
		Thread thread =  new Thread(null, loadListItems);
		thread.start();


	}

	private void initEvents(){
		btn_write.setOnClickListener(clickListener);
		lv_main.setOnScrollListener(scrollListener);


		// Set a listener to be invoked when the list should be refreshed.
		//        ((PullToRefreshListView) lv_main).setOnRefreshListener(new OnRefreshListener() {
		//            @Override
		//            public void onRefresh() {
		////            	Thread thread =  new Thread(null, refreshListItem);
		////				thread.start();
		//                // Do work to refresh the list here.
		//                
		//            }
		//        });
	}

	private Runnable loadListItems = new Runnable(){
		@Override
		public void run(){
			loading = true;

			mHandler.sendEmptyMessage(0);

		}
	};

	private Runnable loadMoreListItems = new Runnable() {			
		@Override
		public void run() {
			loading = true;

			mHandler.sendEmptyMessage(1);

		}
	};	

	private Runnable refreshListItem = new Runnable(){
		@Override
		public void run(){
			mHandler.sendEmptyMessage(2);
		}
	};

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				//처음 로딩할때 몇개로딩할지 여기서 결정하고 보여주는거.
				for(int i=0;i<5 ;i++){
					mListAdapter.add();
				}
				mListAdapter.notifyDataSetChanged();
				//Done loading more.
				loading = false;
				break;
			case 1:
				//스크롤해서 리스트3개씩 추가해주는부분 
				for (int i = 0; i < 3; i++) {		
					mListAdapter.add();	
				}
				mListAdapter.notifyDataSetChanged();
				loading = false;
				break;
			case 2:
				//	        	InteriorEntity e = new InteriorEntity();
				//	        	e.id = "refreshId";
				//	        	e.category ="refresh";
				//	        	e.content = "refresh content";
				//	    		e.image_urls = new ArrayList<String>(){{
				//	    			add("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/t1.0-9/10487348_570244969751450_1480175892860352468_n.jpg");
				//	    			add("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xpa1/t1.0-9/1546376_570244983084782_3616217572638065925_n.jpg");
				//	    			add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
				//	    			}};
				//    			e.badge = 0;
				//    			e.reply = 1;
				//    			e.scrap = 1;
				//    			e.share = 1;
				//    			mInteriorItemArrayList.add(0,e);
				//    			mListAdapter.notifyDataSetChanged();
				break;
			case 3:
				break;
			}
		}
	};

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(getActivity(),SelectWriteActivity.class);
			startActivity(intent);
		}
	};

	private OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			//what is the bottom iten that is visible
			int lastInScreen = firstVisibleItem + visibleItemCount;				

			//is the bottom item visible & not loading more already ? Load more !
			if((lastInScreen == totalItemCount) && !(loading)){

				Thread thread =  new Thread(null, loadMoreListItems);
				thread.start();
			}

		}
	};






	@Override
	public void onRefreshStarted(View view) {
		/**
		 * Simulate Refresh with 4 seconds sleep
		 */
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@SuppressWarnings("serial")
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				InteriorEntity e = new InteriorEntity();
				e.id = "refreshId";
				e.category ="refresh";
				e.content = "refresh content";
				e.image_urls = new ArrayList<String>(){{
					add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
				}};
				e.badge = 0;
				e.reply = 0;
				e.scrap = 0;
				e.share = 0;
				mInteriorItemArrayList.add(0,e);
				mListAdapter.notifyDataSetChanged();
				// Notify PullToRefreshLayout that the refresh has finished
				mPullToRefreshLayout.setRefreshComplete();
			}
		}.execute();
	}

}