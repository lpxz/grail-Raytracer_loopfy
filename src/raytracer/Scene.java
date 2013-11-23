package raytracer;

import java.util.Vector;

public class Scene implements java.io.Serializable {

    public final Vector lights;

    public final Vector objects;

    private View view;

    public Scene() {
        this.lights = new Vector();
        this.objects = new Vector();
    }

    public void addLight(Light l) {
        this.lights.addElement(l);
    }

    public void addObject(Primitive object) {
        this.objects.addElement(object);
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return this.view;
    }

    public Light getLight(int number) {
        return (Light) this.lights.elementAt(number);
    }

    public Primitive getObject(int number) {
        return (Primitive) objects.elementAt(number);
    }

    public int getLights() {
        return this.lights.size();
    }

    public int getObjects() {
        return this.objects.size();
    }

    public void setObject(Primitive object, int pos) {
        this.objects.setElementAt(object, pos);
    }
}
