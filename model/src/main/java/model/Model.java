package model;

import java.util.Observable;

import contract.IModel;
import model.mobile.IHero;

/**
 * The Class Model.
 *
 * @author [enter name]
 */
public class Model extends Observable implements IModel {

	private IHero hero;
	public static int width;
	public static int heigth;
	public static int STYLE = 1;

	public Model() {

	}

	public IHero getHero() {
		return this.hero;
	}

	public void setHero(IHero hero) {
		this.hero = hero;
	}

	public IMap getMap() {
		return null;

	}

	public void setMap(IMap Map) {

	}

	@Override
	public Observable getObservable() {
		// TODO Auto-generated method stub
		return null;
	}

}
