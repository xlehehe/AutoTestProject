package com.zzg.robotium.lib;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 * 测试报告工具类
 *
 * @author zzg
 */
public class ReportLib extends Formatter {

    /**
     * case名
     */
    private static String mLogName;
    public static final String TAG = "ROBOT";
    private int i = 0;
    private long setStartTime;
    private long setEndTime;
    private static int p_pass = 0;
    private static int p_fail = 0;
    private static int p_nt = 0;
    private static String result = "";
    public final static String pass = "Pass";
    public final static String success = "Success";
    public final static String fail = "Fail";
    public final static String exception = "Exception";
    private static Object expected = "";
    private static Object actual = "";
    private static String imageName = null;
    private static FileHandler fileHTML;
    private static Formatter formatterHTML;
    private static String screenShot;

    private static Logger logger = Logger.getLogger(ReportLib.class.getName());

    InputDataStore inputDataStore;

    private static String crash_file = "";
    /**
     * 文件名
     */
    private static String fileName;

    static String HTML_HEADER = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
            + "<META HTTP-EQUIV=\"CACHE-CONTROL\" CONTENT=\"NO-CACHE\">"
            + "<META HTTP-EQUIV=\"PRAGMA\" CONTENT=\"NO-CACHE\">"
            + "<link rel=\"stylesheet\" title=\"TDriverReportStyle\" href=\"demo_report_style.css\"/>"
            + "<html><head><style>\n" +
            "        a,a:hover{ text-decoration: none;}\n" +
            "        .index{ padding: 0 5%;}\n" +
            "        .table{ width: 100%; text-align:left; border: 1px solid #ddd; border-collapse: collapse;  border-spacing: 0;}\n" +
            "        .table tr{ height: 38px;}\n" +
            "        .table tr th{ background: #eeeeee; border: 1px solid #ddd;padding-left: 2px;}\n" +
            "        .table tr td{ border: 1px solid #ddd; padding-left: 2px;}\n" +
            "        .summary {width: 30%; text-align:center; border: 1px solid #ddd; border-collapse: collapse;  border-spacing: 0; margin-top: 20px;}\n" +
            "        .summary tr{ height: 30px;}\n" +
            "        .summary tr:nth-child(even){ background-color: #ebcccc;}\n" +
            "        .summary tr td { border: 1px solid #ddd;}\n" +
            "        a{ color: #5bc0de; }\n" +
            "        a:hover{ color: #1b6d85;}\n" +
            "        #layer{ background: #000000; opacity:0.5; width:100%; height:100%; z-index: 2; position: absolute; top:0; display: none;}\n" +
            "        #pic1,#pic2 { width: 360px; height: 616px; position: absolute; top: 20px; z-index: 3; left: 0px; display: none;}\n" +
            "        #btn { width: 30px; height: 30px; position: absolute; display: none; top: 0; left: 0; z-index: 4; }\n" +
            "        #fail,#crash{ cursor: pointer;}\n" +
            "    #bg{ width: 100%; height: 100%; position: fixed;background: black; opacity: 0.6; z-index: 20; top:0; left: 0; display: none;}\n" +
            "    #bg-con { position: fixed; top: 70px;\n" +
            "                left: 10%;\n" +
            "                width: 80%;\n" +
            "                height: 350px;overflow:auto; z-index: 21; background: #fff; border: 1px solid #ddd; padding: 40px 30px 0; display:none; color:#000;box-sizing: border-box;}" +
            "    #bg_log{ width: 100%; height: 100%; position: fixed;background: black; opacity: 0.6; z-index: 20; top:0; left: 0; display: none;}\n" +
            "    #bg_con_log { position: fixed; top: 70px;\n" +
            "                left: 10%;\n" +
            "                width: 80%;\n" +
            "                height: 350px; overflow:auto; z-index: 21; background: #fff; border: 1px solid #ddd; padding: 40px 30px 0; display:none; color:#000;box-sizing: border-box;}" +
            "</style><title>Robotium测试报告</title></head>"
            + "<body height：100%>"
            + "<div class=\"index\">"
            + "<div class=\"page_title\"><center>"
            + "<h1 id=\"log\"><a>Robotium测试报告</a></h1></center></div>"
            + "<table class=\"table table-bordered table-hover\"><tr>"
            + "<th><b>序号</b></th>"
            + "<th><b>步骤描述</b></th>"
            + "<th><b>期待结果</b></th>"
            + "<th><b>实际结果</b></th>"
            + "<th><b>执行时间</b></th>"
            + "<th><b>状态</b></th>" + "</tr>";

