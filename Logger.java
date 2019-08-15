import java.io.*;

/**
 * The logger is in charge of writing output to 'cron.log'. It does this in its own thread, but 
 * assumes that other threads will call the logMessage() in order to provide messages to log. (You 
 * need to fill in the details!)
 */
public class Logger
{
    private Thread thread;
    private Object monitor = new Object();
    private String nextMessage = null;
    
    public void logMessage(String newMessage) throws InterruptedException
    {   
        synchronized(this.monitor)
        {
            // While waiting for newMessage to be consumed, wait
            while (this.nextMessage != null)
            {
                this.monitor.wait();
            }
            
            this.nextMessage = newMessage;
            this.monitor.notifyAll();
        }
    }
    
    public void start()
    {
        this.thread = new Thread(() -> 
        {
            try(PrintWriter writer = new PrintWriter(new FileWriter("cron.log", true)))
            {
                synchronized(this.monitor)
                {
                    while(true) 
                    {
                        // While waiting for newMessage to be set
                        while (this.nextMessage == null)
                        {
                            this.monitor.wait();
                        }
                        
                        writer.println(this.nextMessage);
                        this.nextMessage = null;
                        this.monitor.notifyAll();
                    }
                }
            }
            catch (InterruptedException e)
            {
                // Do nothing, let the thread end
            }
            catch (IOException e)
            {
                // Do nothing, let the thread end
            }
        }, "Logger");

        this.thread.start();
    }
    
    public void stop()
    {
        this.thread.interrupt();
    }
}
