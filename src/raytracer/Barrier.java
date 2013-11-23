package raytracer;

public abstract class Barrier {

    public Barrier(int n) {
        numThreads = n;
    }

    public abstract void DoBarrier(int myid);

    public volatile int numThreads;
}