    private String crash_log;
//
    Thread recordLogcatThread;

    private static String mLogcat;
    private static String logcat;

    public boolean setup(String p_logName) {
        inputDataStore = InputDataStore.getInstance();
        mLogName = p_logName;
        mLogcat = mLogName + "_" + CommonLib.getCurrentTime();
        fileName = mLogcat + ".html";
//        File dataDirectory = new File(inputDataStore.getInput_LogPath() + "log/");
//        if (dataDirectory != null) {
//            if (!dataDirectory.exists()) {
//                if (!dataDirectory.mkdirs()) {
//                    dataDirectory = null;
//                }
//            }
//            try {
//                new File(dataDirectory, mLogcat + ".log").createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(TAG, "创建文件" + mLogcat + "失败");
//            }
//        }
        setFolder(inputDataStore.getInput_LogPath());
        try {
            logger.setLevel(Level.INFO);
            fileHTML = new FileHandler(inputDataStore.getInput_LogPath() + fileName);
            formatterHTML = new ReportLib();
            fileHTML.setFormatter(formatterHTML);
            logger.addHandler(fileHTML);
            Log.i(TAG, "Create file" + inputDataStore.getInput_LogPath() + fileName + "Successful!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Create file" + inputDataStore.getInput_LogPath() + fileName + "Unsuccessful! Please check your permission" + e.toString());
            return false;
        }
    }

