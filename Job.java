import java.io.*;

/**
 * Represents a job (command) to be run, and performs the execution. (You need to fill in the 
 * details!)
 */
public class Job implements Runnable
{
    public String command;
    public int delay;
    private Logger logger;

    public Job(String command, int delay, Logger logger)
    {
        this.command = command;
        this.delay = delay;
        this.logger = logger;
    }
    
    
    @Override
    public void run()
    {
        try
        {
            // Assume 'command' is a string containing the command the
            // execute. Then we initially run it as follows:
            Process proc = Runtime.getRuntime().exec(command);
            
            // Arrange to capture the command's output, line by line.
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
                
            String line = reader.readLine();
            
            while(line != null)
            {
                output.append(line);
                output.append('\n');
                line = reader.readLine();
            }
            
            // We've now reached the end of the command's output, which
            // generally means the command has finished.
           this.logger.logMessage(command + ": " + output.toString());
        }
        catch (InterruptedException e)
        {
            // Let the thread end
        }
        catch (IOException e)
        {
            System.out.println("There was an IOException :(");
        }
    }
}
