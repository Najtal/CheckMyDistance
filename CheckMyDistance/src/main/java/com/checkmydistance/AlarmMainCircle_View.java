package com.checkmydistance;

import model.GUI_Model;
import util.GeoMath;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;
import bizz.Alarm;

/**
 * 
 * This View create the circle in the main TAB of the ALARM
 * in Alarm_Activity -> AlarmMain_Fragment -> this
 * 
 * @author jvdur_000
 *
 */
@SuppressLint({ "DrawAllocation", "ViewConstructor" })
public class AlarmMainCircle_View extends View {

	private int pourcentage;
	private int degreeBegin;
	private GUI_Model model;
	private int screenW;

	private static int TEXT_MKM_KM_SIZE = 100;
	private static int TEXT_MKM_M_SIZE = 80;
	private static int TEXT_M_M_SIZE = 100;
	private static int TEXT_NO_POS_SIZE = 30;
	private static int CIRCLE_WIDTH = 20;

	private static final String NO_POS = "Looking for position...";

	/**
	 * Constructor
	 * @param context the context in which the view is draw
	 * @param pourcentage the % done of the way done when view create 
	 */
	public AlarmMainCircle_View(Context context, int pourcentage) {
		super(context);

		this.pourcentage = pourcentage;
		this.model = GUI_Model.getInstance();
	}

	/**
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// retrieve data
		Alarm a = model.getAlarm();
		try {
			
			/*
			 * retrieve useful data
			 */
			double dist = a.getLastDistanceCalculated();
			double firstDist = a.getFirstKnownDistance();
			int rayonAlarme = a.getRayon();

			/*
			 * Compute data
			 */
			float orientationNorth = a.getAzimuthInDegress();
			double orientationToGo = GeoMath.GetDirection(a
					.getLastKnownPositionLat(), a.getLastKnownPositionLon(), a
					.getPosition().getLatitude(), a.getPosition()
					.getLongitude());

			canvas.drawColor(getResources().getColor(
					R.color.default_background_color));
			Paint p = new Paint();

			/*
			 * Draw cercles
			 */
			if (firstDist == 0) {
				pourcentage = 0;
				degreeBegin = 0;
			} else {
				pourcentage = (int) (360 - (dist / (firstDist / 360)));
				degreeBegin = (int) (360 / (firstDist / rayonAlarme));
			}

			int x = getWidth();
			//int y = getHeight();

			p.setAntiAlias(true);
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth(CIRCLE_WIDTH);

			// Draw full circle (background)
			p.setColor(getResources().getColor(R.color.circle_alarm_background));
			RectF rectF = new RectF(100, 130, x - 100, x - 80);

			canvas.drawOval(rectF, p);

			/*
			 * Draw the usefull circles
			 */
			if (firstDist != 0) {
				// Draw after alarm cercle
				p.setColor(getResources().getColor(R.color.circle_alarm_limit));
				canvas.drawArc(rectF, 270 - degreeBegin, degreeBegin, false, p);

				// Draw progress circle
				if (pourcentage < 0) {
					p.setColor(getResources()
							.getColor(R.color.circle_wrong_way));
				} else {
					p.setColor(getResources().getColor(R.color.circle_progress));
				}
				canvas.drawArc(rectF, 270, pourcentage, false, p);

				// Draw direction
				p.setStrokeWidth(CIRCLE_WIDTH * 4);
				
				int orientationToShow = (int) (((orientationNorth+90)*-1) + orientationToGo);//(int) ((360-myOrientation) + orientationToGo);

				p.setColor(getResources().getColor(
						R.color.position_info_colo));
				canvas.drawArc(rectF, orientationToShow-1, 2, false, p);

			}

			/*
			 * WRITE Distance
			 */
			p.setColor(Color.rgb(120, 171, 70));
			p.setStyle(Style.FILL);

			int distanceTotal = (int) model.getAlarm()
					.getLastDistanceCalculated();
			int nbrKM = Integer.parseInt(distanceTotal / 1000 + "");
			int nbrM = distanceTotal - (1000 * nbrKM);

			String distKM = nbrKM + " Km";
			String distInMeters = nbrM + " m";

			if (firstDist != 0) {

				// If KM & M
				if (nbrKM > 0) {

					// Draw KM
					p.setTextSize(TEXT_MKM_KM_SIZE);
					int kmWidth = (int) p.measureText(distKM);
					int sWkM = screenW / 2 - kmWidth / 2 - 30;
					int sHkM = (screenW - 200) / 2 + 130 - 20;
					canvas.drawText(distKM, sWkM, sHkM, p);

					// Draw Meters
					p.setTextSize(TEXT_MKM_M_SIZE);
					int mWidth = (int) p.measureText(distInMeters);
					int sWM = screenW / 2 - mWidth / 2 + 30;
					int sHM = (screenW - 200) / 2 + 130 + 80;
					canvas.drawText(distInMeters, sWM, sHM, p);

				} else {

					// Draw Meters
					p.setTextSize(TEXT_M_M_SIZE);
					int mWidth = (int) p.measureText(distInMeters);
					int sWM = screenW / 2 - mWidth / 2;
					int sHM = (screenW - 200) / 2 + 130 + 30;
					canvas.drawText(distInMeters, sWM, sHM, p);
				}

			} else {
				// Draw Meters
				p.setTextSize(TEXT_NO_POS_SIZE);
				int mWidth = (int) p.measureText(NO_POS);
				int sWM = screenW / 2 - mWidth / 2;
				int sHM = (screenW - 200) / 2 + 130 + 10;
				canvas.drawText(NO_POS, sWM, sHM, p);
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Called if screen size change
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;

		invalidate();
	}
}
