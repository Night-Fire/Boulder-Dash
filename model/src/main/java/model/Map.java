package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import contract.IElement;
import contract.IHero;
import contract.IMobile;
import model.mobile.Hero;
import model.mobile.Mobile;
import model.motionless.MotionlessElement;
import model.motionless.MotionlessFactory;

/**
 * The Class Map.
 *
 * @author [enter name]
 */

public class Map extends Observable implements IMap {

	public static final int width = 6;
	public static final int heigth = 4;
	public static final int STYLE = 1;
	private IElement[][] onMap = new IElement[width][heigth];
	private List<IMobile> boulAndDia = new ArrayList<IMobile>();
	private IHero hero;

	public Map(final String content) {
		this.buildMap(content);
	}

	@Override
	public void setMobileHasChanged() {
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public Observable getObservable() {
		return this;
	}

	private void buildMap(String content) {
		List<String> rows = Arrays.asList(content.split(";"));
		int x = 0;
		int y = 0;
		for (String row : rows) {
			for (char symbol : row.toCharArray()) {
				Element element = AbstractFactory.getFactory(symbol).getFromFileSymbole(symbol, x, y, this.getMap());
				if (element instanceof MotionlessElement) {
					this.setOnTheMapXY(element, x, y);
				} else if (element instanceof Mobile) {
					this.setOnTheMapXY(MotionlessFactory.createBackground(), x, y);
					if (element instanceof Hero) {
						this.setHero((Hero) element);
					} else {
						boulAndDia.add((IMobile) element);
					}
				}
				x = x + 1;
			}
			y = y + 1;
			x = 0;
		}
	}

	@Override
	public IElement getOnTheMapXY(int x, int y) {
		return onMap[x][y];
	}

	private void setOnTheMapXY(final IElement element, final int x, final int y) {
		this.onMap[x][y] = element;
	}

	public IElement[][] getOnMap() {
		return onMap;
	}

	public List<IMobile> getBoulAndDia() {
		return boulAndDia;
	}

	public IMap getMap() {
		return this;
	}

	public IHero getHero() {
		return hero;
	}

	public void setHero(IHero hero) {
		this.hero = hero;
	}
}
