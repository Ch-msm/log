package log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * 自定义日志文件处理器---日期及文件大小存储，并自动清除过期日志文件
 * time: 2020/6/19 18:19
 *
 * @author msm
 */
public class XLFileStreamHandler extends StreamHandler {
  /**
   * 抄自FileHandler的实现，用于跟踪写入文件的字节数 这样以便提高效率
   */
  private class MeteredStream extends OutputStream {

    private final OutputStream out;
    // 记录当前写入字节数
    private int written;

    MeteredStream(OutputStream out, int written) {
      this.out = out;
      this.written = written;
    }

    @Override
    public void close() throws IOException {
      out.close();
    }

    @Override
    public void flush() throws IOException {
      out.flush();
    }

    @Override
    public void write(byte buff[]) throws IOException {
      out.write(buff);
      written += buff.length;
    }

    @Override
    public void write(byte buff[], int off, int len) throws IOException {
      out.write(buff, off, len);
      written += len;
    }

    @Override
    public void write(int b) throws IOException {
      out.write(b);
      written++;
    }
  }

  private class XLLogFile extends File {
    private static final long serialVersionUID = 952141123094287978L;
    private Date date;
    private String dateString;
    private int sid;

    public int getSid() {
      return this.sid;
    }

    public void setSid(int sid) {
      this.sid = sid;
    }

