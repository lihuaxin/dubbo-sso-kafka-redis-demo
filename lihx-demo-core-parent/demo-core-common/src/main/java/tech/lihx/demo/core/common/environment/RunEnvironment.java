package tech.lihx.demo.core.common.environment;

import java.util.Arrays;

public interface RunEnvironment {

	/**
	 * @author bo 本地,开发,测试,产品
	 */
	public enum Environment {
		LOCAL(EnvironmentDetect.LOCAL, "本地环境", "LOCAL"), DEVELOP(EnvironmentDetect.DEVELOP, "开发环境", "DEVELOP"), TEST(
				EnvironmentDetect.TEST, "测试环境", "TEST"), PRODUCT(EnvironmentDetect.PRODUCT, "产品环境", "PRODUCT");

		String name;

		String code;

		byte[] value;


		private Environment( byte[] value, String name, String code ) {
			this.name = name;
			this.value = value;
			this.code = code;
		}


		public String getName() {
			return this.name;
		}


		public String getCode() {
			return this.code;
		}


		public byte[] getValue() {
			return this.value;
		}


		public boolean isLocal() {
			return Arrays.equals(EnvironmentDetect.LOCAL, this.value);
		}


		public boolean isTest() {
			return Arrays.equals(EnvironmentDetect.TEST, this.value);
		}


		public boolean isRelease() {
			return isTest() || isProduct();
		}


		public boolean isDevelop() {
			return Arrays.equals(EnvironmentDetect.DEVELOP, this.value);
		}


		public boolean isProduct() {
			return Arrays.equals(EnvironmentDetect.PRODUCT, this.value);
		}
	}


	public Environment getEnvironment();


	public RunConfig getRunConfig();
}
