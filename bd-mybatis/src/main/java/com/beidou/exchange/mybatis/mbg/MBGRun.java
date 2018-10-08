package com.beidou.exchange.mybatis.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

public class MBGRun {
	public static void main(String[] args) throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		/**
		 * 初始化配置解析器
		 */
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(MBGRun.class.getResourceAsStream("generatorConfig.xml"));
		/**
		 * shellcallback接口主要用来处理文件的创建和合并，传入overwrite参数；默认的shellcallback是不支持文件合并的；
		 */
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		/**
		 * 创建一个MyBatisGenerator对象。MyBatisGenerator类是真正用来执行生成动作的类
		 */
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		/**
		 * -verbose 用System.out打印执行过程
		 *
		 */
		ProgressCallback progressCallback = new VerboseProgressCallback();
		/**
		 *  执行生成操作
		 */
		myBatisGenerator.generate(progressCallback);
		//myBatisGenerator.generate(null);
		System.out.println("ok");
	}
}