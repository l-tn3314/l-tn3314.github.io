import javalib.worldimages.*;
import javalib.impworld.*;

// represents a target
class Target {
    int x;
    int y;
    Cell currentCell;
    boolean isCollected;

    // constructor
    Target(Cell c) {
        this.currentCell = c;
        this.x = c.x;
        this.y = c.y;
        this.isCollected = false;
    }

    // returns image of the target
    WorldImage targetImage() {
        return new FromFileImage("parts.png");
    }

    // makes scene for target
    WorldScene makeSceneHelper(WorldScene acc) {
        if (!this.isCollected) {
            int scale = ForbiddenIslandWorld.SCALE;
            acc.placeImageXY(this.targetImage(), 
                    (this.x * scale) + (scale / 2),
                    (this.y * scale) + (scale / 2));
        }
        return acc;
    }
    
    // returns the ticks a Player will be allowed to swim for
    int updateSwim() {
        return 0;
    }

}

// represents the helicopter
class Helicopter extends Target {

    // constructor
    Helicopter(Cell c) {
        super(c);
    }

    // returns image of the helicopter
    WorldImage targetImage() {
        return new FromFileImage("helicopter.png");
    }
}

// represents the scuba swimming suit
class Scuba extends Target {
    
    // constructor
    Scuba(Cell c) {
        super(c);
    }

    // returns image of the swimming suit
    WorldImage targetImage() {
        return new FromFileImage("scuba.png");
    }
    
    // returns the ticks a Player will be allowed to swim for
    int updateSwim() {
        return 100;
    }
}
