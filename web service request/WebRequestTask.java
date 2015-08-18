package com.mdm.communicator;
/**
 * This class is used for handling web api calling in background.
 * 
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2015-02-26
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class WebRequestTask extends AsyncTask<Void, Integer, Void> {
	private ProgressDialog mDialog;
	private Context mContext;
	private Handler mHandler;
	private List<NameValuePair> nameValuePairs;
	private int mPId;
	private boolean isShowDialog;

	/**
	 * @param context
	 * @param nameValuePairs
	 * @param handler
	 * @param pid
	 * @param showDiaolog
	 */
	public WebRequestTask(Context context, List<NameValuePair> nameValuePairs,
			Handler handler ,int pid,boolean showDiaolog) {
		this.mContext = context;
		this.mHandler = handler;
		this.nameValuePairs = nameValuePairs;
		this.mPId=pid;
		this.isShowDialog= showDiaolog;
	}

	/**
	 * @param context
	 */
	public WebRequestTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		try {
			if(isShowDialog){
				mDialog = new ProgressDialog(mContext);
				mDialog.setCancelable(false);
				mDialog.setMessage("Please wait...");
				mDialog.show();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			if(isShowDialog && mDialog!=null)
				mDialog.dismiss();
			super.onPostExecute(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(WebServiceDetails.SERVER_URL);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			String responseString = EntityUtils.toString(response.getEntity());
			Log.v("", "responses sdf : " + responseString);
			Message message = mHandler.obtainMessage();
			message.obj = responseString;
			message.what = this.mPId;
			mHandler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			Message message = mHandler.obtainMessage();
			message.obj = "error";
			message.what = 0;
			mHandler.sendMessage(message);
		}
		return null;
	}
}

