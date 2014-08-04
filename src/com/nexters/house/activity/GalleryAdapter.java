package com.nexters.house.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nexters.house.activity.CustomGallery;
import com.nexters.house.activity.GalleryAdapter.ViewHolder;
import com.nexters.house.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GalleryAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater infalter;
	public static ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
	ImageLoader imageLoader;
	public static ArrayList<CustomGallery> dataChecked = new ArrayList<CustomGallery>();
	private boolean isActionMultiplePick;
	public static int selectCnt = 0;
	public static boolean isShow = true;
	public static ArrayList<CustomGallery> tmps = null;
	
	public GalleryAdapter(Context c, ImageLoader imageLoader) {
		infalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = c;
		this.imageLoader = imageLoader;
		// clearCache();
	}

	@Override
	public int getCount() {

		if(isShow)
			tmps = data;
		else 
			tmps = dataChecked;
		
		return tmps.size();
	}

	@Override
	public CustomGallery getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}

	public void selectAll(boolean selection) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).isSeleted = selection;

		}
		notifyDataSetChanged();
	}

	public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < data.size(); i++) {
			if (!data.get(i).isSeleted) {
				isAllSelected = false;
				break;
			}
		}

		return isAllSelected;
	}

	public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				isAnySelected = true;
				break;
			}
		}

		return isAnySelected;
	}

	public ArrayList<CustomGallery> getSelected() {
		/*
		 * ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
		 * 
		 * for (int i = 0; i < data.size(); i++) { if (data.get(i).isSeleted) {
		 * dataT.add(data.get(i)); } }
		 * 
		 * return dataT;
		 */
		return dataChecked;
	}

	public void addAll(ArrayList<CustomGallery> files) {

		try {
			// this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}

	public void changeSelection(View v, int position) {

		if (data.get(position).isSeleted) {
			data.get(position).isSeleted = false;
			dataChecked.remove(data.get(position)); // 들어가있는거 빼기
			if (selectCnt != 0)
				selectCnt--;
		} else {
			data.get(position).isSeleted = true;
			selectCnt++;
			dataChecked.add(data.get(position)); // 체크된거 넣기
		}

		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
				.get(position).isSeleted);
	}

	public void deleteItem(View v, int position) {
		data.get(position).isSeleted = false;
		selectCnt--;
		dataChecked.remove(data.get(position));

		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
				.get(position).isSeleted);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		
		if(isShow)
			tmps = data;
		else 
			tmps = dataChecked;
		
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);

			holder.imgQueueMultiSelected = (ImageView) convertView
					.findViewById(R.id.imgQueueMultiSelected);

			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);

		try {
			imageLoader.displayImage("file://" + tmps.get(position).sdcardPath,
					holder.imgQueue, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.imgQueue
									.setImageResource(R.drawable.no_media);
							super.onLoadingStarted(imageUri, view);
						}
					});

			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected
						.setSelected(tmps.get(position).isSeleted);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
	}

	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		data.clear();
		dataChecked.clear();
		notifyDataSetChanged();
	}
}
