import java.io.*;
import java.util.*;
import java.lang.ProcessBuilder.Redirect;

public class TestProcessBuilder {
    // List of strings, that contains all commands, 
    // that caused an error in this session
    static List <String> ErrorCommands = new ArrayList();

    static void createProcess(String command) throws java.io.IOException {

        List<String> input = Arrays.asList(command.split(" "));

        ProcessBuilder processBuilder = new ProcessBuilder(input);
        BufferedReader bufferReader = null;
        try {
            // Create "log" file if not exists, else - open it
            File log = new File("log");
            // Redirect all error messages
            processBuilder.redirectErrorStream(true);
            // Redirect error messages into the "log" file
            processBuilder.redirectError(Redirect.appendTo(log));
            Process proc = processBuilder.start();
            InputStream inputStream = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(isr);

            String line;
            while ((line = bufferReader.readLine()) != null) {
                System.out.println(line );
            }
            bufferReader.close();
        } catch (java.io.IOException ioe) {
            System.err.println("Error: " + ioe.getMessage());
            ErrorCommands.add(command);
        } finally {
            if (bufferReader != null) {
                bufferReader.close();
            }
        }
    }

    public static void main(String[] args) throws java.io.IOException {
        String commandLine;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n***** Welcome to the Java Command Shell *****");
        System.out.println("If you want to exit the shell, type END and press RETURN.\n");
    
        while (true) {
               System.out.print("jsh>");
            commandLine = scanner.nextLine();
            // if user entered a return, just loop again
            if (commandLine.equals("")) {
                continue;
            }
            if (commandLine.toLowerCase().equals("showerrlog")) { //User wants to see error command log
                System.out.println("\n***** Error Command Log *****");
                // Iterate through the list string by string
                for (String command: ErrorCommands) {
                    System.out.println("$" + command);
                }
                System.out.println();
                continue;
            }
            if (commandLine.toLowerCase().equals("end")) { //User wants to end shell
                System.out.println("\n***** Command Shell Terminated. See you next time. BYE for now. *****\n");
                scanner.close();
                System.exit(0);
            }
            createProcess(commandLine);
        }   
    }
   
}
