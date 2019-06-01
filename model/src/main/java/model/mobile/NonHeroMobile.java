package model.mobile;

import contract.IElement;
import contract.ObjectType;
import contract.Sprite;
import model.IMap;

public class NonHeroMobile extends Mobile {

	public NonHeroMobile(Sprite sprite, ObjectType objectType, final int x, final int y, final IMap map) {
		super(sprite, objectType, x, y, map);
	}

	private Boolean isFalling;

	public Boolean getIsFalling() {
		return isFalling;
	}

	public void setIsFalling(Boolean isFalling) {
		this.isFalling = isFalling;
	}

	public void moveDown() {
		this.setY(this.getY() + 1);
		this.setHasMoved();
	}

	public void moveLeft() {
		this.setX(this.getX() - 1);
		this.setHasMoved();
	}

	public void moveRight() {
		this.setX(this.getX() + 1);
		this.setHasMoved();
	}
	
	public void gravity() {
		IElement elementdown = this.getMap().getOnTheMapXY(this.getX(), this.getY() + 1);
		IElement elementdownright = this.getMap().getOnTheMapXY(this.getX() - 1, this.getY() + 1);
		IElement elementdownleft = this.getMap().getOnTheMapXY(this.getX() + 1, this.getY() + 1);
		IElement elementleft = this.getMap().getOnTheMapXY(this.getX() + 1, this.getY());
		IElement elementright = this.getMap().getOnTheMapXY(this.getX() - 1, this.getY());
		
		if (elementdown.getObjectType() == ObjectType.DIAMOND || 
				elementdown.getObjectType() == ObjectType.BOULDER || 
				elementdown.getObjectType() == ObjectType.WALL)
		{  isFalling = true;
			if (elementdownright.getObjectType() == ObjectType.BACKGROUND && 
				elementright.getObjectType() == ObjectType.BACKGROUND) {
				moveRight();
				isFalling = false;
				
			} else if (elementdownleft.getObjectType() == ObjectType.BACKGROUND && 
				elementleft.getObjectType() == ObjectType.BACKGROUND) {
				moveLeft();
				isFalling = false;
			}
		} else if(elementdown.getObjectType() == ObjectType.BACKGROUND) {
			moveDown();
		}
	}
}