    public Date getDate() {
      return this.date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public String getDateString() {
      return this.dateString;
    }

    public void setDateString(String dateString) {
      this.dateString = dateString;
    }

    @Override
    public int compareTo(File another) {
      XLLogFile ano = (XLLogFile) another;
      int dateComResult = date.compareTo(ano.getDate());
      if (dateComResult == 0) {
        return sid - ano.getSid();
      }
      return dateComResult;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public XLLogFile(String pathname) {
      super(pathname);
      try {
        int dot = pathname.lastIndexOf('.');
        int split = pathname.lastIndexOf(splitDateIndexChar);
        int underline = pathname.lastIndexOf('_');
        dateString = pathname.substring(underline + 1, split);
        String numStr = pathname.substring(split + 1, dot);
        date = sdf.parse(dateString);
        sid = Integer.valueOf(numStr);
      } catch (Exception e) {
        System.err.println("log对应文件夹中包含了不符合XLLOG格式的文件!!");
        e.printStackTrace();
      }

    }
  }

  private static final String undifine = "xlcallcenter";
  // 是否将日志写入已存在的日志文件中
  private boolean append;
  // 保存几天之内的日志文件
  // 时间间隔小于等于0时表明不删除历史记录
  private int dateline = 5;
  // 保存存在的日志文件
  private Map<String, TreeSet<XLLogFile>> files;
  // 每个日志希望写入的最大字节数，如果日志达到最大字节数则当天日期的一个新的编号的日志文件将被创建，最新的日志记录在最大编号的文件中
  // 文件大小为小于等于0时表明不限制日志文件大小
  private int limit = 1048576 * 5;
  // 输出流
  private MeteredStream msOut;
  // 文件路径， 可以是个目录或希望的日志名称，如果是个目录则日志为"callcenter_zd"
  // 指定日志名称时不需要包括日期，程序会自动生成日志文件的生成日期及相应的编号
  private String pattern = "./log/xunleidemo";
  private char splitDateIndexChar = '#';

  public XLFileStreamHandler() throws Exception {
    configure();
    openWriteFiles();
  }


  /**
   * 初始化自定义文件流处理器
   *
   * @param fileUrl
   *            文件路径， 可以是个目录或希望的日志名称，如果是个目录则日志为"callcenter_zd"
   *            指定日志名称时不需要包括日期，程序会自动生成日志文件的生成日期及相应的编号
   * @param limit
   *            每个日志希望写入的最大字节数，如果日志达到最大字节数则当天日期的一个新的编
   *            号的日志文件将被创建，最新的日志记录在最大编号的文件中
   * @param dateline 
   *            保存几天之内的日志文件
   * @param append
   *            是否将日志写入已存在的日志文件中
   * @throws java.lang.Exception 异常
   */
  public XLFileStreamHandler(String fileUrl, int limit, int dateline,
                             boolean append) throws Exception {
    super();
    this.pattern = fileUrl;
    this.limit = limit;
    this.dateline = dateline;
    this.append = append;
    openWriteFiles();
  }

  /**
   * 检查当前日志时间,删除过期日志
   */
  private void deleteExpiredLog() {

    try {
      // 今天作为基准
     
      String today = LogUtil.getCurrentDateStr("yyyy-MM-dd");
      // 删除过期日志
      for (String keyDate : files.keySet()) {
        TreeSet<XLLogFile> traceDateFiles = files.get(keyDate);
        for (File deletingFile : traceDateFiles) {
          deletingFile.delete();
        }
        files.remove(today);
      }
      
    } catch (Exception ex) {
      Logger.getLogger(XLFileStreamHandler.class.getName()).log(
          Level.SEVERE, null, ex);
    }
  }

  // Private method to configure a FileHandler from LogManager
  // properties and/or default values as specified in the class
  private void configure() {
    LogManager manager = LogManager.getLogManager();
    String cname = getClass().getName();

    // 获得pattern
    pattern = manager.getProperty(cname + ".pattern");
    if (pattern == null) {
      pattern = "./log/xunleidemo";
    }

    // 获得limit
    String limitVal = manager.getProperty(cname + ".limit");
    if (limitVal == null) {
      limit = 1048576 * 5;
    } else {
      try {
        limit = Integer.parseInt(limitVal.trim());
        // if (limit < 0) {
        // limit = 1048576 * 5;
        // }
      } catch (Exception ex) {
        limit = 1048576 * 5;
      }
    }

    // 获得formatter
    String formatVal = manager.getProperty(cname + ".formatter");
    if (formatVal == null) {
      setFormatter(new Formatter() {
        @Override
        public String format(LogRecord record) {
          return LogUtil.logOutputFormat(record);
        }
      });
    } else {
      try {
        Class clz = ClassLoader.getSystemClassLoader().loadClass(
            formatVal);
        setFormatter((Formatter) clz.newInstance());
      } catch (Exception ex) {
        // We got one of a variety of exceptions in creating the
        // class or creating an instance.
        // Drop through.
      }
    }

    // 获得append
    String appendVal = manager.getProperty(cname + ".append");
    if (appendVal == null) {
      append = false;
    } else {
      if (appendVal.equalsIgnoreCase("true") || appendVal.equals("1")) {
        append = true;
      } else if (appendVal.equalsIgnoreCase("false")
          || appendVal.equals("0")) {
        append = false;
      }
    }

    // 获得level
    String levelVal = manager.getProperty(cname + ".level");
    if (levelVal == null) {
      setLevel(Level.ALL);
    } else {
      try {
        setLevel(Level.parse(levelVal.trim()));
      } catch (Exception ex) {
        setLevel(Level.ALL);
      }
    }

    // 获得dateinterval
    String dateintervalVal = manager.getProperty(cname + ".dateline");
    if (dateintervalVal == null) {
      dateline = 5;
    } else {
      try {
        dateline = Integer.parseInt(dateintervalVal.trim());
        if (dateline <= 0) {
          dateline = 5;
        }
      } catch (Exception ex) {
        dateline = 5;
      }
    }

    // 获得filter
    String filterVal = manager.getProperty(cname + ".filter");
    if (filterVal == null) {
      setFilter(null);
    } else {
      try {
        Class clz = ClassLoader.getSystemClassLoader().loadClass(
            filterVal);
        setFilter((Filter) clz.newInstance());
      } catch (Exception ex) {
        // We got one of a variety of exceptions in creating the
        // class or creating an instance.
        // Drop through.
      }
    }

    // 获得encoding
    String encodingVal = manager.getProperty(cname + ".encoding");
    if (encodingVal == null) {
      try {
        setEncoding(null);
      } catch (Exception ex2) {
        // doing a setEncoding with null should always work.
        // assert false;
      }
    } else {
      try {
        setEncoding(encodingVal);
      } catch (Exception ex) {
        try {
          setEncoding(null);
        } catch (Exception ex2) {
          // doing a setEncoding with null should always work.
          // assert false;
        }
      }
    }
  }

  /**
   * 将离现在最近的文件作为写入文件的文件 例如 xunleidemo_2008-02-19#30.log
   * xunleidemo表示自定义的日志文件名，2008-02-19表示日志文件的生成日期，30 表示此日期的第30个日志文件
   */
  private void openLastFile(boolean append) {
    try {
      super.close();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String today = sdf.format(new Date());

      // 如果没有包含当天的日期，则添加当天日期的日志文件
      boolean isFirstLogToday = false;
      if (!files.containsKey(today)) {
        String logIndex = today + splitDateIndexChar + 1;
        TreeSet<XLLogFile> todayFiles = new TreeSet<XLLogFile>();
        todayFiles.add(getNewFile(logIndex));
        files.put(today, todayFiles);
        isFirstLogToday = true;
      }

      // 获得今天最大的日志文件编号
      XLLogFile todayLastFile = files.get(today).last();
      int maxLogCount = todayLastFile.getSid();
      String logIndex = today + splitDateIndexChar
          + (maxLogCount + (isFirstLogToday ? 0 : (append ? 0 : 1)));
      XLLogFile wantWriteFile = getNewFile(logIndex);
      files.get(today).add(wantWriteFile);
      openFile(wantWriteFile, append);
    } catch (Exception ex) {
      Logger.getLogger(XLFileStreamHandler.class.getName()).log(
          Level.SEVERE, null, ex);
    }
  }

  /**
   * 根据logIndex要建立File
   *
   * @param logIndex
   *            包含今天日期及编号,如2008-09-11#1
   * @return File
   */
  private XLLogFile getNewFile(String logIndex) {
    File file = new File(pattern);
    XLLogFile wantWriteFile;
    StringBuilder filePath = new StringBuilder(pattern);
    if (file.isDirectory()) {
      filePath.append(File.separator);
      filePath.append(undifine);

    }
    filePath.append('_');
    filePath.append(logIndex);
    filePath.append(".log");
    wantWriteFile = new XLLogFile(filePath.toString());
    return wantWriteFile;
  }

  /**
   * 读取已经记录的日志的时间信息
   */
  private Map<String, TreeSet<XLLogFile>> getRecodedLog() {
    Map<String, TreeSet<XLLogFile>> filesMap = new HashMap<String, TreeSet<XLLogFile>>();
    try {
      // 建立相关目录
      File file = new File(pattern);
      File fileDir = null;
      if (pattern.endsWith("/") || pattern.endsWith("\\")) {
        // 是个目录
        fileDir = file;
        if (!file.exists()) {
          file.mkdirs();
        }
      } else {
        // 带了前缀
        File parentFile = new File(file.getParent());
        fileDir = parentFile;
        // 父目录不存在则新建目录
        if (!parentFile.exists()) {
          parentFile.mkdirs();
        }
      }

      // 加入到filesMap中
      for (File contentFile : fileDir.listFiles()) {
        if (contentFile.isFile()) {
          XLLogFile newXLLogFile = new XLLogFile(contentFile
              .getAbsolutePath());
          TreeSet<XLLogFile> fileListToDate = filesMap
              .get(newXLLogFile.getDateString());
          if (fileListToDate == null) {
            fileListToDate = new TreeSet<XLLogFile>();
          }
          fileListToDate.add(newXLLogFile);
          filesMap.put(newXLLogFile.getDateString(), fileListToDate);
        }
      }

      files = filesMap;
      return filesMap;
    } catch (Exception ex) {
      Logger.getLogger(XLFileStreamHandler.class.getName()).log(
          Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * 打开需要写入的文件
   *
   * @param file
   *            需要打开的文件
   * @param append
   *            是否将内容添加到文件末尾
   */
  private void openFile(File file, boolean append) throws Exception {
    int len = 0;
    if (append) {
      len = (int) file.length();
    }
    FileOutputStream fout = new FileOutputStream(file.toString(), append);
    BufferedOutputStream bout = new BufferedOutputStream(fout);
    msOut = new MeteredStream(bout, len);
    setOutputStream(msOut);
  }

  /**
   * 获得将要写入的文件
   */
  private synchronized void openWriteFiles() throws Exception {
    if (!getLevel().equals(Level.OFF)) {
      getRecodedLog();
      deleteExpiredLog();
      openLastFile(append);
    }
  }

  /**
   * 发布日志信息
   */
  @Override
  public synchronized void publish(LogRecord record) {
    super.publish(record);
    super.flush();
    if (getLevel().equals(Level.OFF)) {
      return;
    }
    if (limit > 0 && msOut.written >= limit) {
      openLastFile(false);
    }
  }

  public static void main(String[] args) {
    
    
  }
}
