package controller;

import contract.ControllerOrder;
import contract.IController;
import contract.IElement;
import contract.IView;
import contract.ObjectType;
import fr.exia.showboard.BoardFrame;
import fr.exia.showboard.IPawn;
import model.IMap;
import model.mobile.Hero;
import model.mobile.Mobile;
import model.mobile.NonHeroMobile;
import model.motionless.MotionlessFactory;

/**
 * The Class Controller.
 */

public final class Controller implements IController {

	/** The view. */
	private IView view;

	/** The model. */
	private IMap map;

	/** The BoardFrame */
	private BoardFrame boardFrame;

	private ControllerOrder order = ControllerOrder.NONE;

	/**
	 * Instantiates a new controller.
	 *
	 * @param view  the view
	 * @param model the model
	 */
	public Controller(final IView view, final IMap map) {
		this.setView(view);
		this.setBoardFrame(view.getBoardFrame());
		this.setMap(map);
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		while (this.getMap().getHero().isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Mobile[][] pawns = new Mobile[this.getMap().getWidth()][this.getMap().getHeigth()];
			for (IPawn pawn : this.getBoardFrame().getPawns()) {
				if (pawn instanceof NonHeroMobile) {
					pawns[pawn.getX()][pawn.getY()] = (NonHeroMobile) pawn;
				}
				if (pawn instanceof Hero) {
					pawns[pawn.getX()][pawn.getY()] = (Hero) pawn;
				}
			}
			this.getMap().getHero().changeSprite(this.getOrder());
			this.moveHero(pawns);
			for (IPawn pawn : this.getBoardFrame().getPawns()) {
				if (pawn instanceof NonHeroMobile) {
					this.gravity((NonHeroMobile) pawn, pawns);
				}
			}
		}
		System.out.println("crushed");
	}

	private void gravity(NonHeroMobile element, Mobile[][] pawns) {
		IElement caseDown = this.getMap().getOnTheMapXY(element.getX(), element.getY() + 1);
		IElement caseDownRight = this.getMap().getOnTheMapXY(element.getX() + 1, element.getY() + 1);
		IElement caseDownLeft = this.getMap().getOnTheMapXY(element.getX() - 1, element.getY() + 1);
		IElement caseLeft = this.getMap().getOnTheMapXY(element.getX() - 1, element.getY());
		IElement caseRight = this.getMap().getOnTheMapXY(element.getX() + 1, element.getY());

		if (caseDown.getObjectType() == ObjectType.BACKGROUND) {
			Mobile pawnDown = pawns[element.getX()][element.getY() + 1];
			if (pawnDown == null) {
				element.moveDown();
				pawns[element.getX()][element.getY()] = null;
				pawns[element.getX()][element.getY() + 1] = element;
				return;
			} else if (element.getIsFalling() && pawnDown instanceof Hero) {
				element.moveDown();
				pawns[element.getX()][element.getY()] = null;
				pawns[element.getX()][element.getY() + 1] = element;
				this.getMap().getHero().setAlive(false);
				return;
			} else if (pawnDown.getObjectType() == ObjectType.DIAMOND || pawnDown.getObjectType() == ObjectType.BOULDER
					|| pawnDown.getObjectType() == ObjectType.WALL) {
				Mobile pawnLeft = pawns[element.getX() - 1][element.getY()];
				Mobile pawnDownLeft = pawns[element.getX() - 1][element.getY() + 1];
				Mobile pawnRight = pawns[element.getX() + 1][element.getY()];
				Mobile pawnDownRight = pawns[element.getX() + 1][element.getY() + 1];
				if (caseDownLeft.getObjectType() == ObjectType.BACKGROUND
						&& caseLeft.getObjectType() == ObjectType.BACKGROUND && pawnLeft == null
						&& pawnDownLeft == null) {
					element.moveLeft();
					element.setIsFalling(true);
					pawns[element.getX()][element.getY()] = null;
					pawns[element.getX() - 1][element.getY()] = element;
					return;
				} else if (caseDownRight.getObjectType() == ObjectType.BACKGROUND
						&& caseRight.getObjectType() == ObjectType.BACKGROUND && pawnRight == null
						&& pawnDownRight == null) {
					element.moveRight();
					element.setIsFalling(true);
					pawns[element.getX()][element.getY()] = null;
					pawns[element.getX() + 1][element.getY()] = element;
					return;
				}
			}
		}
		element.setIsFalling(false);
	}

