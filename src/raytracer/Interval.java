package raytracer;

public class Interval implements java.io.Serializable {

    public final int number;

    public final int width;

    public final int height;

    public final int yfrom;

    public final int yto;

    public final int total;

    public final int threadid;

    public Interval(int number, int width, int height, int yfrom, int yto, int total, int threadid) {
        this.number = number;
        this.width = width;
        this.height = height;
        this.yfrom = yfrom;
        this.yto = yto;
        this.total = total;
        this.threadid = threadid;
    }
}
