import java.util.Arrays;
import java.util.List;

public class Constants {

	/**
	 * 文件类型
	 */
	public static final String FILE_TYPE_ZIP = ".zip";
	public static final String FILE_TYPE_DOC = ".doc";
	public static final String FILE_TYPE_DOCX = ".docx";
	public static final String FILE_TYPE_RTF = ".rtf";
	public static final String FILE_TYPE_TXT = ".txt";
	public static final String FILE_TYPE_HTML = ".html";
	public static final String FILE_TYPE_UNKNOWN = ".unknown";

	/**
	 * 文件路径
	 */
	// 原始文件路径
	public static final String FILE_PATH_ORIGINAL = "file.path.original";
	// 备份文件路径
	public static final String FILE_PATH_BACKUP = "file.path.backup";
	// 处理中文件路径
	public static final String FILE_PATH_PROCESSING = "file.path.processing";
	// 处理后文件路径
	public static final String FILE_PATH_PROCESSED = "file.path.processed";
	// 处理失败的文件路径
	public static final String FILE_PATH_PROCESSED_ERROR = "file.path.processed.error";
	// Txt文件路径
	public static final String FILE_PATH_PROCESSED_TXT = "file.path.processed.txt";
	// Html文件路径
	public static final String FILE_PATH_PROCESSED_HTML = "file.path.processed.html";
	// 解析后Txt文件路径
	public static final String FILE_PATH_PARSED_TXT = "file.path.parsed.txt";

	/**
	 * 数据差异类型
	 */
	// 错误（通达海数据错误）
	public static final String DIFFERENCE_TYPE_TDH_ERROR = "通达海错误";
	// 错误（文书中数据错误）
	public static final String DIFFERENCE_TYPE_WRIT_ERROR = "文书错误";
	// 错误（通达海，文书中数据均错误）
	public static final String DIFFERENCE_TYPE_BOTH_ERROR = "二者错误";

	// 二者相同
	public static final String DIFFERENCE_TYPE_SAME = "二者相同";
	// 二者不同
	public static final String DIFFERENCE_TYPE_DIFF = "二者不同";

	// 遗漏（通达海中无数据）
	public static final String DIFFERENCE_TYPE_TDH_OMIT = "通达海遗漏";
	// 补充（文书中无数据）
	public static final String DIFFERENCE_TYPE_WRIT_OMIT = "文书遗漏";
	// 遗漏（二者都无数据）
	public static final String DIFFERENCE_TYPE_BOTH_OMIT = "二者遗漏";


	/**
	 * 当事人类型
	 */
	// 被告
	public static final String PARTY_TYPE_DEFENDANT = "0";
	// 原告
	public static final String PARTY_TYPE_PLAINTIFF = "1";
	// 法官
	public static final String PARTY_TYPE_JUDGEMENT = "2";
	// 律师
	public static final String PARTY_TYPE_LAWYER = "3";
	// 证人
	public static final String PARTY_TYPE_WITNESS = "4";
	// 被害人
	public static final String PARTY_TYPE_VICTIM = "5";
	// 自然代理人
	public static final String PARTY_TYPE_PROXY_NATURAL = "6";
	// 法定代理人
	public static final String PARTY_TYPE_PROXY_STATUTORY = "7";

	/**
	 * 原告和被告的类型（个人或公司）
	 */
	// 个人
	public static final int ENTITY_TYPE_PERSON = 0;
	// 公司
	public static final int ENTITY_TYPE_COMPANY = 1;

	/**
	 * 地址解析
	 */
	// 中国地域信息
	public static final String CHINA_AREA_DATA = "china.area.data";
	// 安徽法院信息
	public static final String COURT_ANHUI_DATA = "court.anhui.data";
	// 冲突地域信息
	public static final String CHINA_CONFLICT_NAME_DATA = "china.conflict.name.data";

	/**
	 * 案件解析
	 */
	// 刑事案件案由
	public static final String CRIMINAL_CAUSE_DATA = "criminal.cause.data";
	// 职业
	public static final String PROFESSION_DATA = "profession.data";
	// 罪名
	public static final String CHARGE_DATA = "charge.data";

	//职业配置地址
	public static final String PROFESSION_RULES = "profession.rules";
	// 职业
	public static final String PROFESSION_SYNONYMS = "profession.synonyms";
	// 罪名
	public static final String PROFESSION_TOTDHMAPPER = "profession.ToTDHmapper";
	// 一般概括性单位关键词
	public static final String PROFESSION_GENERAL_SECTOR_WORDS="profession.generalSectorWords";

	//姓名分类：人名或者机构
	public static final String NAME_ORG_SUFFIX="name.org.suffix";
	// 汉字，拼音映射表
	public static final String NAME_HANZI_PINYIN_MAP="name.hanzi.pinyin.map";

	/**
	 * 地址类型
	 */
	// 户籍地址
	public static final String ADDRESS_TYPE_DOMICILE = "0";
	// 现住地址
	public static final String ADDRESS_TYPE_CURRENT = "1";

	/**
	 * 地址中替换掉的特定字符
	 */
	public static final String ADDRESS_DISTRICT_NAME = "（户籍地）省市区县乡镇村组街道楼单元室号";
	public static final char[] ADDRESS_DISTRICT_CHINESE_NUM = { '一', '二', '三', '四', '五', '六', '七', '八', '九', '零' };
	public static final char[] ADDRESS_DISTRICT_ARABIC_NUM = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	/**
	 * 数据库schemas
	 */
	public static final String SCHEMA_MYSQL_PARSE = "court_parse";
	public static final String SCHEMA_MYSQL_STD = "court_std";
	public static final String SCHEMA_MYSQL_MERGE = "court_merge";

	/**
	 * 标准化对象（最多30个，只允许追加），映射为表`kr_writ_case_info`中standardized值。
	 */
	public static final List<String> STANDARDIZED_TARGETS = Arrays.asList(
			"被告", "原告", "律师", "第三人", "被害人", "证人","法官", "前科"
	);

	/**
	 * 标准化映射
	 */
	public static final String EDUCATION_MAPPER = "education.mapper";
	public static final String NATION_MAPPER = "nation.mapper";
}
