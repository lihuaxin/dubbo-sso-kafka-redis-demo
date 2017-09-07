package tech.lihx.demo.core.web.appversion.download;


/**
 * 断点下载测试
 * <p>
 * 
 * @author LHX
 * @date 2015年4月29日
 * @version 1.0.0
 */
public class TestDownload {

	public static void main( String[] args ) {
		try {
			/*DownFileInfo bean = new DownFileInfo("http://dldir1.qq.com/qqfile/qq/QQ6.1/11905/QQ6.1.exe",
						"e:\\temp", "QQ6.1.exe", 5, true, null);
			 */

			DownFileInfo bean = new DownFileInfo(
					"http://app.yunxiaoyuan.net/api/m/version/download.html?type=apk&name=yxy&version=1.0&token=jtXWzaBoPxRT87YrEeqgnAvnU8aJJaAwdHbElkDgFsA.&sn=d885af77ecf4f7d3803b64531c7a3423",
					"c:/temp", "abc.apk", 5, true, null);

			/*File file = new File("c:/test_1.0.apk");
			DownFileInfo bean = new DownFileInfo(null, "c:/temp",
			        "test_1.0.apk", 3,false,file);*/

			DownFileFetch fileFetch = new DownFileFetch(bean);
			fileFetch.addListener(new DownListener() {

				@Override
				public void success() {
					System.out.println("client end");
				}
			});
			fileFetch.start();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
