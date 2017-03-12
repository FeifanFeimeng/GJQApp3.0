package com.ustc.gjqapp.activity;

import java.util.List;

public class Util {
	public String getContent(String string) {

		return string;
	}
}

class Status {
	private String error;
	private String status;
	private String date;
	private List<Results> results;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Results> getResults() {
		return results;
	}

	public void setResults(List<Results> results) {
		this.results = results;
	}
}

class Results {
	private String currentCity;
	private String pm25;
	private List<Weather> weather_data;
	private List<IndexData> index;

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getPM25() {
		return pm25;
	}

	public void setPM25(String pm25) {
		this.pm25 = pm25;
	}

	public List<Weather> getWeather_data() {
		return weather_data;
	}

	public void setWeather_data(List<Weather> weatherData) {
		weather_data = weatherData;
	}

	public List<IndexData> getIndext() {
		return index;
	}

	public void setIndex(List<IndexData> indexData) {
		index = indexData;
	}
}

class Weather {
	private String date;
	private String dayPictureUrl;
	private String nightPictureUrl;
	private String weather;
	private String wind;
	private String temperature;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayPictureUrl() {
		return dayPictureUrl;
	}

	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}

	public String getNightPictureUrl() {
		return nightPictureUrl;
	}

	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
}

class IndexData {
	private String title;
	private String zs;
	private String tipt;
	private String des;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getZS() {
		return zs;
	}

	public void setZS(String zs) {
		this.zs = zs;
	}

	public String getTipt() {
		return tipt;
	}

	public void setTipt(String tipt) {
		this.tipt = tipt;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
