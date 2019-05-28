package model.mobile;

import java.io.IOException;

import contract.ObjectType;
import contract.Sprite;

public class Boulder extends NonHeroMobile {

	private static final Sprite SPRITE = new Sprite('R', "main/images/pierre/ROCK.gif");
	
	public Boulder(Sprite sprite, ObjectType objectType) throws IOException {
		super(SPRITE, ObjectType.BOULDER);
		sprite.loadImage();
	}

}
