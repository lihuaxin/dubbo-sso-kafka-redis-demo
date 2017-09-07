package tech.lihx.demo.core.web.appversion.download;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 文件下载
 * <p>
 * 
 * @author LHX
 * @date 2015年4月29日
 * @version 1.0.0
 */
public class DownFileFetch extends Thread {

	protected static Logger logger = LoggerFactory.getLogger(DownFileFetch.class);

	private DownListener downListener;

	DownFileInfo siteInfoBean = null; // 文件信息 Bean

	long[] nStartPos; // 开始位置

	long[] nEndPos; // 结束位置

	DownFileFetchSplitter[] fileSplitterFetch; // 子线程对象

	long nFileLength; // 文件长度

	boolean bFirst = true; // 是否第一次取文件

	boolean bStop = false; // 停止标志

	File tmpFile; // 文件下载的临时信息

	DataOutputStream output; // 输出到文件的输出流

	boolean fileflag; // 是本地上传还是远程下载的标志

	File downfile; // 本地文件下载

	int splitter = 0;


	/**
	 * 下载上传文件抓取初始化
	 * 
	 * @param bean
	 * @throws IOException
	 */
	public DownFileFetch( DownFileInfo bean ) throws IOException {
		siteInfoBean = bean;
		/**
		 * File.separator windows是\,unix是/
		 */
		tmpFile = new File(bean.getSFilePath() + File.separator + bean.getSFileName() + ".info");
		if ( tmpFile.exists() ) {
			bFirst = false;
			// 读取已下载的文件信息
			read_nPos();
		} else {
			nStartPos = new long[bean.getNSplitter()];
			nEndPos = new long[bean.getNSplitter()];
		}
		fileflag = bean.getFileflag();
		downfile = bean.getDownfile();
		this.splitter = bean.getNSplitter();
	}


