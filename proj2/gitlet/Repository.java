package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Repository {

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
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
        writeContents(HEAD, "master");
        writeContents(MASTER, commitUID);

        File file = join(COMMIT_DIR, commitUID);
        writeObject(file, initCommit);

    }

    //创建所需文件夹
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

    public static void addCommand(String fileName) throws IOException {
        //添加文件不存在
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Stage stage = new Stage();
        Blob blob = new Blob(file);
        //添加暂存
        stage.addFile(blob);
        stage.addMap.put(fileName, blob.getID());
        stage.writeAddContent();
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

    private static String getCurrentCommitID() {
        String currentBranch = readContentsAsString(HEAD);
        String commitID = readContentsAsString(join(BRANCH_DIR, currentBranch));
        return commitID;
    }

    private static String getSpecifyCommitID(String branchName) {
        String commitID = readContentsAsString(join(BRANCH_DIR, branchName));
        return commitID;
    }

    //获取当前提交
    public static Commit getCurrentCommit() {
        String commitID = getCurrentCommitID();
        File commitFile = join(COMMIT_DIR, commitID);
        return readObject(commitFile, Commit.class);
    }

    public static void commitCommand(String message) throws IOException {
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
            commit.traceFile.put(key, value);
        }
        //移除暂存
        for (String fileName : stage.removeList) {
            commit.traceFile.remove(fileName);
            restrictedDelete(fileName);
        }
        //提交commit
        String commitID = commit.getUID();
        File commitFile = join(COMMIT_DIR, commitID);
        commitFile.createNewFile();
        writeObject(commitFile, commit);
        //修改当前branch
        File branchFile = join(BRANCH_DIR, readContentsAsString(HEAD));
        writeContents(branchFile, commitID);
        //清空暂存区
        writeContents(ADD_DIR, "");
        writeContents(REMOVE_DIR, "");
    }

    public static void rmCommand(String fileName) {
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
        if (!currentCommit.traceFile.containsKey(fileName)) {
            System.out.println("文件未跟踪，删除暂存");
            addMap.remove(fileName);
            stage.writeAddContent();
        } else {
            // 文件已跟踪，添加暂存以移除
            System.out.println("文件已跟踪，添加暂存以移除");
            removeList.add(fileName);
            stage.writeRemoveContent();
        }
    }

    public static void logCommand() {

    }

    public static void globalLogCommand() {

    }

    public static void findCommand(String message) {
        boolean hasMessage = false;
        List<String> fileNameList = plainFilenamesIn(COMMIT_DIR);
        if (fileNameList != null) {
            for (String fileName : fileNameList) {
                File commitFile = join(COMMIT_DIR, fileName);
                Commit commit = readObject(commitFile, Commit.class);
                if (commit.message == message) {
                    System.out.println(message);
                    hasMessage = true;
                }
            }
        }
        if (!hasMessage) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void statusCommand() {

    }

    //将文件恢复到当前提交版本
    public static void checkoutFileCommand(String fileName) {
        String commitID = getCurrentCommitID();
        checkoutSpecifyFileCommand(commitID, fileName);
    }

    //将文件恢复到指定提交版本
    public static void checkoutSpecifyFileCommand(String commitID, String fileName) {
        Commit commit = readObject(join(COMMIT_DIR, commitID), Commit.class);
        if (commit.traceFile.containsKey(fileName)) {
            File file = join(Blob.BLOB_DIR, commit.traceFile.get(fileName));
            File currentFile = join(CWD, fileName);
            writeContents(currentFile, readContentsAsString(file));
        }
    }

    //修改当前分支
    public static void checkoutBranchCommand(String branchName) {
        if(!plainFilenamesIn(BRANCH_DIR).contains(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if(readContentsAsString(HEAD).equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        //获得指定分支的commitID
        String specifyCommitID = getSpecifyCommitID(branchName);
        File specifyCommitFile = join(COMMIT_DIR, specifyCommitID);
        Commit specifyCommit = readObject(specifyCommitFile, Commit.class);
        //当前提交
        Commit currentCommit = getCurrentCommit();
        //工作目录是否存在当前分支未跟踪文件被覆盖的情况
        for (Map.Entry<String, String> entry : specifyCommit.traceFile.entrySet()) {
            String key = entry.getKey();
            File file = join(CWD,key);
            if (file.exists()&&!currentCommit.traceFile.containsKey(key)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        //遍历指定分支,更新当前目录
        for (Map.Entry<String, String> entry : specifyCommit.traceFile.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String fileContent = readContentsAsString(join(Blob.BLOB_DIR,value));
            File file = join(CWD,key);
            writeContents(file,fileContent);
        }
        //遍历签出分支，删除冗余文件
        for (Map.Entry<String, String> entry : currentCommit.traceFile.entrySet()) {
            String key = entry.getKey();
            if(!specifyCommit.traceFile.containsKey(key)) {
                restrictedDelete(key);
            }
        }
        //清空暂存区
        writeContents(ADD_DIR,"");
        writeContents(REMOVE_DIR,"");
        //修改HEAD
        writeContents(HEAD,branchName);
    }

    //创建分支
    public static void branchCommand(String branchName) throws IOException {
        File branch = join(BRANCH_DIR,branchName);
        branch.createNewFile();
        writeContents(branch,getCurrentCommitID());
        writeContents(HEAD,branchName);
    }

    //删除分支
    public static void rmBranchCommand(String branchName) {
        if(!plainFilenamesIn(BRANCH_DIR).contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if(branchName.contains(readContentsAsString(HEAD))) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        restrictedDelete(branchName);
    }

    //重置版本
    public static void resetCommand(String commitID) {

    }
}
