import java.awt.Color;
import javalib.worldimages.*;
import javalib.impworld.*;

// represents the player
class Player {
    int x;
    int y;
    Cell currentCell;
    WorldImage image;
    int stepsTaken;
    int scoreDisplay;
    int swimTix;
    boolean isScubaOn;

    // constructor
    Player(Cell currentCell) {
        this.x = currentCell.x;
        this.y = currentCell.y;
        this.currentCell = currentCell;
        this.image = new FromFileImage("p1.png");
        this.stepsTaken = 0;
        this.scoreDisplay = 6;
        this.swimTix = 0;
	this.isScubaOn = false;
    }

    // convenience constructor
    Player(Cell currentCell, WorldImage img, int scoreDisplay) {
        this(currentCell);
        this.image = img;
        this.scoreDisplay = scoreDisplay;
    }

    // EFFECT: updates currentCell
    void updateCell(Cell c) {
        if (!c.isFlooded || (this.isScubaOn && swimTix > 0)) {
            this.currentCell = c;
            this.x = c.x;
            this.y = c.y;
            this.stepsTaken += 1;
        }
    }

    // EFFECT: alters player's position to the left cell
    void updateToLeft() {
        this.updateCell(this.currentCell.left);
    }

    // EFFECT: alters player's position to the top cell
    void updateToTop() {
        this.updateCell(this.currentCell.top);
    }

    // EFFECT: alters player's position to the right cell
    void updateToRight() {
        this.updateCell(this.currentCell.right);
    }

    // EFFECT: alters player's position to the bottom cell
    void updateToBottom() {
        this.updateCell(this.currentCell.bottom);
    }

    // is the player drowning?
    boolean isDrowning() {
        return this.currentCell.isFlooded;
    }

    // returns list of remaining targets
    // EFFECT: updates list of targets if picked up
    IList<Target> checkFor(IList<Target> targets) {
        IList<Target> temp = new MtList<Target>();
        for (Target t : targets) {
            if (this.x == t.x && this.y == t.y) {
                this.swimTix += t.updateSwim();
                t.isCollected = true;
            }
            else {
                temp = new ConsList<Target>(t, temp);
            }
        }
        return temp;
    }

    // returns image of the player
    WorldImage playerImage() {
        return this.image;
    }

    // EFFECT: changes the player image
    void updateImage(FromFileImage image) {
        this.image = image;
    }

    // makes scene for player
    WorldScene makeSceneHelper(WorldScene acc) {
        int scale = ForbiddenIslandWorld.SCALE;
        TextImage score = new TextImage("SCORE: " + Integer.toString(this.stepsTaken), 20, Color.WHITE);
        acc.placeImageXY(this.playerImage(), 
			 ((this.x * scale) + (scale / 2)),
                ((this.y * scale) + (scale / 2)));
        acc.placeImageXY(score, scoreDisplay * scale, 2 * scale);
	if (this.isScubaOn && this.swimTix > 0) {
	    TextImage scubaTime = new TextImage("Scuba time: " + Integer.toString(this.swimTix), 20, Color.WHITE);
	    acc.placeImageXY(scubaTime, 7 * scale, (ForbiddenIslandWorld.ISLAND_SIZE - 2) * scale);
	    this.swimTix -= 1;
	}
	if (this.swimTix == 0) {
	    this.isScubaOn = false;
	}
	return acc;
    }
	
    // EFFECT: turns scuba on if applicable
    void scubaOn() {
	if (!isScubaOn && swimTix > 0) {
	    this.isScubaOn = true;
	}
    }
}
