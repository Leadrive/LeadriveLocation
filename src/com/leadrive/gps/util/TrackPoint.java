package com.leadrive.gps.util;

public class TrackPoint {
	private double lat;
	private double lng;
	private double h;
	private float speed;
	private float bearing;
	private int score;
	private long time;

	public TrackPoint(double lat, double lng, double h, float speed, float bearing, int score, long time) {
		this.lat = lat;
		this.lng = lng;
		this.h = h;
		this.score = score;
		this.time = time;
		this.speed = speed;
		this.bearing = bearing;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}
	
	

}
