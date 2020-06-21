package per.msm.log.handler;

import per.msm.log.LogConfig;
import per.msm.log.factory.InputFormatter;
import per.msm.log.factory.LogFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * 自定义日志文件处理器---日期及文件大小存储，并自动清除过期日志文件
 * time: 2020/6/19 18:19
 *
 * @author msm
 */
public class FileStreamHandler extends StreamHandler {
  /**
   * 追加日志
   */
  private final boolean append;
  /**
   * 保存几天之内的日志文件
   * 时间间隔小于等于0时表明不删除历史记录
   */

  private final int dateline;
  /**
   * 保存存在的日志文件
   */
  private Map<String, TreeSet<LogFile>> files;
  /**
   * 每个日志希望写入的最大字节数，如果日志达到最大字节数则当天日期的一个新的编号的日志文件将被创建，最新的日志记录在最大编号的文件中
   * 文件大小为小于等于0时表明不限制日志文件大小
   */
  private final int limit;
  /**
   * 输出流
   */
  private MeteredStream msOut;
  /**
   * 指定日志名称时不需要包括日期，程序会自动生成日志文件的生成日期及相应的编号
   */
  private final String pattern;
  /**
   * 分割符
   */
  private final char splitDateIndexChar = '#';
  /**
   * 当天时间
   */
  private String today = LocalDate.now().toString();
  /**
   * 格式化日器
   */
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * 初始化自定义文件流处理器
   *
   * @param fileUrl  文件路径， 可以是个目录或希望的日志名称
   *                 指定日志名称时不需要包括日期，程序会自动生成日志文件的生成日期及相应的编号
   * @param limit    每个日志希望写入的最大字节数，如果日志达到最大字节数则当天日期的一个新的编
   *                 号的日志文件将被创建，最新的日志记录在最大编号的文件中
   * @param dateline 保存几天之内的日志文件
   * @param append   是否将日志写入已存在的日志文件中
   */
  public FileStreamHandler(String fileUrl, int limit, int dateline,
                           boolean append) {
    super();
    this.pattern = fileUrl;
    this.limit = limit;
    this.dateline = dateline;
    this.append = append;
    setFormatter(new InputFormatter());
    openWriteFiles();
  }

  /**
   * 检查当前日志时间,删除过期日志
   */
  private void deleteExpiredLog() {
    try {
      if (dateline < 1) {
        return;
      }
      // 今天作为基准
      LocalDate today = LocalDate.now();
      // 删除过期日志
      String temp = "";
      for (String keyDate : files.keySet()) {
        LocalDate localDate = LocalDate.parse(keyDate);
        if (localDate.plusDays(dateline).isBefore(today)) {
          temp = keyDate;
          TreeSet<LogFile> traceDateFiles = files.get(keyDate);
          for (File deletingFile : traceDateFiles) {
            System.out.println(deletingFile.getAbsoluteFile());
            //noinspection ResultOfMethodCallIgnored
            deletingFile.delete();
          }
        }
      }
      if (!temp.isEmpty()) {
        files.remove(temp);
      }
    } catch (Exception ex) {
      Logger.getLogger(FileStreamHandler.class.getName()).log(
          Level.SEVERE, null, ex);
    }
  }

  /**
   * 将离现在最近的文件作为写入文件的文件
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
        TreeSet<LogFile> todayFiles = new TreeSet<>();
        todayFiles.add(getNewFile(logIndex));
        files.put(today, todayFiles);
        isFirstLogToday = true;
      }
      // 获得今天最大的日志文件编号
      LogFile todayLastFile = files.get(today).last();
      int maxLogCount = todayLastFile.getSid();
      String logIndex = today + splitDateIndexChar
          + (maxLogCount + (isFirstLogToday ? 0 : (append ? 0 : 1)));
      LogFile wantWriteFile = getNewFile(logIndex);
      files.get(today).add(wantWriteFile);
      openFile(wantWriteFile, append);
    } catch (Exception ex) {
      Logger.getLogger(FileStreamHandler.class.getName()).log(
          Level.SEVERE, null, ex);
    }
  }

  /**
   * 根据logIndex要建立File
   *
   * @param logIndex 包含今天日期及编号
   * @return File
   */
  private LogFile getNewFile(String logIndex) {
    File file = new File(pattern);
    LogFile wantWriteFile;
    StringBuilder filePath = new StringBuilder(pattern);
    if (file.isDirectory()) {
      filePath.append(File.separator);
      filePath.append(LogConfig.LOG_FILE_NAME);
    }
    filePath.append('_');
    filePath.append(logIndex);
    filePath.append(".log");
    wantWriteFile = new LogFile(filePath.toString());
    return wantWriteFile;
  }

