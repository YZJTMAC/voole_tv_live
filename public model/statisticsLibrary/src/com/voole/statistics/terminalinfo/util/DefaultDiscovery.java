/*
 * Copyright (C) 2009-2010 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 2, see README
 */

package com.voole.statistics.terminalinfo.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

@SuppressLint("NewApi")
public class DefaultDiscovery extends AsyncTask<Void, HostBean, Void> {

	protected int hosts_done = 0;
	public List<HostBean> beans = new ArrayList<HostBean>() ;

	protected long ip;
	protected long start = 0;
	protected long end = 0;

	private final String TAG = "DefaultDiscovery";
	private final static int[] DPORTS = { 139, 445, 22, 80 };
	private final static int TIMEOUT_SCAN = 3600; // seconds
	private final static int TIMEOUT_SHUTDOWN = 10; // seconds
	private final static int THREADS = 10; //FIXME: Test, plz set in options again ?
	private final int mRateMult = 5; // Number of alive hosts between Rate
	private int pt_move = 2; // 1=backward 2=forward
	private ExecutorService mPool;
	private boolean doRateControl;
	private RateControl mRateControl;
	private AsyncPortscan portAsyn;
	@SuppressLint("NewApi")
//	private ExecutorService exec = Executors.newFixedThreadPool(4);