	@Override
	public void run() {
		// 获得文件长度
		// 分割文件
		// 实例 FileSplitterFetch
		// 启动 FileSplitterFetch 线程
		// 等待子线程返回
		try {
			long beginTime = System.currentTimeMillis();
			nFileLength = getFileSize();
			if ( bFirst ) {
				if ( nFileLength == -1 ) {
					logger.debug("File Length is not known!");
				} else if ( nFileLength == -2 ) {
					logger.debug("File is not access!");
				} else {
					// 58238648 /5 = 11647729.6
					for ( int i = 0 ; i < nStartPos.length ; i++ ) {
						nStartPos[i] = i * (nFileLength / nStartPos.length);
					}
					for ( int i = 0 ; i < nEndPos.length - 1 ; i++ ) {
						nEndPos[i] = nStartPos[i + 1];
					}
					nEndPos[nEndPos.length - 1] = nFileLength; // 最后一次的截至长度为文件总大小
				}
			}
			// 启动子线程
			fileSplitterFetch = new DownFileFetchSplitter[nStartPos.length];
			for ( int i = 0 ; i < nStartPos.length ; i++ ) {
				fileSplitterFetch[i] = new DownFileFetchSplitter(
						siteInfoBean.getSSiteURL(), siteInfoBean.getSFilePath()
								+ File.separator + siteInfoBean.getSFileName() + "_" + i, nStartPos[i], nEndPos[i], i,
						fileflag, downfile, bFirst);
				logger.debug("Thread " + i + " , nStartPos = " + nStartPos[i] + ", nEndPos = " + nEndPos[i]);
				fileSplitterFetch[i].start();
			}
			// 下载子线程是否完成标志
			boolean breakWhile = false;
			while ( !bStop ) {
				write_nPos();
				Thread.sleep(500);
				breakWhile = true;
				for ( int i = 0 ; i < nStartPos.length ; i++ ) {
					if ( !fileSplitterFetch[i].bDownOver ) {
						breakWhile = false;
						break;
					} else {
						write_nPos();
					}
				}
				if ( breakWhile ) {
					break;
				}
			}
			long endLength = hebinfile(
				siteInfoBean.getSFilePath() + File.separator + siteInfoBean.getSFileName(), splitter);
			if ( nFileLength - endLength == 0 ) {
				deleteTempFile();

				if ( downListener != null ) {
					downListener.success();
				}
			}
			logger.debug("文件下载结束！ 用时 "
					+ (System.currentTimeMillis() - beginTime) + "毫秒" + "剩余:" + (nFileLength - endLength) + "b");
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 删除临时文件
	 */
	private boolean deleteTempFile() {
		boolean flag = tmpFile.delete();
		for ( int i = 0 ; i < nStartPos.length ; i++ ) {
			flag = flag && new File(siteInfoBean.getSFilePath(), siteInfoBean.getSFileName() + "_" + i).delete();
		}
		return flag;
	}


	/**
	 * 获得文件长度
	 * 
	 * @return
	 */
	public long getFileSize() {
		int _nFileLength = -1;
		if ( fileflag ) {
			try {
				URL url = new URL(siteInfoBean.getSSiteURL());
				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("User-Agent", "NetFox");
				int responseCode = httpConnection.getResponseCode();
				if ( responseCode >= 400 ) {
					processErrorCode(responseCode);
					// represent access is error
					return -2;
				}
				String sHeader;
				for ( int i = 1 ; ; i++ ) {
					sHeader = httpConnection.getHeaderFieldKey(i);
					if ( sHeader != null ) {
						if ( sHeader.equals("Content-Length") ) {
							_nFileLength = Integer.parseInt(httpConnection.getHeaderField(sHeader));
							break;
						}
					} else {
						break;
					}
				}
			} catch ( IOException e ) {
				logger.error(e.getMessage(), e);
			} catch ( Exception e ) {
				logger.error(e.getMessage(), e);
			}
			logger.debug("file length:{}", _nFileLength);
		} else {
			try {
				File myflie = downfile;
				nFileLength = (int) myflie.length();
			} catch ( Exception e ) {
				logger.error(e.getMessage(), e);
			}
			logger.debug("file length:{}", _nFileLength);
		}
		return _nFileLength;
	}


	/**
	 * 读取保存的下载信息（文件指针位置）
	 */
	private void read_nPos() {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(tmpFile));
			int nCount = input.readInt();
			nStartPos = new long[nCount];
			nEndPos = new long[nCount];
			for ( int i = 0 ; i < nStartPos.length ; i++ ) {
				nStartPos[i] = input.readLong();
				nEndPos[i] = input.readLong();
			}
			input.close();
		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 输出错误信息
	 * 
	 * @param nErrorCode
	 */
	private void processErrorCode( int nErrorCode ) {
		logger.equals("Error Code : " + nErrorCode);
	}


	/**
	 * 保存下载信息（文件指针位置）
	 */
	private void write_nPos() {
		try {
			output = new DataOutputStream(new FileOutputStream(tmpFile));
			output.writeInt(nStartPos.length);
			for ( int i = 0 ; i < nStartPos.length ; i++ ) {
				output.writeLong(fileSplitterFetch[i].nStartPos);
				output.writeLong(fileSplitterFetch[i].nEndPos);
			}
			output.close();
		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 停止文件下载
	 */
	public void siteStop() {
		bStop = true;
		for ( int i = 0 ; i < nStartPos.length ; i++ ) {
			fileSplitterFetch[i].splitterStop();
		}
	}


	/**
	 * 合并文件
	 * 
	 * @param sName
	 * @param splitternum
	 */
	private long hebinfile( String sName, int splitternum ) {
		try {
			File file = new File(sName);
			if ( file.exists() ) {
				file.delete();
			}
			RandomAccessFile saveinput = new RandomAccessFile(sName, "rw");
			for ( int i = 0 ; i < splitternum ; i++ ) {
				try {
					RandomAccessFile input = new RandomAccessFile(new File(sName + "_" + i), "r");
					byte[] b = new byte[1024];
					int nRead;
					while ( (nRead = input.read(b, 0, 1024)) > 0 ) {
						write(saveinput, b, 0, nRead);
					}
					input.close();
				} catch ( Exception e ) {
					logger.error(e.getMessage(), e);
				}
			}
			logger.debug("file size is " + saveinput.length());
			return saveinput.length();
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}


	/**
	 * 写文件
	 * 
	 * @param b
	 * @param nStart
	 * @param nLen
	 * @return
	 */
	private int write( RandomAccessFile oSavedFile, byte[] b, int nStart, int nLen ) {
		int n = -1;
		try {
			oSavedFile.seek(oSavedFile.length());
			oSavedFile.write(b, nStart, nLen);
			n = nLen;
		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		}
		return n;
	}


	@SuppressWarnings( "hiding" )
	public void addListener( DownListener downListener ) {
		this.downListener = downListener;
	}
}
