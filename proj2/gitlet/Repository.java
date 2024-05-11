package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;


/**
 * Represents a gitlet repository.
 * depend on git method
 * does at a high level.
 *
 * @author Cambrain
 */
public class Repository {

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The blob directory.
     */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branch");
    public static final File ADD_DIR = join(GITLET_DIR, "add");
    public static final File REMOVE_DIR = join(GITLET_DIR, "remove");
    public static final File MASTER = join(BRANCH_DIR, "master");
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static void init() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        creatDir();

        Commit initCommit = new Commit("initial commit", "");
        initCommit.setDate("Thu Jun 01 00:00:00 UTC 1970");
        String commitUID = initCommit.getUID();
        writeContents(HEAD, commitUID);
        writeContents(MASTER, commitUID);

        File file = join(COMMIT_DIR, commitUID);
        writeObject(file, initCommit);

    }

    /**
     * 创建所需文件夹
     */
    private static void creatDir() throws IOException {
        GITLET_DIR.mkdir();
        Blob.BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BRANCH_DIR.mkdir();
        ADD_DIR.createNewFile();
        REMOVE_DIR.createNewFile();
        HEAD.createNewFile();
        MASTER.createNewFile();
    }

    public static void add(String fileName) throws IOException {
        //添加文件不存在
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Stage stage = new Stage();
        Blob blob = new Blob(file);
        //添加暂存
        if (!stage.exist(fileName)) {
            stage.addFile(blob);
            stage.addMap.put(fileName, blob.getID());
            stage.writeAddContent();
        }
        //工作目录和当前提交版本相同，删去暂存
        Commit currentCommit = getCurrentCommit();
        TreeMap<String, String> traceFile = currentCommit.traceFile;
        if (traceFile.containsKey(fileName) && traceFile.get(fileName) == blob.getID()) {
            stage.addMap.remove(fileName);
            stage.writeAddContent();
        }
        //删除暂存移除
        ArrayList<String> removeList = stage.removeList;
        removeList.remove(fileName);
        stage.writeRemoveContent();
    }

    //获取当前提交
    public static Commit getCurrentCommit() {
        String commitID = readContentsAsString(HEAD);
        File commitFile = join(COMMIT_DIR, commitID);
        return readObject(commitFile, Commit.class);
    }

    public static void commit(String message) throws IOException {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        if (readContentsAsString(ADD_DIR).isEmpty() && readContentsAsString(REMOVE_DIR).isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // 获取当前提交
        Commit currentCommit = getCurrentCommit();
        // 克隆上一个提交
        Commit commit = new Commit(message, currentCommit.getUID());
        commit.traceFile.putAll(currentCommit.traceFile);
        // 获取暂存信息，更新工作目录和commit
        //添加暂存
        Stage stage = new Stage();
        for (Map.Entry<String, String> entry : stage.addMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            commit.traceFile.put(key,value);
        }
        //移除暂存
        for (String fileName : stage.removeList) {
            commit.traceFile.remove(fileName);
            restrictedDelete(fileName);
        }
        //提交commit
        String commitID = commit.getUID();
        File commitFile = join(COMMIT_DIR,commitID);
        commitFile.createNewFile();
        writeObject(commitFile,commit);
    }


    public static void rm(String fileName) {
        // 读取当前提交
        Commit currentCommit = getCurrentCommit();
        // 读取暂存区
        Stage stage = new Stage();
        TreeMap<String, String> addMap = stage.addMap;
        ArrayList<String> removeList = stage.removeList;
        //文件未跟踪，且暂存区无文件
        if (!addMap.containsKey(fileName) && !currentCommit.traceFile.containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        // 文件未跟踪，删除暂存
        addMap.remove(fileName);
        stage.writeAddContent();
        // 文件已跟踪，添加暂存以移除
        removeList.add(fileName);
        stage.writeRemoveContent();

    }

    public static void printLog() {
    }

    public static void main(String[] args) throws IOException {

    }
}
