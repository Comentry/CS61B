package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Blob implements Serializable {

    public String fileName;
    public String fileContent;
    private File file;
    public static final File BLOB_DIR = join(Repository.GITLET_DIR,"blob");

    public Blob(File file) {
        this.file = file;
        this.fileName = file.getName();
        fileContent = readContentsAsString(file);
    }

    public String getID() {
        return sha1(fileName,fileContent);
    }

    //将blob内容写入blobID文件中
    public File creatFile() throws IOException {
        //存入BOLB_DIR中,写入blobID中
        String blobID = getID();
        File blobFile = join(BLOB_DIR,blobID);
        blobFile.createNewFile();
        return blobFile;
    }
}