  /**
   * 读取已经记录的日志的时间信息
   */
  private void getRecodedLog() {
    Map<String, TreeSet<LogFile>> filesMap = new HashMap<>(16);
    try {

      // 建立相关目录
      File fileDir = new File(pattern);
      // 加入到filesMap中
      for (File contentFile : Objects.requireNonNull(fileDir.listFiles())) {
        if (contentFile.isFile()) {
          LogFile newLogFile = new LogFile(contentFile
              .getAbsolutePath());
          TreeSet<LogFile> fileListToDate = filesMap
              .get(newLogFile.getDateString());
          if (fileListToDate == null) {
            fileListToDate = new TreeSet<>();
          }
          fileListToDate.add(newLogFile);
          filesMap.put(newLogFile.getDateString(), fileListToDate);
        }
      }
      files = filesMap;
    } catch (Exception ex) {
      Logger.getLogger(FileStreamHandler.class.getName()).log(
          Level.SEVERE, null, ex);
    }
  }

  /**
   * 打开需要写入的文件
   *
   * @param file   需要打开的文件
   * @param append 是否将内容添加到文件末尾
   */
  private void openFile(File file, boolean append) throws Exception {
    int len = 0;
    if (append) {
      len = (int) file.length();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file.toString(), append);

    BufferedOutputStream bout = new BufferedOutputStream(fileOutputStream);
    msOut = new MeteredStream(bout, len);
    setOutputStream(msOut);
  }

  /**
   * 获得将要写入的文件
   */
  private synchronized void openWriteFiles() {
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
      LogFactory.logThreadLocal.remove();
      openLastFile(false);
    } else {
      //当前时间
      String now = LocalDate.now().toString();
      if (!now.equals(today)) {
        today = now;
        openLastFile(false);
      }
    }
    //删除历史日志文件
    if (dateline >= 1) {
      String now = LocalDate.now().toString();
      if (!now.equals(today)) {
        deleteExpiredLog();
      }
    }
  }

  /**
   * 自定义日志文件
   */
  private class LogFile extends File {
    private static final long serialVersionUID = 952141123094287978L;
    private Date date;
    private String dateString;
    private int sid;

    public int getSid() {
      return this.sid;
    }

    @SuppressWarnings("unused")
    public void setSid(int sid) {
      this.sid = sid;
    }

    public Date getDate() {
      return this.date;
    }

    @SuppressWarnings("unused")
    public void setDate(Date date) {
      this.date = date;
    }

    public String getDateString() {
      return this.dateString;
    }

    @SuppressWarnings("unused")
    public void setDateString(String dateString) {
      this.dateString = dateString;
    }

    @Override
    public int compareTo(File another) {
      LogFile ano = (LogFile) another;
      int dateComResult = date.compareTo(ano.getDate());
      if (dateComResult == 0) {
        return sid - ano.getSid();
      }
      return dateComResult;
    }

    public LogFile(String pathname) {
      super(pathname);
      try {
        int dot = pathname.lastIndexOf('.');
        int split = pathname.lastIndexOf(splitDateIndexChar);
        int underline = pathname.lastIndexOf('_');
        dateString = pathname.substring(underline + 1, split);
        String numStr = pathname.substring(split + 1, dot);
        date = sdf.parse(dateString);
        sid = Integer.parseInt(numStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    public boolean delete() {
      return super.delete();
    }

    @Override
    public boolean equals(Object o) {
      return super.equals(o);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), date, dateString, sid, sdf);
    }
  }

  /**
   * 抄自FileHandler的实现，用于跟踪写入文件的字节数 这样以便提高效率
   */
  private static class MeteredStream extends OutputStream {

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
    public void write(byte[] buff) throws IOException {
      out.write(buff);
      written += buff.length;
    }

    @Override
    public void write(byte[] buff, int off, int len) throws IOException {
      out.write(buff, off, len);
      written += len;
    }

    @Override
    public void write(int b) throws IOException {
      out.write(b);
      written++;
    }
  }
}

