package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;

public class Stage {

    String addContent;
    String removeContent;
    File addStage, removeStage;
    TreeMap<String, String> addMap;
    ArrayList<String> removeList;
    public static final String CRLF = "\r\n";

    public Stage() {
        addMap = new TreeMap<>();
        removeList = new ArrayList<>();
        addStage = Repository.ADD_DIR;
        removeStage = Repository.REMOVE_DIR;
        addContent = readContentsAsString(addStage);
        removeContent = readContentsAsString(removeStage);
        readAddFile(addStage);
        readRemoveFile(removeStage);
    }

    //读取索引信息
    public void readAddFile(File file) {
        String[] index = addContent.split("\r\n");
        for (String s : index) {
            if (s.isEmpty()) {
                break;
            }
            String fileName = s.split(" ")[0];
            String blobId = s.split(" ")[1];
            addMap.put(fileName, blobId);
        }
    }

    //读取索引信息
    private void readRemoveFile(File removeStage) {

        String[] index = removeContent.split("\r\n");
        removeList.addAll(Arrays.asList(index));
    }

    //判断暂存区是否存在文件
    public boolean exist(String fileName) {
        return addMap.containsKey(fileName);
    }

    //添加到暂存区
    public File addFile(Blob blob)  {
        //文件转化为blob对象
        File blobFile = blob.creatFile();
        writeContents(blobFile, blob.fileContent);
        return blobFile;
    }

    //修改索引
    public void updateFile(File file) throws IOException {
        Blob blob = new Blob(file);
        File blobFile = blob.creatFile();
        writeContents(blobFile, blob.fileContent);
        String blobID = blob.getID();
        addMap.put(blob.fileName, blobID);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" ").append(value).append("\r\n");
        }
        writeContents(addStage, sb.toString());
    }

    //将addMap内容写入addstage中
    public void writeAddContent() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" ").append(value).append(CRLF);
        }
        writeContents(addStage, sb.toString());
    }

    //将removeList内容写入removestage
    public void writeRemoveContent() {
        StringBuilder sb = new StringBuilder();
        for (String filename : removeList) {
            if (!filename.isEmpty()) {
                sb.append(filename).append(CRLF);
            }
        }
        writeContents(removeStage, sb.toString());
    }
}
