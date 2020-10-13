package com.junzixiehui.doraon.code.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/19  10:43
 * @version: 1.0
 */
@Slf4j
public class EntityCodeGenerateUtils {

	private String[] colnames; // 列名数组
	private String[] colTypes; //列名类型数组
	private int[] colSizes; //列名大小数组
	private boolean f_util = false; // 是否需要导入包java.util.*
	private boolean f_sql = false; // 是否需要导入包java.sql.*
	private String tablename;
	private String URL = "";
	private String NAME = "";
	private String PASSWORD = "";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private String dbName = "";

	private static Connection connection;

	public EntityCodeGenerateUtils(String url, String name, String password, String dbName) {
		this.URL = url;
		this.NAME = name;
		this.PASSWORD = password;
		this.dbName = dbName;

		connection = getConnection();
	}

	public static void main(String[] args) {

		String packageOutPath = "com.xxx.xxx.sys.domain.entity";//指定实体生成所在包的路径
		String suffix = "Entity";
		generateEntityCode("t_start_warehouse", packageOutPath, suffix);
	}

	private Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL + "/" + dbName, NAME, PASSWORD);
		} catch (Exception e) {
			log.error("get connection failure", e);
		}
		return conn;
	}

	public static void generateEntityCode(String tableName, String packageOutPath, String suffix) {
		List<String> tableList = new ArrayList<String>();

		try {
			DatabaseMetaData dm = connection.getMetaData();
			ResultSet rs = dm.getTables(connection.getCatalog(), "root", null, new String[]{"TABLE"});
			while (rs.next()) {
				tableList.add(rs.getString("TABLE_NAME"));
			}
			if (StringUtils.isNotBlank(tableName)) {
				new EntityCodeGenerateUtils(connection, tableName, packageOutPath, suffix);
				return;
			}

			if (CollectionUtils.isNotEmpty(tableList)) {
				for (String table : tableList) {
					new EntityCodeGenerateUtils(connection, table, packageOutPath, suffix);
				}
			}
		} catch (Exception e1) {
			log.error("", e1);
		}
	}

	List<String> columnComments = new ArrayList<>();//列名注释集合

	public EntityCodeGenerateUtils(Connection con, String tableName, String packageOutPath, String suffix) {
		this.tablename = tableName;
		String sql = "select * from " + tablename;
		PreparedStatement pStemt;
		try {

			pStemt = con.prepareStatement(sql);
			ResultSetMetaData rsmd = pStemt.getMetaData();

			ResultSet rs = null;
			try {
				rs = pStemt.executeQuery("show full columns from " + tableName);
				while (rs.next()) {
					columnComments.add(rs.getString("Comment"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int size = rsmd.getColumnCount();
			colnames = new String[size];
			colTypes = new String[size];
			colSizes = new int[size];
			for (int i = 0; i < size; i++) {
				colnames[i] = rsmd.getColumnName(i + 1);
				colTypes[i] = rsmd.getColumnTypeName(i + 1);

				if (colTypes[i].equalsIgnoreCase("datetime")) {
					f_util = true;
				}
				if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")) {
					f_sql = true;
				}
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
			}

			String content = parse(packageOutPath, suffix);

			try {
				File directory = new File("");
				String outputPath =
						directory.getAbsolutePath() + "/src/main/java/" + packageOutPath.replace(".", "/") + "/"
								+ parseName(tablename, suffix) + ".java";
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 功能：生成实体类主体代码
	 * @return
	 */
	private String parse(String packageOutPath, String suffix) {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + packageOutPath + ";\r\n");
		sb.append("\r\n");
		//判断是否导入工具包
		if (f_util) {
			sb.append("import java.util.Date;\r\n");
		}
		if (f_sql) {
			sb.append("import java.sql.*;\r\n");
		}
		sb.append("import com.zhouyutong.zorm.dao.jdbc.annotation.Column;\r\n");
		sb.append("import com.zhouyutong.zorm.dao.jdbc.annotation.Table;\r\n");
		sb.append("import lombok.Data;\r\n");
		sb.append("import com.zhouyutong.zorm.annotation.PK;\r\n");
		sb.append("import com.zhouyutong.zorm.entity.IdEntity;\r\n");
		sb.append("\r\n");
		//注释部分
		//sb.append("/**\r\n");
		//sb.append(" *@author: " +  this.authorName  + " \r\n");
		//sb.append(" *@date: " + DateUtils.format(new Date(),DateUtils.FORMAT_YYYY_MM_DD) +  "\r\n");
		//sb.append(" */ \r\n");
		//实体部分
		sb.append("@Data\r\n");
		sb.append("@ApiModel\r\n");
		sb.append("@Table(value = \"" + tablename + "\")\r\n");
		sb.append("public class " + parseName(tablename, suffix) + " implements IdEntity {\r\n");
		processAllAttrs(sb);//属性
		sb.append("}\r\n");
		return sb.toString();
	}

	/**
	 * 功能：生成所有属性
	 * @param sb
	 */
	private void processAllAttrs(StringBuffer sb) {

		for (int i = 0; i < colnames.length; i++) {
			String column = parseNameNotFirst(colnames[i]);
			if (column.equals("fileSize")) {
				System.out.println("here");
			}
			if (i == 0) {
				sb.append("\r\n");
			}
			if ("id".equals(column)) {
				sb.append("\t@PK\r\n");
				sb.append("\t@Column(value = \"" + colnames[i] + "\")\r\n");
			} else {
				sb.append("\t@Column(value = \"" + colnames[i] + "\")\r\n");
				sb.append("\t@ApiModelProperty(value = \"" + columnComments.get(i) + "\")\r\n");
			}
			sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + column + ";\r\n");
		}
	}

	private static final String SQL = "SELECT * FROM ";// 数据库操作

	public static List<String> getColumnComments(String tableName) {
		List<String> columnTypes = new ArrayList<>();
		//与数据库的连接
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		List<String> columnComments = new ArrayList<>();//列名注释集合
		ResultSet rs = null;
		try {
			pStemt = connection.prepareStatement(tableSql);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				columnComments.add(rs.getString("Comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		}
		return columnComments;
	}

	/**
	 * 描述：第一个首字母不大写，后面大写
	 * @param name
	 * @return
	 * String
	 */
	public String parseNameNotFirst(String name) {
		StringBuffer sb = new StringBuffer();
		String[] array = name.split("_");
		if (array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				if (i > 0) {
					sb.append(initcap(array[i]));
				} else {
					sb.append(array[i]);
				}
			}
		}

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

	/**
	 * 功能：获得列的数据类型
	 * @param sqlType
	 * @return
	 */
	private String sqlType2JavaType(String sqlType) {
		sqlType = sqlType.toLowerCase();

		if (sqlType.equalsIgnoreCase("bit")) {
			return "Boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint") || sqlType.equalsIgnoreCase("TINYINT UNSIGNED")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("smallint") || sqlType.equalsIgnoreCase("SMALLINT UNSIGNED")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("INT UNSIGNED")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("bigint") || sqlType.equalsIgnoreCase("BIGINT UNSIGNED")) {
			return "Long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "Float";
		} else if (sqlType.equalsIgnoreCase("decimal") || sqlType.startsWith("decimal") || sqlType.startsWith("numeric")
				|| sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real") || sqlType
				.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType
				.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") || sqlType
				.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		}

		return null;
	}
}
