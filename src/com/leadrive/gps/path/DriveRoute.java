package com.leadrive.gps.path;

import static com.leadrive.gps.util.RamerDouglasPeuckerFilter.simplify;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


import com.leadrive.gps.util.TrackPoint;

public class DriveRoute {
	private static final double EPSILON = 20;
	private static final String STOP = "STOP";
	private static final String START = "START";
	private List<TrackPoint> path = new ArrayList<TrackPoint>();
	private LinkedHashMap<Long, String> rec = new LinkedHashMap<Long, String>();
	private float distance = 0;
	private float maxSpd = 0;
	private float avgSpd = 0;
	private long tripDuration = 0;
	private long drivingDuration = 0;
	private long idelDuration = 0;
	private boolean isDriving = false;
	private boolean isEnd = false;
	private Long startTime;
	private Long endTime;
	private int sumScore = 0;


	public TrackPoint addPoint(double lat, double lng, double alt, float speed,
			float bearing, int score, long time) {
		TrackPoint point = new TrackPoint(lat, lng, alt, speed, bearing, score,
				time);
		init(point, speed, score);
		return point;
	}

	private void init(TrackPoint point, float speed, int s) {
		path.add(point);
		if (startTime == null) {
			startTime = System.currentTimeMillis();
		}
		if (speed > maxSpd) {
			maxSpd = speed;
		}

		if (speed > 0) {
			if (!isDriving) {
				isDriving = true;
				rec.put(System.currentTimeMillis(), START);
			}
		} else {
			if (isDriving) {
				isDriving = false;
				rec.put(System.currentTimeMillis(), STOP);
			}
		}
		sumScore += s;
	}

	public TrackPoint[] getRdpPath() {
		return simplify(path.toArray(new TrackPoint[0]), EPSILON);
	}

	public double getDistance() {
		return distance;
	}

	public void addDistance(double distance) {
		distance += distance;
	}

	public float getAvgSpd() {
		if (isEnd) {
			if (endTime != null && startTime != null) {
				float t = ((float) (endTime - startTime) / 1000f);
				avgSpd = distance / t;
			}

		} else {
			if (startTime != null) {
				float t = ((float) (System.currentTimeMillis() - startTime) / 1000f);
				avgSpd = distance / t;
			}
		}
		return avgSpd;
	}

	public long getTripDuration() {
		if (isEnd) {
			if (endTime != null && startTime != null) {
				tripDuration = endTime - startTime;
			}

		} else {
			if (startTime != null) {
				tripDuration = System.currentTimeMillis() - startTime;
			}
		}

		return tripDuration;
	}

	public float getMaxSpeed() {
		return maxSpd;
	}

	public void endTrip() {
		isEnd = true;
		Set<Long> set = rec.keySet();
		String mode = null;
		endTime = System.currentTimeMillis();
		for (Long key : set) {
			mode = rec.get(key);
		}
		if (mode != null && mode.equals(START)) {
			rec.put(endTime, STOP);
		}
	}

	public void calDrivingDur() {
		Set<Long> set = rec.keySet();
		List<Long> start = new ArrayList<Long>();
		List<Long> stop = new ArrayList<Long>();
		for (Long key : set) {
			if (rec.get(key).equals(START)) {
				start.add(key);
			} else {
				stop.add(key);
			}
		}
		if (start.size() == stop.size()) {
			for (int i = 0; i < start.size(); i++) {
				if (stop.get(i) > start.get(i)) {
					drivingDuration += stop.get(i) - start.get(i);
				}
				if (i + 1 < start.size()) {
					if (start.get(i + 1) > stop.get(i)) {
						idelDuration += start.get(i + 1) - stop.get(i);
					}
				}
			}
		} else {
			for (int i = 0; i < start.size() - 1; i++) {
				if (stop.get(i) > start.get(i)) {
					drivingDuration += stop.get(i) - start.get(i);
				}
				if (i + 1 < start.size() - 1) {
					if (start.get(i + 1) > stop.get(i)) {
						idelDuration += start.get(i + 1) - stop.get(i);
					}
				}
			}
			drivingDuration += System.currentTimeMillis()
					- stop.get(stop.size() - 1);
		}
	}

	public long getDrivingDuration() {
		calDrivingDur();
		return drivingDuration;
	}

	public long getIdelDuration() {
		calDrivingDur();
		return idelDuration;
	}

	public int getScore() {
		if (path.size() > 0) {
			return sumScore / path.size();
		}
		return 0;
	}

}
