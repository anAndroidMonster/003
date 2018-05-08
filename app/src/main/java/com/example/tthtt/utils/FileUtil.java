package com.example.tthtt.utils;

import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #-readFile(String)} read file</li>
 * <li>{@link #-readFileToList(String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file from String</li>
 * <li>{@link #writeFile(String, String)} write file from String</li>
 * <li>{@link #writeFile(String, List, boolean)} write file from String List</li>
 * <li>{@link #writeFile(String, List)} write file from String List</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class FileUtil {

    public final static String FILE_EXTENSION_SEPARATOR = ".";
    public static final String TAG = "FileUtil";
    private static final int IO_BUFFER_SIZE = 16384;
    private static final long ONE_DAY_MILLIS = 0x5265c00L;

    private FileUtil() {
        throw new AssertionError();
    }

    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static byte[] readFileToByte(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(
                (int) file.length());
        InputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try {
            int i = -1;
            byte[] buffer = new byte[1024];
            while ((i = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, i);
            }
            return bos.toByteArray();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Serializable readFile(File file) {
        Object object = null;
        InputStream fis = null;
        ObjectInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            dis = new ObjectInputStream(fis);
            object = dis.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (Serializable) object;
    }

    public static long getAvailableStorageSize(File file) {
        try {
            if (file != null && file.exists() && file.isDirectory()) {
                StatFs statfs = new StatFs(file.getPath());
                return statfs.getBlockSize() * (long) statfs.getAvailableBlocks();
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public static void writeFile(File file, byte[] data) throws Exception {
        if (file != null && data != null) {
            OutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.write(data);
            if (dos != null)
                dos.close();
        }
    }


    public static void writeFile(File file, Serializable data) {
        OutputStream fos = null;
        ObjectOutputStream dos = null;
        if (file != null && data != null) {
            try {
                fos = new FileOutputStream(file);
                dos = new ObjectOutputStream(fos);
                dos.writeObject(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {

                        fos.close();
                    }
                    if (dos != null) {
                        dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtil.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param contentList
     * @param append      is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if ((contentList == null || contentList.size() == 0)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param -file  the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    public static void copyFile(File source, File target) throws IOException {
        if (source != null && target != null) {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(source));
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(target));
            try {
                byte[] buffer = new byte[8192];
                int i = -1;
                while ((i = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, i);
                }
                bos.flush();
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        }
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     * <p>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     * <p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * <li>return {@link File#-makeFolder}</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtil.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtil.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtil.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtil.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }


    public static boolean create(String absPath) {
        return create(absPath, false);
    }

    public static boolean create(String absPath, boolean force) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }

        if (exists(absPath)) {
            return true;
        }

        String parentPath = getParent(absPath);
        mkdirs(parentPath, force);

        try {
            File file = new File(absPath);
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean mkdirs(String absPath) {
        return mkdirs(absPath, false);
    }

    public static boolean mkdirs(String absPath, boolean force) {
        File file = new File(absPath);
        if (exists(absPath) && !isFolder(absPath)) {
            if (!force) {
                return false;
            } else {
                delete(file);
            }
        }
        try {
            file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists(file);
    }

    public static void move(File file, File file1) throws IOException {
        copy(file, file1);
        delete(file);
    }

    public static boolean move(String srcPath, String dstPath) {
        return move(srcPath, dstPath, false);
    }

    public static boolean move(String srcPath, String dstPath, boolean force) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
            return false;
        }

        if (!exists(srcPath)) {
            return false;
        }

        if (exists(dstPath)) {
            if (!force) {
                return false;
            } else {
                delete(dstPath);
            }
        }

        try {
            File srcFile = new File(srcPath);
            File dstFile = new File(dstPath);
            return srcFile.renameTo(dstFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }

        File file = new File(absPath);
        return delete(file);
    }

    public static boolean delete(File file) {
        if (!exists(file)) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        boolean result = true;
        File files[] = file.listFiles();
        for (int index = 0; index < files.length; index++) {
            result |= delete(files[index]);
        }
        result |= file.delete();

        return result;
    }

    /**
     * @param file
     * @param self 删除自身
     */
    public static boolean deleteDirectory(File file, boolean self) {
        return deleteDirectory(file, null, self, false);
    }

    /**
     * @param file
     * @param end  匹配后缀名
     * @param self 删除自身
     * @param loop 循环目录删除
     */
    public static boolean deleteDirectory(File file, String end, boolean self,
                                          boolean loop) {
        if (file == null) {
            return false;
        }
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        boolean flag3 = true;
        File[] f = file.listFiles();
        for (int i = 0; i < f.length; i++) {
            if (f[i].isFile()) {
                if (end == null
                        || f[i].getName().toLowerCase()
                        .endsWith("." + end.toLowerCase())) {
                    flag3 = delete(f[i]);
                    if (!flag3) {
                        break;
                    }
                }
            } else {
                if (!loop) {
                    flag3 = deleteDirectory(f[i], true);
                    if (!flag3) {
                        break;
                    }
                }
            }
        }
        if (!flag3) {
            Log.i(TAG, "delete directory fail: " + file.getAbsolutePath());
        } else if (self) {
            if (file.delete())
                return true;
            else
                Log.i(TAG, "delete directory fail: " + file.getAbsolutePath());
        } else {
            return true;
        }
        return false;
    }

    public static boolean deleteDirectoryByTime(File file, int day) {
        boolean flag = true;
        if ((file == null || file.exists()) && file.isDirectory()) {
            if (file != null) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        if (System.currentTimeMillis() - f.lastModified()
                                - ONE_DAY_MILLIS * (long) day > 0L)
                            if (f.isDirectory())
                                flag = deleteDirectory(f, true);
                            else
                                flag = delete(f);
                    }
                }
            }
        } else {
            return false;
        }
        return flag;
    }

    public static boolean exists(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }
        File file = new File(absPath);
        return exists(file);
    }

    public static boolean exists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    public static boolean childOf(String childPath, String parentPath) {
        if (TextUtils.isEmpty(childPath) || TextUtils.isEmpty(parentPath)) {
            return false;
        }
        childPath = cleanPath(childPath);
        parentPath = cleanPath(parentPath);
        if (childPath.startsWith(parentPath + File.separator)) {
            return true;
        }
        return false;
    }

    public static int childCount(String absPath) {
        if (!exists(absPath)) {
            return 0;
        }
        File file = new File(absPath);
        File[] children = file.listFiles();
        if (children == null || children.length == 0) {
            return 0;
        }
        return children.length;
    }

    public static String cleanPath(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return absPath;
        }
        try {
            File file = new File(absPath);
            absPath = file.getCanonicalPath();
        } catch (Exception e) {

        }
        return absPath;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        long dirSize = 0;
        try{
            if (dir == null) {
                return 0;
            }
            if (!dir.isDirectory()) {
                return 0;
            }

            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += file.length();
                    dirSize += getDirSize(file); // 递归调用继续统计
                }
            }
        }catch (Exception e)
        {
            return 0;
        }
        return dirSize;
    }

    public static long size(String absPath) {
        if (absPath == null) {
            return 0;
        }
        File file = new File(absPath);
        return size(file);
    }

    public static long size(File file) {
        if (!exists(file)) {
            return 0;
        }

        long length = 0;
        if (isFile(file)) {
            length = file.length();
            return length;
        }

        File files[] = file.listFiles();
        if (files == null || files.length == 0) {
            return length;
        }

        int size = files.length;
        for (int index = 0; index < size; index++) {
            File child = files[index];
            length += size(child);
        }
        return length;
    }

    public static boolean copy(String srcPath, String dstPath) {
        return copy(srcPath, dstPath, false);
    }

    public static boolean copy(String srcPath, String dstPath, boolean force) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
            return false;
        }

        // check if copy source equals destination
        if (srcPath.equals(dstPath)) {
            return true;
        }

        // check if source file exists or is a directory
        if (!exists(srcPath) || !isFile(srcPath)) {
            return false;
        }

        // delete old content
        if (exists(dstPath)) {
            if (!force) {
                return false;
            } else {
                delete(dstPath);
            }
        }
        if (!create(dstPath)) {
            return false;
        }

        FileInputStream in = null;
        FileOutputStream out = null;

        // get streams
        try {
            in = new FileInputStream(srcPath);
            out = new FileOutputStream(dstPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            byte[] buffer = new byte[IO_BUFFER_SIZE];

            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void copy(File source, File target) throws IOException {
        if (source != null && !source.exists())
            Log.i(TAG, "the source file is not exists: "
                    + source.getAbsolutePath());
        else if (source.isFile())
            copyFile(source, target);
        else
            copyDirectory(source, target);
    }

    public static void copyDirectory(File sourceDir, File targetDir)
            throws IOException {
        targetDir.mkdirs();
        if (sourceDir != null) {
            File[] f = sourceDir.listFiles();
            for (int i = 0; i < f.length; i++) {
                if (f[i].isFile()) {
                    copyFile(
                            f[i],
                            new File((new StringBuilder(String
                                    .valueOf(targetDir.getAbsolutePath())))
                                    .append(File.separator)
                                    .append(f[i].getName()).toString()));
                } else if (f[i].isDirectory()) {
                    copyDirectory(new File(sourceDir, f[i].getName()),
                            new File(targetDir, f[i].getName()));
                }
            }
        }
    }

    public final static boolean isFile(String absPath) {
        boolean exists = exists(absPath);
        if (!exists) {
            return false;
        }

        File file = new File(absPath);
        return isFile(file);
    }

    public final static boolean isFile(File file) {
        if (file == null) {
            return false;
        }

        return file.isFile();
    }

    public final static boolean isFolder(String absPath) {
        boolean exists = exists(absPath);
        if (!exists) {
            return false;
        }

        File file = new File(absPath);
        return file.isDirectory();
    }

    public final static String getName(File file) {
        if (file == null) {
            return null;
        } else {
            return getName(file.getAbsolutePath());
        }
    }

    public final static String getName(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return absPath;
        }

        String fileName = null;
        int index = absPath.lastIndexOf("/");
        if (index > 0 && index < (absPath.length() - 1)) {
            fileName = absPath.substring(index + 1, absPath.length());
        }
        return fileName;
    }

    public final static String getParent(File file) {
        if (file == null) {
            return null;
        } else {
            return file.getParent();
        }
    }

    public final static String getParent(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return null;
        }
        absPath = cleanPath(absPath);
        File file = new File(absPath);
        return getParent(file);
    }

    public static String getStem(File file) {
        if (file == null) {
            return null;
        }
        return getStem(file.getName());
    }

    public final static String getStem(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            return fileName.substring(0, index);
        } else {
            return "";
        }
    }

    public static String getExtension(File file) {
        if (file == null) {
            return null;
        }
        return getExtension(file.getName());
    }

    public static String getExtension(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        int index = fileName.lastIndexOf('.');
        if (index < 0 || index >= (fileName.length() - 1)) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    public static String getMimeType(File file) {
        if (file == null) {
            return "*/*";
        }
        String fileName = file.getName();
        return getMimeType(fileName);
    }

    public static String getMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "*/*";
        }
        String extension = getExtension(fileName);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String type = map.getMimeTypeFromExtension(extension);
        if (TextUtils.isEmpty(type)) {
            return "*/*";
        } else {
            return type;
        }
    }

    public static String fileSHA1(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return null;
        }
        File file = new File(absPath);

        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        String fileSHA1 = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int length = 0;
            while ((length = fis.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, length);
            }
            fis.close();
            fileSHA1 = DigestUtil.bytes2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(fileSHA1)) {
            fileSHA1 = fileSHA1.trim();
        }
        return fileSHA1;
    }

    public static String fileMD5(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return null;
        }
        File file = new File(absPath);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        String fileMD5 = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int length = 0;
            while ((length = fis.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, length);
            }
            fis.close();
            fileMD5 = DigestUtil.bytes2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(fileMD5)) {
            fileMD5 = fileMD5.trim();
        }
        return fileMD5;
    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    //建立一个MIME类型与文件后缀名的匹配表
    private final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}
