import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;




public class FileToTxt {
  private static String oripath = null;
  private static String newpath = null;
  private static int count = 0;


  public static void main(String[] args) {
    oripath = args[0];
    newpath = args[1];
    File file = new File(oripath);
    getFileList(file);
  }

  private static void getFileList(File pFile) {
    if (pFile.isDirectory()) {
      File[] files = pFile.listFiles();
      if(files != null){
          for (File file : files) {
              getFileList(file);
            }
      }
    } else {
      try {
        count++;
        transferFile(pFile);
        if (count % 100 == 0) {
          System.out.println(count);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void transferFile(File oFile) {
    String fileType = FileUtil.getTypeByFile(oFile);
    String context = fileReader(oFile.getAbsolutePath(), fileType);
    boolean isTxtEmpty = isTxtEmpty(context);
    if (isTxtEmpty) {
      System.out.println("文件转换Text内容为空。文件：" + oFile.getAbsolutePath());
    } else {
      String destPath = oFile.getParent().replace(oripath, newpath);
      File path = new File(destPath);
      if (!path.exists()) {
        path.mkdirs();
      }
      String filename = oFile.getName().replace(fileType, "");
      try {
        FileUtils.writeStringToFile(new File(destPath + "/" + filename + Constants.FILE_TYPE_TXT), context, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // 定义空格回车换行符
  private static final Pattern REGEX_SPACE = Pattern.compile("\\s*|\t|\r|\n");

  // 判断文本是否为空
  private static boolean isTxtEmpty(String context) {
    Matcher m = REGEX_SPACE.matcher(context);
    if (StringUtils.isEmpty(m.replaceAll(""))) {
      return true;
    }
    return false;
  }

  public static String fileReader(String filepath, String prefix) {
    String ss = "";
    if (".doc".equals(prefix)) {
      WordExtractor wordExtractor = null;
      FileInputStream filefis = null;
      try {
        filefis = new FileInputStream(filepath);
        wordExtractor = new WordExtractor(filefis);
        String[] paragraph = wordExtractor.getParagraphText();
        for (int j = 0; j < paragraph.length; j++) {
          ss += paragraph[j].toString();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (filefis != null) {
          try {
            filefis.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        if (wordExtractor != null) {
          try {
            wordExtractor.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    if (".docx".equals(prefix)) {
      POIXMLTextExtractor ex = null;
      try {
        OPCPackage opc = POIXMLDocument.openPackage(filepath);
        XWPFDocument xwdoc = new XWPFDocument(opc);
        ex = new XWPFWordExtractor(xwdoc);
        ss = ex.getText().trim();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (ex != null) {
          try {
            ex.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    return ss;
  }


}