	/**
	 * Sets the view.
	 *
	 * @param pview the new view
	 */
	private void setView(final IView view) {
		this.view = view;
	}

	/**
	 * Return the view.
	 *
	 * @param pview the new view
	 */
	public IView getView() {
		return this.view;
	}

	/**
	 * Sets the map.
	 *
	 * @param model the new model
	 */
	private void setMap(final IMap map) {
		this.map = map;
	}

	/**
	 * Return the map.
	 *
	 * @param pview the new view
	 */
	private IMap getMap() {
		return this.map;
	}

	public IController getOrderPerformer() {
		return this;
	}

	public ControllerOrder getOrder() {
		return order;
	}

	public void setOrder(ControllerOrder order) {
		this.order = order;
	}

	public BoardFrame getBoardFrame() {
		return boardFrame;
	}

	public void setBoardFrame(BoardFrame boardFrame) {
		this.boardFrame = boardFrame;
	}

	public void moveHero(Mobile[][] pawns) {
		IElement caseUp = this.getMap().getOnTheMapXY(this.getMap().getHero().getX(),
				this.getMap().getHero().getY() - 1);
		IElement caseLeft = this.getMap().getOnTheMapXY(this.getMap().getHero().getX() - 1,
				this.getMap().getHero().getY());
		IElement caseRight = this.getMap().getOnTheMapXY(this.getMap().getHero().getX() + 1,
				this.getMap().getHero().getY());
		IElement caseDown = this.getMap().getOnTheMapXY(this.getMap().getHero().getX(),
				this.getMap().getHero().getY() + 1);
		switch (this.getOrder()) {
		case RIGHT:
			if (caseRight.getObjectType() == ObjectType.DIRT) {
				this.getMap().setOnTheMapXY(MotionlessFactory.createBackground(), this.getMap().getHero().getX() + 1,
						this.getMap().getHero().getY());
				this.getBoardFrame().addSquare(MotionlessFactory.createBackground(), this.getMap().getHero().getX() + 1,
						this.getMap().getHero().getY());
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveRight();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				return;
			} else if (caseRight.getObjectType() == ObjectType.BACKGROUND) {
				Mobile pawnRight = pawns[this.getMap().getHero().getX() + 1][this.getMap().getHero().getY()];
				if (pawnRight == null) {
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveRight();
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				} else if (pawnRight.getObjectType() == ObjectType.DIAMOND) {
					this.collectDiamond(pawnRight, pawns);
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveRight();
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				} else if (pawnRight.getObjectType() == ObjectType.BOULDER) {
					IElement caseBehindBoulder = this.getMap().getOnTheMapXY(this.getMap().getHero().getX() + 2,
							this.getMap().getHero().getY());
					Mobile pawnBehindBoulder = pawns[this.getMap().getHero().getX() + 2][this.getMap().getHero()
							.getY()];
					if (pawnBehindBoulder == null && caseBehindBoulder.getObjectType() == ObjectType.BACKGROUND) {
						pawns[pawnRight.getX()][pawnRight.getY()] = null;
						((NonHeroMobile) pawnRight).moveRight();
						pawns[pawnRight.getX()][pawnRight.getY()] = pawnRight;
						pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
						this.getMap().getHero().moveRight();
						pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
					}
				}
			} else if (caseRight.getObjectType() == ObjectType.EXIT_OPEN) {
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveRight();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
			}
			break;
		case LEFT:
			if (caseLeft.getObjectType() == ObjectType.DIRT) {
				this.getMap().setOnTheMapXY(MotionlessFactory.createBackground(), this.getMap().getHero().getX() - 1,
						this.getMap().getHero().getY());
				this.getBoardFrame().addSquare(MotionlessFactory.createBackground(), this.getMap().getHero().getX() - 1,
						this.getMap().getHero().getY());
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveLeft();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				return;
			} else if (caseLeft.getObjectType() == ObjectType.BACKGROUND) {
				Mobile pawnLeft = pawns[this.getMap().getHero().getX() - 1][this.getMap().getHero().getY()];
				if (pawnLeft == null) {
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveLeft();
				} else if (pawnLeft.getObjectType() == ObjectType.DIAMOND) {
					this.collectDiamond(pawnLeft, pawns);
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveLeft();
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				} else if (pawnLeft.getObjectType() == ObjectType.BOULDER) {
					IElement caseBehindBoulder = this.getMap().getOnTheMapXY(this.getMap().getHero().getX() - 2,
							this.getMap().getHero().getY());
					Mobile pawnBehindBoulder = pawns[this.getMap().getHero().getX() - 2][this.getMap().getHero()
							.getY()];
					if (pawnBehindBoulder == null && caseBehindBoulder.getObjectType() == ObjectType.BACKGROUND) {
						pawns[pawnLeft.getX()][pawnLeft.getY()] = null;
						((NonHeroMobile) pawnLeft).moveLeft();
						pawns[pawnLeft.getX()][pawnLeft.getY()] = pawnLeft;
						pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
						this.getMap().getHero().moveLeft();
						pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
					}
				}
			} else if (caseLeft.getObjectType() == ObjectType.EXIT_OPEN) {
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveLeft();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
			}
			break;
		case DOWN:
			if (caseDown.getObjectType() == ObjectType.DIRT) {
				this.getMap().setOnTheMapXY(MotionlessFactory.createBackground(), this.getMap().getHero().getX(),
						this.getMap().getHero().getY() + 1);
				this.getBoardFrame().addSquare(MotionlessFactory.createBackground(), this.getMap().getHero().getX(),
						this.getMap().getHero().getY() + 1);
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveDown();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				return;
			} else if (caseDown.getObjectType() == ObjectType.BACKGROUND) {
				Mobile pawnDown = pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY() + 1];
				if (pawnDown == null) {
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveDown();
				} else if (pawnDown.getObjectType() == ObjectType.DIAMOND) {
					this.collectDiamond(pawnDown, pawns);
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveDown();
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				}
			} else if (caseDown.getObjectType() == ObjectType.EXIT_OPEN) {
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveDown();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
			}
			break;
		case UP:
			if (caseUp.getObjectType() == ObjectType.DIRT) {
				this.getMap().setOnTheMapXY(MotionlessFactory.createBackground(), this.getMap().getHero().getX(),
						this.getMap().getHero().getY() - 1);
				this.getBoardFrame().addSquare(MotionlessFactory.createBackground(), this.getMap().getHero().getX(),
						this.getMap().getHero().getY() - 1);
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveUp();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				return;
			} else if (caseUp.getObjectType() == ObjectType.BACKGROUND) {
				Mobile pawnUp = pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY() - 1];
				if (pawnUp == null) {
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveUp();
				} else if (pawnUp.getObjectType() == ObjectType.DIAMOND) {
					this.collectDiamond(pawnUp, pawns);
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
					this.getMap().getHero().moveUp();
					pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
				}
			} else if (caseUp.getObjectType() == ObjectType.EXIT_OPEN) {
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = null;
				this.getMap().getHero().moveUp();
				pawns[this.getMap().getHero().getX()][this.getMap().getHero().getY()] = (Mobile) this.getMap().getHero();
			}
			break;
		default:
			break;
		}
	}

	private void collectDiamond(Mobile diamond, IPawn[][] pawns) {
		this.getBoardFrame().removePawn(diamond);
		pawns[diamond.getX()][diamond.getY()] = null;
		this.getBoardFrame().getCountDiamond().collectDiamond();
		if (this.getBoardFrame().getCountDiamond().getNbrDiamond() == 0) {
			this.getMap().getExit().open();
		}
	}
}
