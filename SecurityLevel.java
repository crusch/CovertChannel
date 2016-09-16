/**
 * Created by Chelsea on 2/6/2016.
 */
public class SecurityLevel {
    private static final int HIGH = 1;
    private static final int LOW = 0;
    private int level;

    public SecurityLevel(char l){
        level = ((l == 'h') || (l == 'H')) ? HIGH : LOW;
    }

    public boolean dominates(SecurityLevel other){
        //return true if THIS security level dominates argument
        return level >= other.level;
    }

}
