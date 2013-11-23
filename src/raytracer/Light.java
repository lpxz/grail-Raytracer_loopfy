package raytracer;

public class Light implements java.io.Serializable {

    public Vec pos;

    public double brightness;

    public Light() {
    }

    public Light(double x, double y, double z, double brightness) {
        this.pos = new Vec(x, y, z);
        this.brightness = brightness;
    }
}
