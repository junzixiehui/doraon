package com.junzixiehui.doraon.code.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2019/7/19  10:43
 * @version: 1.0
 */
@Slf4j
public class DaoCodeGenerateUtils {

	private String tablename;
	private String URL = "";
	private String NAME = "";
	private String PASSWORD = "";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private String dbName = "";

	private static Connection con;

	public DaoCodeGenerateUtils(String url, String name, String password, String dbName) {
		this.URL = url;
		this.NAME = name;
		this.PASSWORD = password;
		this.dbName = dbName;
		this.con = getConnection();
	}

	public static void main(String[] args) {

		String packageOutPath = "com.xxx.xxx.sys.dao";//指定实体生成所在包的路径
		String daoSuffix = "Dao";
		String entitySuffix = "Entity";
		generateEntityCode("t_sys_dict_item", packageOutPath, daoSuffix, entitySuffix, "xxxDbJdbcSettings");
	}

	public static void generateEntityCode(String tableName, String packageOutPath, String daoSuffix,
			String entitySuffix, String settingBeanName) {
		List<String> tableList = new ArrayList<String>();
		try {
			DatabaseMetaData dm = con.getMetaData();
			ResultSet rs = dm.getTables(con.getCatalog(), "root", null, new String[]{"TABLE"});
			while (rs.next()) {
				tableList.add(rs.getString("TABLE_NAME"));
			}

			if (StringUtils.isNotBlank(tableName)) {
				new DaoCodeGenerateUtils(con, tableName, packageOutPath, daoSuffix, entitySuffix, settingBeanName);
				return;
			}

			if (CollectionUtils.isNotEmpty(tableList)) {
				for (String table : tableList) {
					new DaoCodeGenerateUtils(con, table, packageOutPath, daoSuffix, entitySuffix, settingBeanName);
				}
			}
		} catch (Exception e1) {
			log.error("", e1);
		}
	}

	private  Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL + "/" + dbName, NAME, PASSWORD);
		} catch (Exception e) {
			log.error("get connection failure", e);
		}
		return conn;
	}

	public DaoCodeGenerateUtils(Connection con, String tableName, String packageOutPath, String daoSuffix,
			String entitySuffix, String settingBeanName) {

		tablename = tableName;
		try {
			String content = parse(packageOutPath, daoSuffix, entitySuffix, settingBeanName);
			try {
				File directory = new File("");
				String outputPath =
						directory.getAbsolutePath() + "/src/main/java/" + packageOutPath.replace(".", "/") + "/"
								+ parseName(tableName, daoSuffix) + ".java";
				if (directory.exists()) {
					return;
				}

				FileWriter fw = new FileWriter(outputPath);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(content);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parse(String packageOutPath, String daoSuffix, String entitySuffix, String settingBeanName) {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + packageOutPath + ";\r\n");
		sb.append("\r\n");

		sb.append("import com.zhouyutong.zorm.annotation.Dao;\r\n");
		sb.append("import com.zhouyutong.zorm.dao.jdbc.JdbcBaseDao;\r\n");
		sb.append("import org.springframework.stereotype.Repository;\r\n");

		sb.append("\r\n");
		//实体部分
		sb.append("@Repository\r\n");
		sb.append("@Dao(settingBeanName =  \"" + settingBeanName + "\")\r\n");
		sb.append("public class " + parseName(tablename, daoSuffix) + " extends JdbcBaseDao<" + parseName(tablename,
				entitySuffix) + "> {\r\n");

		sb.append("}\r\n");
		return sb.toString();
	}

	/**
	 * 描述：全部首字母大写
	 * @param name
	 * @return
	 * String
	 */
	public String parseName(String name, String suffix) {
		StringBuffer sb = new StringBuffer();
		String[] array = name.split("_");
		if (array.length > 0) {
			for (String string : array) {
				sb.append(initcap(string));
			}
		}

		return sb.toString().substring(1) + suffix;
	}

	/**
	 * 功能：将输入字符串的首字母改成大写（修改成驼峰法）
	 * @param str
	 * @return
	 */
	private String initcap(String str) {

		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}
}
