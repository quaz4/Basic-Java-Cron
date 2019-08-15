import java.util.*;

/**
 * The scheduler keeps track of all the jobs, and runs each one at the appropriate time. (You need
 * to fill in the details!)
 */
public class Scheduler
{
    private List<Job> jobs = new ArrayList<Job>(); // Should probs be syncronized using the extra stuff, will do in a bit
    private Object mutex = new Object();
    private Thread thread;

    public void addJob(Job newJob)
    {
        synchronized(mutex)
        {
            this.jobs.add(newJob);
        }
    }
    
    public void start()
    {
        this.thread = new Thread(() -> 
        {
            int timer = 0;
        
            try
            {
                // Loop for the lifetime of the thread
                while (true)
                {
                    synchronized(mutex)
                    {
                        // Loop through the job list
                        for (Job job : this.jobs)
                        {
                            // If timer is divisible by the delay, run the job
                            if (timer % job.delay == 0)
                            {
                                new Thread(job, job.command).start();
                            }
                        }
                    }
                    
                    timer++;
                
                    // Wait 1s
                    Thread.sleep(1000L);
                }
            }
            catch (InterruptedException e)
            {
                // Do nothing, end thread execution
            }
        }, "Sheduler");
        
        this.thread.start();
    }

    public void stop()
    {
        this.thread.interrupt();
    }
}
