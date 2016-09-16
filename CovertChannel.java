import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Created by Chelsea on 2/23/2016.
 */
public class CovertChannel {

    public static void main(String args[]){
        //check to make sure null file arg doesn't mess it up
        long startTime = System.currentTimeMillis();
        SecureSystem system = new SecureSystem("");
        String[] bit1Instructions = {"create lyle obj",
                "write lyle obj 1", "read lyle obj", "destroy lyle obj", "run lyle"};
        String[] bit0Instructions = {"create hal obj", "create lyle obj",
        "write lyle obj 1" , "read lyle obj", "destroy lyle obj", "run lyle"};
        File covertFile = new File(args[0]);
        File outputFile = new File("log.txt");
        FileOutputStream logOutput;
        try{
            logOutput = new FileOutputStream(outputFile);
        }
        catch(FileNotFoundException e){
            System.out.println("Error creating log file.");
            return;
        }

        system.addSubject("Hal", new SecurityLevel('h'));
        system.addSubject("Lyle", new SecurityLevel('l'));
        try{
            outputFile.createNewFile();
        }
        catch(IOException e){
            System.out.println("File already exists, or directory does not exist.");
            return;
        }
        //read file bit by bit
        //write instructions for each bit to log.txt
        //runSystem with log.txt
        ByteArrayInputStream readFile;
        try {
            readFile = new ByteArrayInputStream(Files.readAllBytes(covertFile.toPath()));
        }
        catch(IOException e){
            System.out.println("Error reading bytes from file.");
            return;
        }

        //read the file to be transferred and generate instructions
        //fix this later for bytearraystream or whatever
        int numBytes = 0;
        while (readFile.available() != 0){
            System.out.println("Reading file.");
            //use bitwise ops
            int nextByte = readFile.read();
            numBytes++;

            System.out.println("Reading char: " + (char)nextByte);
            int bitmask = 0x1;
            for(int i = 0; i < 8; i++){
                int bit = (nextByte >> i) & bitmask;
                if(bit == 1){
                    //write bit1Instructions to out
                    System.out.println("Current bit = 1");
                    for(int j = 0; j < bit1Instructions.length; j++) {
                        System.out.println("Executing: " + bit1Instructions[j]);
                        system.runChannel(bit1Instructions[j]);
                        for(int k = 0; k < bit1Instructions[j].length(); k++) {
                            try {
                                logOutput.write((bit1Instructions[j]).charAt(k));
                            } catch (IOException e) {
                                System.out.println("Error writing to log.txt");
                            }
                        }
                        try {
                            logOutput.write('\n');
                        }
                        catch(IOException e){
                            System.out.println("Error writing to log.txt");
                        }

                    }
                }
                else if(bit == 0){
                    System.out.println("Current bit = 0");
                    //write bit0Instructions to out
                    for(int j = 0; j < bit0Instructions.length; j++) {
                        System.out.println("Executing: " + bit0Instructions[j]);
                        system.runChannel(bit0Instructions[j]);
                        for(int k = 0; k < bit0Instructions[j].length(); k++){
                            try {
                                logOutput.write((bit0Instructions[j]).charAt(k));
                            }
                            catch(IOException e){
                                System.out.println("Error writing to log.txt");
                            }
                        }try {
                            logOutput.write('\n');
                        }
                        catch(IOException e){
                            System.out.println("Error writing to log.txt");
                        }
                    }
                }
                else {
                    System.out.println("Problem reading file.");
                    return;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total bytes written: " + numBytes);
        System.out.println("Total time taken (s): " + (endTime - startTime)/1000);

        //now we have log.out full of the instructions we need to run the covert channel
        //all that should be left: code to create lyle and hal, lyle run code and accomodate
        //lyle keeping state (local file variable?), then debug?

    }

}
