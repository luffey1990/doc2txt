import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;

public class FileUtil {

  private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

  private static final TikaConfig TIKA_CONFIG = TikaConfig.getDefaultConfig();

  private static final int BUFFER_SIZE = 4096;

  // private static final String LINE_SEPARATPR =
  // System.getProperty("line.separator");

  /*** 从zip文件中解压 开始 **/
  /***
   * 从Zip文件中解压文件，并删除原文件。
   * 
   * @param zipfile Input .zip file
   * @param dest Output directory
   */
  public static List<String> unZip(String zipfile, String dest) {
    return unZip(new File(zipfile), dest);
  }

  public static List<String> unZip(File zipfile, String dest) {

    List<String> files = new ArrayList<String>();

    try {
      File outdir = new File(dest);

      ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
      ZipEntry entry;
      String name, dir;
      while ((entry = zin.getNextEntry()) != null) {
        name = entry.getName();
        if (entry.isDirectory()) {
          mkdirs(outdir, name);
          continue;
        }

        /*
         * this part is necessary because file entry can come before directory entry where is file
         * located i.e.: /foo/foo.txt /foo/
         */
        dir = dirpart(name);
        if (dir != null) {
          mkdirs(outdir, dir);
        }

        extractFile(zin, outdir, name);
        files.add(dest + name);
      }
      zin.close();
    } catch (IOException e) {
      logger.error("文件解压失败，文件：" + zipfile.getPath());
    } finally {
      zipfile.delete();
    }
    return files;
  }