    private static void clearLogcat(){
        try {
            ArrayList commandLine = new ArrayList();
            commandLine.add("logcat");
            commandLine.add("-c");//使用该参数可以让logcat获取日志完毕后终止进程
            Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String recordLogcat(String logcatName) {
        StringBuilder log=new StringBuilder();
        try {
            ArrayList commandLine = new ArrayList();
            commandLine.add("logcat");
            commandLine.add("-d");//使用该参数可以让logcat获取日志完毕后终止进程
            commandLine.add("-v");
            commandLine.add("time");

            Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("</br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log.toString();
    }

    public static void setImageName(String name) {
        imageName = name;
    }

    public String setFolder(String directoryName) {
        String reportPath = directoryName + "images";
        File dataDirectory = new File(reportPath);
        if (dataDirectory != null) {
            if (!dataDirectory.exists()) {
                if (!dataDirectory.mkdirs()) {
                    dataDirectory = null;
                }
            }
        }
        return dataDirectory.toString();
    }

    public void closeLog() {
        logcat = recordLogcat(mLogcat);
        clearLogcat();
        Log.e(TAG,"logcat==="+logcat);
        fileHTML.close();
        p_pass = 0;
        p_fail = 0;
        p_nt = 0;
        result = "";
        imageName = "";
        expected = "";
        actual = "";
        screenShot = "";
    }

    public static void logWriter(String p_info, Object p_expected,
                                 Object p_actual, String p_result) {
        result = p_result;
        actual = p_actual;
        expected = p_expected;
        try {
            logger.info(p_info);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" logger write exception!");
        }
    }

    public String format(LogRecord rec) {
        StringBuffer buf = new StringBuffer(1000);
        buf.append("<div class=\"statistics\">");
        buf.append("<tr>");
        buf.append("<td>");
        buf.append(recordStep());
        buf.append("</td>");
        buf.append("<td>");
        buf.append(formatMessage(rec));
        buf.append('\n');
        buf.append("</td>");
        buf.append("<td>");
        buf.append(expected);
        buf.append("</td>");
        if (result.matches(exception)) {
            crash_log = actual.toString();
            buf.append("<td id=\"crash\">");
            buf.append("<b>");
            buf.append("<font color=Red>");
            buf.append("异常日志");
            buf.append("</font>");
            buf.append("</b>");
        } else {
            buf.append("<td>");
            buf.append(actual);
        }

        buf.append("</td>");
        buf.append("<td>");
        buf.append(getCalcDate(rec.getMillis()));
        buf.append("</td>");

        buf.append("<td>");

        if (result.matches(pass)) {
            p_pass = p_pass + 1;
            buf.append("<b>");
            buf.append("<font color=Green>");
            buf.append("成功");
            buf.append("</font>");
            buf.append("</b>");
        } else if (result.matches(fail)) {
            p_fail = p_fail + 1;
            buf.append("<a href=\"images/" + imageName + "\">");
            buf.append("<b>");
            buf.append("<font color=Red>");
            buf.append("失败");
            buf.append("</font>");
            buf.append("</b>");
            buf.append("</a>");
        } else if (result.matches(exception)) {
            p_nt = p_nt + 1;
            buf.append("<a href=\"images/" + imageName + "\">");
            buf.append("<b>");
            buf.append("<font color=Red>");
            buf.append("异常");
            buf.append("</font>");
            buf.append("</b>");
            buf.append("</a>");
        }
        buf.append("</td>");
        buf.append("</tr>");
        buf.append("</div>\n");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buf.toString();
    }


    private int recordStep() {
        i = i + 1;
        return i;
    }

    @SuppressLint("SimpleDateFormat")
    private String getCalcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    private String getDeltaTime(long p_startTime, long p_endTime) {
        long day = (p_endTime - p_startTime) / (24 * 60 * 60 * 1000);
        long hour = ((p_endTime - p_startTime) / (60 * 60 * 1000) - day * 24);
        long min = (((p_endTime - p_startTime) / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = ((p_endTime - p_startTime) / 1000 - day * 24 * 60 * 60 - hour
                * 60 * 60 - min * 60);

        return day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    @Override
    public String getHead(Handler h) {
        String html_header = HTML_HEADER;
        try {
            html_header = HTML_HEADER.replace("Robotium", mLogName).replace("logcat", mLogcat);
            setStartTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html_header;
    }



    @Override
    public String getTail(Handler h) {
        Log.e(TAG, "getTail被调用了");

        setEndTime = System.currentTimeMillis();
        String startTime = getCalcDate(setStartTime);
        String endTime = getCalcDate(setEndTime);
        String runTime = getDeltaTime(setEndTime, setStartTime);
        int p_total = p_pass + p_fail + p_nt;
        String HTML_Tail;
        if (p_total > 0) {
            if (p_fail > 0) {
                Report.logWriter(mLogName, fileName, startTime, endTime, runTime, p_total, Report.fail);
            } else if (p_nt > 0) {
                Report.logWriter(mLogName, fileName, startTime, endTime, runTime, p_total, Report.exception);
            } else {
                Report.logWriter(mLogName, fileName, startTime, endTime, runTime, p_total, Report.pass);
            }
            HTML_Tail = "</table></PRE>"
                    + "<br>&nbsp;开始时间  ：" + startTime
                    + "<br>&nbsp;结束时间  ：" + endTime
                    + "<br>&nbsp;运行时间  ：" + runTime
                    + "<br>&nbsp;执行步骤  ：" + p_total
                    + "</div>" +
                    "<div id=\"bg\"></div><div id=\"bg-con\"><p>" + crash_log + "</p></div>" +
                    "<div id=\"bg_log\"></div><div id=\"bg_con_log\"><p>" + logcat + "</p></div>";
            if (p_nt > 0) {
                HTML_Tail += "<script>\n" +
                        "        document.getElementById('crash').onclick = function () {\n" +
                        "            document.getElementById('bg').style.display = 'block';\n" +
                        "            document.getElementById('bg-con').style.display = 'block';\n" +
                        "        };\n" +
                        "document.getElementById('bg').onclick = function(){\n" +
                        "        document.getElementById('bg').style.display = 'none';\n" +
                        "        document.getElementById('bg-con').style.display = 'none';\n" +
                        "    };" +
                        "</script>"
                        ;
            }
                HTML_Tail += "<script>\n" +
                        "        document.getElementById('log').onclick = function () {\n" +
                        "            document.getElementById('bg_log').style.display = 'block';\n" +
                        "            document.getElementById('bg_con_log').style.display = 'block';\n" +
                        "        };\n" +
                        "document.getElementById('bg_log').onclick = function(){\n" +
                        "        document.getElementById('bg_log').style.display = 'none';\n" +
                        "        document.getElementById('bg_con_log').style.display = 'none';\n" +
                        "    };" +
                        "</script>"+"</BODY></HTML>";

        } else {
            HTML_Tail = "</table></PRE>" + "<br>&nbsp;用例执行异常！" + "<br><br>"
                    + "</div>"
                    + "</BODY></HTML>";
        }
        return HTML_Tail;
    }

}