	public void setNetwork(long ip) {
		int shift = (32 - NetInfo.cidr);
		if (NetInfo.cidr < 31) {
			this.start = (ip >> shift << shift) + 1;
			this.end = (ip | ((1 << shift) - 1)) - 1;
		} else {
			this.start = (ip >> shift << shift);
			this.end = (ip | ((1 << shift) - 1));
		}
		this.ip = ip;
		Log.e(TAG, "start---->"+start);
		Log.e(TAG, "end---->"+end);
		Log.e(TAG, "ip---->"+ip);
	}
	public DefaultDiscovery() {
		super();
		mRateControl = new RateControl();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	//	executeOnExecutor();
	@SuppressLint("NewApi")
	@Override
	protected void onProgressUpdate(HostBean... host) {
		if (!isCancelled()) {
			if (host[0] != null) {
				Log.e("TAG", host[0].ipAddress+"---" + host[0].hardwareAddress);
				beans.add(host[0]);
			}
		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(Void unused) {
		Log.d("xx", "onPostExecute()");
		//		Log.e("TAG", "host-->size" +  beans.size());
		//		for (int i = 0; i < beans.size(); i++) {
		//			Log.e("TAG", "PORTSIZE--->"+ beans.get(i).portsOpen.size());
		if (portAsyn != null && portAsyn.getStatus() != AsyncTask.Status.FINISHED) {
			portAsyn.cancel(true);
			portAsyn.onPortCancelled();
		}
		portAsyn = new AsyncPortscan(beans);
		//			portAsyn.executeOnExecutor(exec);
		portAsyn.execute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		mPool = Executors.newFixedThreadPool(THREADS);
		if (ip <= end && ip >= start) {
			Log.i(TAG, "Back and forth scanning");
			// gateway
			launch(start);

			// hosts
			long pt_backward = ip;
			long pt_forward = ip + 1;
			long size_hosts = end - start;

			for (int i = 0; i < size_hosts; i++) {
				// Set pointer if of limits
				if (pt_backward <= start) {
					pt_move = 2;
				} else if (pt_forward > end) {
					pt_move = 1;
				}
				// Move back and forth
				if (pt_move == 1) {
					launch(pt_backward);
					pt_backward--;
					pt_move = 2;
				} else if (pt_move == 2) {
					launch(pt_forward);
					pt_forward++;
					pt_move = 1;
				}
			}
		} else {
			Log.i(TAG, "Sequencial scanning");
			for (long i = start; i <= end; i++) {
				launch(i);
			}
		}
		mPool.shutdown();
		try {
			if(!mPool.awaitTermination(TIMEOUT_SCAN, TimeUnit.SECONDS)){
				mPool.shutdownNow();
				Log.e(TAG, "Shutting down pool");
				if(!mPool.awaitTermination(TIMEOUT_SHUTDOWN, TimeUnit.SECONDS)){
					Log.e(TAG, "Pool did not terminate");
				}
			}
		} catch (InterruptedException e){
			Log.e(TAG, e.getMessage());
			mPool.shutdownNow();
			Thread.currentThread().interrupt();
		} finally {
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	public void onCancelledPortAsync() {
		if (mPool != null) {
			synchronized (mPool) {
				mPool.shutdownNow();
			}
		}
		if (portAsyn != null && portAsyn.getStatus() != AsyncTask.Status.FINISHED) {
			portAsyn.cancel(true);
			portAsyn.onPortCancelled();
		}
	}

	private void launch(long i) {
		if(!mPool.isShutdown()) {
			mPool.execute(new CheckRunnable(NetInfo.getIpFromLongUnsigned(i)));
		}
	}

	private int getRate() {
		if (doRateControl) {
			return mRateControl.rate;
		}

		return Integer.parseInt("500");
	}

	private class CheckRunnable implements Runnable {
		private String addr;

		CheckRunnable(String addr) {
			this.addr = addr;
		}

		public void run() {
			if(isCancelled()) {
				publish(null);
			}
			// Create host object
			final HostBean host = new HostBean();
			host.responseTime = getRate();
			host.ipAddress = addr;
			try {
				InetAddress h = InetAddress.getByName(addr);
				// Rate control check
				if (doRateControl && mRateControl.indicator != null && hosts_done % mRateMult == 0) {
					mRateControl.adaptRate();
				}
				// Arp Check #1
				host.hardwareAddress = HardwareAddress.getHardwareAddress(addr);
				if(!NetInfo.NOMAC.equals(host.hardwareAddress)){
					publish(host);
					return;
				}
				// Native InetAddress check
				if (h.isReachable(getRate())) {
					publish(host);
					// Set indicator and get a rate
					if (doRateControl && mRateControl.indicator == null) {
						mRateControl.indicator = addr;
						mRateControl.adaptRate();
					}
					return;
				}
				// Arp Check #2
				host.hardwareAddress = HardwareAddress.getHardwareAddress(addr);
				if(!NetInfo.NOMAC.equals(host.hardwareAddress)){
					publish(host);
					return;
				}
				// TODO: Get ports from options
				Socket s = new Socket();
				for (int i = 0; i < DPORTS.length; i++) {
					try {
						s.bind(null);
						s.connect(new InetSocketAddress(addr, DPORTS[i]), getRate());
					} catch (IOException e) {
					} catch (IllegalArgumentException e) {
					} finally {
						try {
							s.close();
						} catch (Exception e){
						}
					}
				}

				// Arp Check #3
				host.hardwareAddress = HardwareAddress.getHardwareAddress(addr);
				if(!NetInfo.NOMAC.equals(host.hardwareAddress)){
					publish(host);
					return;
				}
				publish(null);

			} catch (IOException e) {
				publish(null);
				Log.e(TAG, e.getMessage());
			} 
		}
	}

	private void publish(final HostBean host) {
		hosts_done++;
		if(host == null){
			publishProgress((HostBean) null);
			return; 
		}

		// Mac Addr not already detected
		if(NetInfo.NOMAC.equals(host.hardwareAddress)){
			host.hardwareAddress = HardwareAddress.getHardwareAddress(host.ipAddress);
		}

		// Is gateway ?
		//		if (NetInfo.gatewayIp.equals(host.ipAddress)) {
		//			host.deviceType = HostBean.TYPE_GATEWAY;
		//		}

		// FQDN
		// Static
		// DNS
		try {
			host.hostname = (InetAddress.getByName(host.ipAddress)).getCanonicalHostName();
		} catch (UnknownHostException e) {
			Log.e(TAG, e.getMessage());
		}
		// TODO: NETBIOS

		publishProgress(host);
	}
}
