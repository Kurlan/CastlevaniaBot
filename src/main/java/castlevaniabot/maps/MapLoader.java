package castlevaniabot.maps;

import castlevaniabot.model.gameelements.MapElement;
import castlevaniabot.model.gameelements.MapRoutes;

import java.io.DataInputStream;

public class MapLoader {
    public MapRoutes loadMap(final String name, final DataInputStream in)
            throws Exception {

        final int WIDTH = in.readInt();
        final int HEIGHT = in.readInt();

        final MapElement[][] map = new MapElement[HEIGHT][WIDTH];
        for(int y = HEIGHT - 1; y >= 0; --y) {
            for(int x = WIDTH - 1; x >= 0; --x) {
                map[y][x] = new MapElement(in.read());
            }
        }

        final int ROUTES = in.readInt();
        final int[][][][] routes = new int[HEIGHT][WIDTH][HEIGHT][WIDTH];
        for(int startY = HEIGHT - 1; startY >= 0; --startY) {
            for(int startX = WIDTH - 1; startX >= 0; --startX) {
                for(int endY = HEIGHT - 1; endY >= 0; --endY) {
                    for(int endX = WIDTH - 1; endX >= 0; --endX) {
                        routes[startY][startX][endY][endX] = (endX << 8) | (endY << 4);
                        if (startX != endX || startY != endY) {
                            routes[startY][startX][endY][endX] |= 0xFFFF0000;
                        }
                    }
                }
            }
        }
        for(int i = ROUTES - 1; i >= 0; --i) {
            final int startX = in.read();
            final int endX = in.read();
            final int endYstartY = in.read();
            final int startY = endYstartY & 0x0F;
            final int endY = (endYstartY >> 4) & 0x0F;
            routes[startY][startX][endY][endX] = in.readInt();
        }
        return new MapRoutes(name, WIDTH, HEIGHT, map, routes);
    }
}
