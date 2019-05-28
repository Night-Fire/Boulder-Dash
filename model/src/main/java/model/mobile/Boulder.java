package model.mobile;

import java.io.IOException;

import model.ObjectType;
import model.Sprite;

public class Boulder extends NonHeroMobile {

	private static final Sprite SPRITE = new Sprite('R', "main/images/pierre/ROCK.gif");
	
	public Boulder(Sprite sprite, ObjectType objectType, final int x, final int y) throws IOException {
		super(SPRITE, ObjectType.BOULDER);
		sprite.loadImage();
		this.setX(x);
		this.setY(y);
	}

}
