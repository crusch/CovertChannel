
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Chelsea on 2/6/2016.
 */


public class SecureSystem {

    private static final int READ = 0;
    private static final int WRITE = 1;
    private static final int CREATE = 2;
    private static final int DESTROY = 3;
    private static final int RUN = 4;
    private static final int BADINSTRUCTION = 27;

    private static ReferenceMonitor monitor;
    private static File instructionFile;
    private static Scanner readFile;
    private static InstructionObject currentInstruction;

    public SecureSystem(String filepath){
        newFile(filepath);
        monitor = new ReferenceMonitor();
    }

    public void runSystem(){
        while (readFile.hasNextLine()){
            nextInstruction();
            //printState();
        }
    }

    public void runChannel(String instructionString){
        nextInstruction(instructionString);
        //printState();
    }

    public static void printState(){
        int op = currentInstruction.operation;
        if (op == WRITE){
           System.out.println(currentInstruction.subjectName + " writes value " + currentInstruction.writeValue +
               " to " + currentInstruction.objectName);
        }
        if (op == READ){
           System.out.println(currentInstruction.subjectName + " reads " + currentInstruction.objectName);
        }
        System.out.println("The current state is:");
        printObjects();
        printSubjects();
        System.out.println();
    }

    private static void printObjects(){
        monitor.printObjects();
    }

    private static void printSubjects(){
        monitor.printSubjects();
    }

    private static void newFile(String filepath){
        if(filepath == "")
            return;
        instructionFile = new File(filepath);
        try {
            readFile = new Scanner(instructionFile); //???
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found! Program will now crash. Try again with a different file.");
        }
    }

    //need to modify to allow new instructions
    private void nextInstruction(){
        //read instruction, then execute
        //read instruction code here
        currentInstruction = new InstructionObject(readFile.nextLine());

        //execute
        if(currentInstruction.operation == READ){
            monitor.readObject(currentInstruction.objectName, currentInstruction.subjectName);
        }
        else if(currentInstruction.operation == WRITE){
            monitor.writeObject(currentInstruction.objectName, currentInstruction.subjectName, currentInstruction.writeValue);
        }
        else if(currentInstruction.operation == CREATE){
            //currentInstruction.objectName, security level of currentInst.subjectname
            if(!monitor.objectExists(currentInstruction.objectName))
               addObject(currentInstruction.objectName, 0, (monitor.getSubjectLevel(currentInstruction.subjectName)));

        }
        else if(currentInstruction.operation == DESTROY){
            if(monitor.canWrite(currentInstruction.subjectName, currentInstruction.objectName)){
                //destroy object
                monitor.destroy(currentInstruction.objectName);
            }
        }
        else if(currentInstruction.operation == RUN){
            monitor.runSubject(currentInstruction.subjectName);

        }
        else{
            assert currentInstruction.operation == BADINSTRUCTION;
            System.out.println("Bad instruction");
        }
    }

    private void nextInstruction(String instruction){
        //read instruction, then execute
        //read instruction code here
        currentInstruction = new InstructionObject(instruction);

        //execute
        if(currentInstruction.operation == READ){
            monitor.readObject(currentInstruction.objectName, currentInstruction.subjectName);
        }
        else if(currentInstruction.operation == WRITE){
            monitor.writeObject(currentInstruction.objectName, currentInstruction.subjectName, currentInstruction.writeValue);
        }
        else if(currentInstruction.operation == CREATE){
            //currentInstruction.objectName, security level of currentInst.subjectname
            if(!monitor.objectExists(currentInstruction.objectName))
                addObject(currentInstruction.objectName, 0, (monitor.getSubjectLevel(currentInstruction.subjectName)));

        }
        else if(currentInstruction.operation == DESTROY){
            if(monitor.canWrite(currentInstruction.subjectName, currentInstruction.objectName)){
                //destroy object
                monitor.destroy(currentInstruction.objectName);
            }
        }
        else if(currentInstruction.operation == RUN){
            monitor.runSubject(currentInstruction.subjectName);
        }
        else{
            assert currentInstruction.operation == BADINSTRUCTION;
            System.out.println("Bad instruction");
        }
    }


    //debug methods for creating subjects and objects
    public static void addSubject(String name, SecurityLevel level){

        monitor.addSubject(name.toLowerCase(), level);
    }

    public static void addObject(String name, int value, SecurityLevel level){

        monitor.addObject(name.toLowerCase(), value, level);
    }

    private class InstructionObject{
        private int operation;
        private String subjectName;
        private String objectName;
        private int writeValue;


        //TODO: instructions should parse properly, need to implement actual function calls now

        //this should be done being modified
        public InstructionObject(String instructionString){
            Scanner s = new Scanner(instructionString);
            int numArgs = new StringTokenizer(instructionString).countTokens();
            if (numArgs < 2 || numArgs > 4) {
                //bad instruction if too many or too few args
                operation = BADINSTRUCTION;
            }
            else {
                String operationString = s.next();
                parseOperation(operationString);
                if(operation == READ){
                    subjectName = s.next();
                    objectName = s.next();
                    if(numArgs != 3)
                        operation = BADINSTRUCTION;
                }
                else if(operation == WRITE){
                    if(numArgs != 4)
                        operation = BADINSTRUCTION;
                    else {
                        subjectName = s.next();
                        objectName = s.next();
                        writeValue = s.nextInt();
                    }
                }
                else if(operation == CREATE){
                    if(numArgs != 3)
                        operation = BADINSTRUCTION;
                    subjectName = s.next();
                    objectName = s.next();
                }
                else if(operation == DESTROY){
                    if(numArgs != 3)
                        operation = BADINSTRUCTION;
                    subjectName = s.next();
                    objectName = s.next();
                }
                else if(operation == RUN){
                    if(numArgs != 2)
                        operation = BADINSTRUCTION;
                    subjectName = s.next();
                }

                //operation is bad if already bad or subject and/or object don't exist
                operation = isBadInstruction() ? BADINSTRUCTION : operation;
            }


        }

        //this should be done being modified
        private void parseOperation(String op){
            if (op.toLowerCase().equals("read")){
                operation = READ;
            }
            else if (op.toLowerCase().equals("write")){
                operation = WRITE;
            }
            else if (op.toLowerCase().equals("create"))
                operation = CREATE;
            else if (op.toLowerCase().equals("destroy"))
                operation = DESTROY;
            else if (op.toLowerCase().equals("run"))
                operation = RUN;
            else
                operation = BADINSTRUCTION;
        }

        //this should be done being modified
        private boolean isBadInstruction(){
                if (!monitor.subjectExists(subjectName)) {
                    return true;
                }
            if(!(operation == RUN)) {
                if (!monitor.objectExists(objectName)) {
                    //ok for object to not exist if instruction is CREATE
                    if (operation == CREATE)
                        return false;
                    return true;
                }
            }
            return false;
        }
    }

}
