package com.hj.singlejoker;


import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.loader.Fetcher;

public class AD {

	public static MainActivity ma;


	public static final String ADS_APP_ID = "100010037";
	public static final String ADS_SECRET_KEY = "da0feb4c4bfa471533dd781d3a010186";
	public static final String TAG_INTERSTITIAL_FULLSCREEN = "158a9e9a9f335254c1f58ea1567d65dc";
	public static final String TAG_INTERSTITIAL_List="60ebcc6af3b6824c7f1ab56452f5124d";
	public static Runnable showWandoujia = new Runnable() {
		public void run() {
			 if (Ads.isLoaded(Fetcher.AdFormat.interstitial, TAG_INTERSTITIAL_FULLSCREEN)) {
				 Ads.showAppWidget(ma, null, TAG_INTERSTITIAL_FULLSCREEN, Ads.ShowMode.FULL_SCREEN);
			    }
		}
	};
	public static Runnable showWandoujiaList = new Runnable() {
		public void run() {
			 if (Ads.isLoaded(Fetcher.AdFormat.appwall, TAG_INTERSTITIAL_List)) {
				 Ads.showAppWall(ma, TAG_INTERSTITIAL_List);
			    }
		}
	};
	public static void showWandoujia()
	{
		if(ScoreManager.getScore()<16000)
			ma.handler.post(showWandoujia);
	}
	public static void showWandoujiaList()
	{
		ma.handler.post(showWandoujiaList);
	}

}
