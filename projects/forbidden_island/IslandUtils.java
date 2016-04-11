// represents utils for ForbiddenIsland
class IslandUtils {

    // returns manhattan distance from center
    int manhattan(int x, int y) {
        int isleHeight = ForbiddenIslandWorld.ISLAND_SIZE / 2;
        int dist = Math.abs(isleHeight - x) + Math.abs(isleHeight - y);
        return dist;
    }
}

