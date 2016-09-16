/**
 * Created by Chelsea on 2/4/2016.
 */
import java.io.*;
import java.util.*;
public class ReferenceMonitor {
    private ObjectManager manager;
    private Map<String, Subject> subjects;

    public ReferenceMonitor(){
        manager = new ObjectManager();
        subjects = new HashMap<>();
    }

    public int readObject(String objectName, String subjectName){
        if(subjects.get(subjectName).getLevel().dominates(manager.getObjectLevel(objectName))){
            //we can read
            //update subject's TEMP
            subjects.get(subjectName).updateTemp(manager.read(objectName));
            return manager.read(objectName);
        }
        else {
            subjects.get(subjectName).updateTemp(0);
            return 0;
        }
    }

    public int writeObject(String objectName, String subjectName, int value){
        if(manager.getObjectLevel(objectName).dominates(subjects.get(subjectName).getLevel())){
            //we can write
            //update object's value
            manager.write(objectName, value);
            return value;
        }
        else
            return 0;
    }

    public void runSubject(String name){
        subjects.get(name).run();
    }

    public void destroy(String objectName){
        manager.objectMap.remove(objectName);
    }

    public boolean canWrite(String subjectName, String objectName){
        return manager.getObjectLevel(objectName).dominates(getSubject(subjectName).getLevel());
    }

    public Subject getSubject(String name){
        return subjects.get(name);
    }
    public SecurityLevel getSubjectLevel(String subjectName){
        return subjects.get(subjectName).getLevel();
    }

    public boolean subjectExists(String name){

        return subjects.containsKey(name.toLowerCase());
    }

    public boolean objectExists(String name){
        return manager.objectExists(name.toLowerCase());
    }

    public void printSubjects(){
        for(String s : subjects.keySet()){
            System.out.println(s + " has recently read: " + subjects.get(s).TEMP);
        }
    }

    public void printObjects(){
        manager.printObjects();
    }


    //debug functions for main
    public void addSubject(String subjectName, SecurityLevel level){
        subjects.put(subjectName, new Subject(subjectName, level));
    }

    public void addObject(String objectName, int value, SecurityLevel level){
        manager.addObject(value, objectName, level);
    }


    private class ObjectManager{
        private Map<String, Object> objectMap;

        private ObjectManager(){
            objectMap = new HashMap<>();

        }

        private int read(String objectName){
            return (objectMap.get(objectName)).getValue();
        }

        private int write(String objectName, int value){
            (objectMap.get(objectName)).editValue(value);
            return 0;
        }

        private void addObject(int value, String name, SecurityLevel level){
            objectMap.put(name, new Object(value, name, level));
        }

        private SecurityLevel getObjectLevel(String objectName){
            return objectMap.get(objectName).getLevel();
        }


        private boolean objectExists(String name){
            return objectMap.containsKey(name);
        }

        private void printObjects(){
            for(String s : objectMap.keySet()){
                System.out.println(s + " has value " + objectMap.get(s).getValue());
            }

        }


        private class Object {
            private int value;
            private String name;
            private SecurityLevel level;

            private Object(int val, String objName, SecurityLevel lvl){
                value = val;
                name = objName;
                level = lvl;
            }

            private int getValue(){
                return value;
            }

            private int editValue(int newValue){
                value = newValue;
                return value;
            }

            private SecurityLevel getLevel(){
                return level;
            }

        }
    }



    private class Subject{
        private String name;
        private int TEMP;
        private SecurityLevel level;
        private File outputFile;
        private FileOutputStream out;
        private int[] bitArray = {0,0,0,0,0,0,0,0};
        private int arraySize = 0;

        public Subject(String subjectName, SecurityLevel securityLevel) {
            name = subjectName;
            TEMP = 0;
            level = securityLevel;
            outputFile = new File("output.txt");
            try {
                out = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                System.out.println("Error creating output file.");
                return;
            }
        }

        private SecurityLevel getLevel(){
            return level;
        }

        private void updateTemp(int value){
            TEMP = value;
        }

        //**********************
        private void run(){
            if(arraySize < 8){
                //read a bit
                bitArray[arraySize] = TEMP;
                arraySize++;
            }
            if(arraySize == 8){
                //write to file
                //convert array into int
                int charInt = 0;
                for(int i = 0; i < 8; i++) {
                    charInt += (bitArray[i] * Math.pow(2, i));
                }
                System.out.println("Writing " + (char)(charInt));
                try{
                    out.write((char) charInt);
                }
                catch(IOException e){
                    System.out.println("Problem writing to file.");
                    return;
                }

                arraySize = 0;
            }
        }
    }

}
