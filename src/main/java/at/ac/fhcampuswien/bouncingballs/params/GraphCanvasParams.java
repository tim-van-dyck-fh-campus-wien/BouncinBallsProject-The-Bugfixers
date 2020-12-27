package at.ac.fhcampuswien.bouncingballs.params;

public class GraphCanvasParams {
    private static int width=500;
    private static int height=400;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        GraphCanvasParams.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        GraphCanvasParams.height = height;
    }
}
