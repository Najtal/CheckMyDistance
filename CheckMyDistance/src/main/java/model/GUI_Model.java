package model;

import java.util.List;

import bizz.Alarm;
import bizz.PositionChooser;

/**
 * This class is the MODEL of our application It will save and handle loads of
 * data and references to various objects that will be used in most class of the
 * application
 * 
 * @author jvdur_000
 * 
 */
public class GUI_Model {

	// SSO instance
	private static GUI_Model instance;

	/*
	 * CUD function
	 */
	private int posIdToEdit;
	private double posLongitude;
	private double posLatitude;
	private int modeNewPosition; // 1 = actual position - 2 = set position

	/*
	 * Distance between 2 positions function
	 */
	private List<PositionChooser> listOfElements;

	/*
	 * Alarm function
	 */
	private Alarm alarm;

	/**
	 * Constructor (private)
	 */
	private GUI_Model() {
		this.posIdToEdit = 1;
	}

	/**
	 * Allow to get the SSO instance
	 * @return the SSO instance of Model
	 */
	public static GUI_Model getInstance() {
		if (instance == null) {
			instance = new GUI_Model();
		}
		return instance;
	}


	/*
	 * GETTERS & SETTERS
	 */
	public void setCUDPosValue(int position) {
		this.posIdToEdit = position;
	}

	public int getCUPPosValue() {
		return posIdToEdit;
	}

	public List<PositionChooser> getListOfElements() {
		return listOfElements;
	}

	public void setListOfElements(List<PositionChooser> listOfElements) {
		this.listOfElements = listOfElements;
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public double getPosLongitude() {
		return posLongitude;
	}

	public void setPosLongitude(double posLongitude) {
		this.posLongitude = posLongitude;
	}

	public double getPosLatitude() {
		return posLatitude;
	}

	public void setPosLatitude(double posLatitude) {
		this.posLatitude = posLatitude;
	}

	public void setModeNewPosition(int modeNewPosition) {
		this.modeNewPosition = modeNewPosition;
	}

	public int getModeNewPosition() {
		return modeNewPosition;
	}
}
