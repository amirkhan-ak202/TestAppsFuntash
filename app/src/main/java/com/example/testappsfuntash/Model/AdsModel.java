package com.example.testappsfuntash.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Native;
import java.util.List;
public class AdsModel {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AdResponse{" +
                "message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {

        @SerializedName("ads")
        private List<Ad> ads;

        @SerializedName("banners")
        private List<Banner> banners;

        @SerializedName("natives")
        private List<NativeAd> natives;

        @SerializedName("interstitals")
        private List<Interstitial> interstitials;
        // Define getter and setter methods for each list


        public List<Ad> getAds() {
            return ads;
        }

        public void setAds(List<Ad> ads) {
            this.ads = ads;
        }

        public List<Banner> getBanners() {
            return banners;
        }

        public void setBanners(List<Banner> banners) {
            this.banners = banners;
        }

        public List<NativeAd> getNatives() {
            return natives;
        }

        public void setNatives(List<NativeAd> natives) {
            this.natives = natives;
        }

        public List<Interstitial> getInterstitals() {
            return interstitials;
        }

        public void setInterstitals(List<Interstitial> interstitals) {
            this.interstitials = interstitals;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "ads=" + ads +
                    ", banners=" + banners +
                    ", natives=" + natives +
                    ", interstitals=" + interstitials +
                    '}';
        }
    }

    public static class Ad {
        @SerializedName("id")
        private String id;

        @SerializedName("app_id")
        private String appId;

        @SerializedName("game_id")
        private String gameId;

        // Define getter and setter methods


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "id='" + id + '\'' +
                    ", appId='" + appId + '\'' +
                    ", gameId='" + gameId + '\'' +
                    '}';
        }
    }

    public static class Banner {
        @SerializedName("id")
        private String id;

        @SerializedName("banner_type")
        private String bannerType;

        @SerializedName("banner_adunit")
        private String bannerAdUnit;

        // Define getter and setter methods


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBannerType() {
            return bannerType;
        }

        public void setBannerType(String bannerType) {
            this.bannerType = bannerType;
        }

        public String getBannerAdUnit() {
            return bannerAdUnit;
        }

        public void setBannerAdUnit(String bannerAdUnit) {
            this.bannerAdUnit = bannerAdUnit;
        }

        @Override
        public String toString() {
            return "Banner{" +
                    "id='" + id + '\'' +
                    ", bannerType='" + bannerType + '\'' +
                    ", bannerAdUnit='" + bannerAdUnit + '\'' +
                    '}';
        }
    }

    public static class NativeAd {
        @SerializedName("id")
        private String id;

        @SerializedName("native_type")
        private String nativeType;

        @SerializedName("native_adunit")
        private String nativeAdUnit;

        @SerializedName("native_how_many_click")
        private String nativeHowManyClick;
        // Define getter and setter methods

        @Override
        public String toString() {
            return "NativeAd{" +
                    "id='" + id + '\'' +
                    ", nativeType='" + nativeType + '\'' +
                    ", nativeAdUnit='" + nativeAdUnit + '\'' +
                    ", nativeHowManyClick='" + nativeHowManyClick + '\'' +
                    '}';
        }
    }

    public static class Interstitial {
        @SerializedName("id")
        private String id;

        @SerializedName("interstital_type")
        private String interstitialType;

        @SerializedName("interstital_adunit")
        private String interstitialAdUnit;

        @SerializedName("interstital_how_many_click")
        private String interstitialHowManyClick;

        // Define getter and setter methods


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInterstitialType() {
            return interstitialType;
        }

        public void setInterstitialType(String interstitialType) {
            this.interstitialType = interstitialType;
        }

        public String getInterstitialAdUnit() {
            return interstitialAdUnit;
        }

        public void setInterstitialAdUnit(String interstitialAdUnit) {
            this.interstitialAdUnit = interstitialAdUnit;
        }

        public String getInterstitialHowManyClick() {
            return interstitialHowManyClick;
        }

        public void setInterstitialHowManyClick(String interstitialHowManyClick) {
            this.interstitialHowManyClick = interstitialHowManyClick;
        }

        @Override
        public String toString() {
            return "Interstitial{" +
                    "id='" + id + '\'' +
                    ", interstitialType='" + interstitialType + '\'' +
                    ", interstitialAdUnit='" + interstitialAdUnit + '\'' +
                    ", interstitialHowManyClick='" + interstitialHowManyClick + '\'' +
                    '}';
        }
    }
}