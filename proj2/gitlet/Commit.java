package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author cambrain
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */


    private String message;     //The message of this Commit.
    private String date;
    private String parent;      // The last commit reference
    /**
     * Map<Filename,BlobUID>
     * 存储跟踪文件，序列化id
     */
    public TreeMap<String, String> traceFile;

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        this.date = new Date().toString();
        traceFile = new TreeMap<>();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUID() {
        return Utils.sha1(date,message,parent,traceFile.toString());
    }


}
