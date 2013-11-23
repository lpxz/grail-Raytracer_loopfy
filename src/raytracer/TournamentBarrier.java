package raytracer;

public class TournamentBarrier extends Barrier {

    public TournamentBarrier(int n) {
        super(n);
        IsDone = new boolean[numThreads];
        edu.hkust.clap.monitor.Monitor.loopBegin(4);
for (int i = 0; i < n; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(4);
{
            IsDone[i] = false;
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(4);

    }

    public void debug(String s) {
    }

    public void setMaxBusyIter(int b) {
        maxBusyIter = b;
    }

    public void DoBarrier(int myid) {
        int b;
        int roundmask = 3;
        boolean donevalue = !IsDone[myid];
        edu.hkust.clap.monitor.Monitor.loopBegin(5);
while (((myid & roundmask) == 0) && (roundmask < (numThreads << 2))) { 
edu.hkust.clap.monitor.Monitor.loopInc(5);
{
            int spacing = (roundmask + 1) >> 2;
            edu.hkust.clap.monitor.Monitor.loopBegin(6);
for (int i = 1; i <= 3 && myid + i * spacing < numThreads; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(6);
{
                b = maxBusyIter;
                edu.hkust.clap.monitor.Monitor.loopBegin(7);
while (IsDone[myid + i * spacing] != donevalue) { 
edu.hkust.clap.monitor.Monitor.loopInc(7);
{
                    b--;
                    if (b == 0) {
                        Thread.yield();
                        b = maxBusyIter;
                    }
                }} 
edu.hkust.clap.monitor.Monitor.loopEnd(7);

            }} 
edu.hkust.clap.monitor.Monitor.loopEnd(6);

            roundmask = (roundmask << 2) + 3;
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(5);

        IsDone[myid] = donevalue;
        b = maxBusyIter;
        edu.hkust.clap.monitor.Monitor.loopBegin(8);
while (IsDone[0] != donevalue) { 
edu.hkust.clap.monitor.Monitor.loopInc(8);
{
            b--;
            if (b == 0) {
                Thread.yield();
                b = maxBusyIter;
            }
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(8);

    }

    volatile boolean[] IsDone;

    public int maxBusyIter = 1;
}