  private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException {

    byte[] buffer = new byte[BUFFER_SIZE];
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir, name)));
    int count = -1;
    while ((count = in.read(buffer)) != -1)
      out.write(buffer, 0, count);
    out.close();
  }

  private static void mkdirs(File outdir, String path) {
    File d = new File(outdir, path);
    if (!d.exists())
      d.mkdirs();
  }

  private static String dirpart(String name) {
    int s = name.lastIndexOf(File.separatorChar);
    return s == -1 ? null : name.substring(0, s);
  }

  /*** 从zip文件中解压 结束 **/

  /****** 判断文件类型。开始 ****/
  // 根据文件流的头部，判断文件类型。
  public static String getTypeByName(String fileName) {

    try {
      return TIKA_CONFIG.getMimeRepository().forName(new Tika().detect(fileName)).getExtension();
    } catch (Exception e) {
      logger.error("根据文件名解析文件类型失败，文件：" + fileName);
    }

    return "unknown";
  }

  public static String getTypeByFile(File file) {

    try {
      return TIKA_CONFIG.getMimeRepository().forName(new Tika().detect(file)).getExtension();
    } catch (Exception e) {
      logger.error("根据文件内容解析文件类型失败，文件：" + file.getPath());
    }

    return Constants.FILE_TYPE_UNKNOWN;

  }

  public static String getTypeByFile(InputStream is) {

    try {
      return TIKA_CONFIG.getMimeRepository().forName(new Tika().detect(is)).getExtension();
    } catch (Exception e) {
      logger.error("根据文件码解析文件类型失败，");
    }

    return Constants.FILE_TYPE_UNKNOWN;

  }

  /****** 根据文件流的头部，判断文件类型。结束 ****/

  /**
   * 文件解析为文本
   * 
   * @param fileName
   * @return
   */
  public static String parseToText(String fileName) {

    String content = "";
    BodyContentHandler handler = new BodyContentHandler(10 * 1024 * 1024);

    InputStream stream = null;

    AutoDetectParser parser = new AutoDetectParser();
    Metadata metadata = new Metadata();
    try {
      stream = new FileInputStream(new File(fileName));
      parser.parse(stream, handler, metadata);
      content = handler.toString();
    } catch (Exception e) {
      logger.error("文件解析为文本失败，文件：" + fileName, e);
    } finally {
      try {
        stream.close();
      } catch (Exception e) {
        logger.error("", e);
      }
    }
    return content;

  }

  /**
   * 文件解析为HTML
   * 
   * @param fileName
   */
  public static String parseToHTML(String fileName) {

    String content = "";
    ContentHandler handler = new ToXMLContentHandler();

    InputStream stream = null;
    AutoDetectParser parser = new AutoDetectParser();
    Metadata metadata = new Metadata();
    metadata.set("content", "text/html; charset=UTF-8");
    try {
      stream = new FileInputStream(new File(fileName));
      parser.parse(stream, handler, metadata);
      content = handler.toString();
    } catch (Exception e) {
      logger.error("文件解析为HTML失败，文件：" + fileName, e);
    } finally {
      try {
        stream.close();
      } catch (Exception e) {
        logger.error("", e);
      }
    }
    return content;
  }

  /**
   * 移动文件
   */
  public static void moveFile(String src, String dest) {

    moveFile(new File(src), dest);
  }

  public static void moveFile(File src, String dest) {

    int index = dest.lastIndexOf(".");
    if (index == -1) {
      dest = dest + src.getName();
    }

    moveFile(src, new File(dest));
  }

  public static void moveFile(File src, File dest) {
    copyFile(src, dest);
    src.delete();
  }

  /**
   * 复制文件
   */
  public static void copyFile(String src, String dest) {

    copyFile(new File(src), dest);
  }

  public static void copyFile(File src, String dest) {

    int index = dest.lastIndexOf(".");
    if (index == -1) {
      dest = dest + src.getName();
    }

    copyFile(src, new File(dest));
  }

  public static void copyFile(File src, File dest) {
    FileInputStream fi = null;
    FileOutputStream fo = null;
    FileChannel in = null;
    FileChannel out = null;
    try {

      fi = new FileInputStream(src);
      fo = new FileOutputStream(dest);
      in = fi.getChannel();// 得到对应的文件通道
      out = fo.getChannel();// 得到对应的文件通道
      in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
          if(fi != null){
              fi.close();
          }
          if(in != null){
              in.close();
          }
          if(fo != null){
              fo.close();
          }
          if(out != null){
              out.close();
          }
        
      } catch (Exception e) {
        logger.error("复制文件失败，源文件：" + src.getPath() + ",目标地址:" + dest.getPath(), e);
      }
    }
  }

  /**
   * 文件重命名，改为案件ID，并添加后缀
   * 
   * @param fileName 文件名
   * @param destName 输出名
   * @return
   */
  public static String renameFile(String fileName, String destName) {

    File file = new File(fileName);

    // 根据文件内容，取得的文件类型
    String realType = getTypeByFile(file);

    String newFileName = destName + realType;
    File newFile = new File(file.getParent(), newFileName);
    file.renameTo(newFile);

    return newFile.getPath();
  }

  public static void writeFile(String fileName, String content) {
    writeFile(fileName, content, false);
  }

  public static void writeFile(String fileName, String content, boolean isAppend) {
    FileWriter fstream = null;
    BufferedWriter out = null;
    try {
      fstream = new FileWriter(fileName, isAppend);
      out = new BufferedWriter(fstream);
      out.write(content);
      out.close();
    } catch (Exception e) {
      logger.error("写文件失败，文件：" + fileName, e);
    } finally {
      try {
        fstream.close();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static String readToString(File file) {
    // FileInputStream fis = null;
    // StringBuffer sb = new StringBuffer();
    // try {
    // fis = new FileInputStream(file);
    // byte[] b = new byte[1024 * 1024];
    // while (fis.read(b) != -1) {
    // sb.append(new String(b));
    // }
    // } catch (Exception e) {
    // logger.error("读取文件失败，文件：" + file.getName(), e);
    // } finally {
    // if (fis != null) {
    // try {
    // fis.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // return sb.toString();

    Long filelength = file.length(); // 获取文件长度
    FileInputStream in = null;
    byte[] filecontent = new byte[filelength.intValue()];
    try {
      in = new FileInputStream(file);
      in.read(filecontent);
      in.close();
    } catch (Exception e) {
      logger.error("读取文件失败，文件：" + file.getName(), e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return new String(filecontent);// 返回文件内容,默认编码

    // FileInputStream inputStream = null;
    // Scanner sc = null;
    // StringBuffer sb = new StringBuffer();
    //
    // try {
    // inputStream = new FileInputStream(file);
    // sc = new Scanner(inputStream, "UTF-8");
    // while (sc.hasNextLine()) {
    // sb.append(sc.nextLine()).append(LINE_SEPARATPR);
    // }
    // } catch (Exception e) {
    // logger.error("读取文件失败，文件：" + file.getName(), e);
    // } finally {
    // if (inputStream != null) {
    // try {
    // inputStream.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // if (sc != null) {
    // sc.close();
    // }
    // }
    // return sb.toString();

    // // 获取文件长度
    // Long filelength = file.length();
    // byte[] filecontent = new byte[filelength.intValue()];
    // try {
    // FileInputStream in = new FileInputStream(file);
    // in.read(filecontent);
    // in.close();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // // 返回文件内容,默认编码
    // return new String(filecontent);
  }

  public static String getFileName(String file) {
    File f = new File(file);
    return f.getName();
  }

  public static void writeParserModel(String file, byte[] content) throws IOException {

    BufferedOutputStream out = null;
    try {
      out = new BufferedOutputStream(new FileOutputStream(new File(file)));
      out.write(content);

    } finally {
      try {
        if (out != null)
          out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public static byte[] readParserModel(String file) throws IOException {

    FileInputStream in = null;
    byte[] buffer = new byte[0];

    try {
      in = new FileInputStream(file);
      buffer = new byte[in.available()];
      in.read(buffer);
    } finally {
      try {
        if (in != null)
          in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return buffer;

  }

  @SuppressWarnings("resource")
public static final void main(String[] args) throws Exception {

    String strFile = "C:\\Files\\3\\1 (1).docx";
//    InputStream is = new FileInputStream(new File(strFile));
//    WordExtractor ex = new WordExtractor(is);
//    String text2003 = ex.getText();
//    System.out.println(text2003);

    OPCPackage opcPackage = POIXMLDocument.openPackage(strFile);
    POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
    String text2007 = extractor.getText();
    System.out.println(text2007);

    Tika tika = new Tika();
    try {
      String text = tika.parseToString(new File(strFile));
      System.out.println(text);
    } catch (IOException  e) {
      e.printStackTrace();
    }

    parseToText(strFile);

    String file = "C:\\work\\99_temp\\jdk-7u79-windows-x64.exe";
    String file2 = "C:\\work\\99_temp\\jdk-7u79-windows-x64_12.exe";

    try {
      byte[] content = readParserModel(file);
      writeParserModel(file2, content);

    } catch (IOException e1) {
      e1.printStackTrace();
    }

  }
}
